package cc.foxa.kie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.umeng.message.PushAgent
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.math.log

class MainViewModel : ViewModel() {
    val comments = MutableLiveData<List<Comment>>(emptyList())

    val commentLoading = MutableLiveData<Boolean>(true)

    val commentStatus = MutableLiveData<Int>(STATUS_SUCCESS)

    val overviewLoading = MutableLiveData<Boolean>(true)

    val overviewStatus = MutableLiveData<Int>(STATUS_SUCCESS)

    val overviewPushEnabled = MutableLiveData<Boolean>(false)


    val deviceToken = MutableLiveData<String>("")

    val message = MutableLiveData<String>("")

    val overviewTitle = MutableLiveData<String>("")

    val toast = MutableLiveData<Event<String>>()

    val postStatus = MutableLiveData<Int>(STATUS_SUCCESS)

    val postLoading = MutableLiveData<Boolean>(false)

    val postButtonVisible = MutableLiveData<Boolean>()

    val writePushStateToSP = MutableLiveData<Event<Boolean>>()

    var pushStateFromSP: Boolean? = null

    val scrollListTo = MutableLiveData<Event<Int>>()


    val deviceTokenShorted = deviceToken.map {
        if(it.length >= 5) {
            pushStateFromSP?.let { setPushEnabled(it) }
            it.substring(0, 5)
        } else {
            ""
        }
    }


    private val api: CommentApi

    init {
        val client = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
        api = retrofit.create(CommentApi::class.java)

        refresh()
        setPushEnabled(true)
    }

    fun refresh() {
        commentLoading.postValue(true)
        viewModelScope.launch {
            try {
                val list = api.getComments().sortedByDescending { it.createdAt }
                comments.postValue(list)
                commentLoading.postValue(false)
                commentStatus.postValue(STATUS_SUCCESS)
            } catch (e: Exception) {
                commentLoading.postValue(false)
                commentStatus.postValue(STATUS_FAILED)
            }
        }

    }

    fun setPushEnabled(enabled: Boolean) {
        overviewLoading.postValue(true)
        viewModelScope.launch {
            try {
                if (enabled && deviceToken.value!!.isNotBlank()) {
                    api.enable(DeviceInfo(deviceToken.value!!))
                    overviewPushEnabled.postValue(true)
                    overviewStatus.postValue(STATUS_SUCCESS)
                    overviewLoading.postValue(false)
                    overviewTitle.postValue("推送接收：已开启")
                    toast.postValue(Event("推送接收：已开启"))
                    writePushStateToSP.postValue(Event(true))
                } else if(!enabled && deviceToken.value!!.isNotBlank()) {
                    api.disable(DeviceInfo(deviceToken.value!!))
                    overviewPushEnabled.postValue(false)
                    overviewStatus.postValue(STATUS_SUCCESS)
                    overviewLoading.postValue(false)
                    overviewTitle.postValue("推送接收：已关闭")
                    toast.postValue(Event("推送接收：已关闭"))
                    writePushStateToSP.postValue(Event(false))
                } else {

                    overviewPushEnabled.postValue(false)
                    overviewStatus.postValue(STATUS_FAILED)
                    overviewLoading.postValue(false)

                }
            } catch (e: Exception) {
                overviewPushEnabled.postValue(false)
                overviewStatus.postValue(STATUS_FAILED)

                overviewLoading.postValue(false)
                toast.postValue(Event("状态更新失败"))
                writePushStateToSP.postValue(Event(false))

            }

        }
    }

    fun postComment(content: String) {
        val comment = CommentToPost(content, deviceToken.value?:"")

        scrollListTo.postValue(Event(0))
        viewModelScope.launch{
            postLoading.postValue(true)
            toast.postValue(Event("正在发送"))
            postButtonVisible.postValue(false)
            try {
                api.postComment(comment)
                postLoading.postValue(false)
                postStatus.postValue(STATUS_SUCCESS)
                toast.postValue(Event("发送成功"))
                postButtonVisible.postValue(true)
                refresh()
            } catch (e: Exception) {
                postLoading.postValue(false)
                postStatus.postValue(STATUS_FAILED)
                toast.postValue(Event("发送失败"))
                postButtonVisible.postValue(true)
            }
        }
    }

    fun setDeviceToken(token: String?) {
        deviceToken.postValue(token?:"")
    }

    fun getDeviceToken(): String {
        return deviceToken.value!!.substring(0, 5)
    }

    companion object {
        const val STATUS_SUCCESS = 0x01
        const val STATUS_FAILED = 0x02
    }

}
