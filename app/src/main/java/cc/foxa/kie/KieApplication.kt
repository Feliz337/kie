package cc.foxa.kie

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengCallback
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import org.android.agoo.xiaomi.MiPushRegistar

class KieApplication : Application() {

    var deviceToken: String? = null
    override fun onCreate() {
        super.onCreate()

        registerPush { isSuccess, token ->
            if (isSuccess) deviceToken = token
        }
    }

    fun registerPush(callback: (Boolean, String) -> Unit){
        UMConfigure.init(
            this, BuildConfig.UMENG_APPKEY, "Common",
            UMConfigure.DEVICE_TYPE_PHONE, BuildConfig.UMENG_MESSAGE_SECRET
        )
        val pushAgent = PushAgent.getInstance(this)

        pushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String?) {
                Log.i(TAG, "注册成功：deviceToken=${deviceToken}")
                callback(true, deviceToken!!)
            }

            override fun onFailure(p0: String?, p1: String?) {
                Log.e(TAG, "注册失败：p0=${p0}, p1=${p1}")
                callback(false, "")
            }
        })

        MiPushRegistar.register(this, BuildConfig.XIAOMI_APPID, BuildConfig.XIAOMI_APPKEY)

    }
    companion object {
        const val TAG = "KieApplication"
    }
}