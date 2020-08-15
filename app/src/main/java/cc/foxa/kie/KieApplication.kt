package cc.foxa.kie

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengCallback
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import org.android.agoo.xiaomi.MiPushRegistar

class KieApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        UMConfigure.init(
            this, BuildConfig.UMENG_APPKEY, "Common",
            UMConfigure.DEVICE_TYPE_PHONE, BuildConfig.UMENG_MESSAGE_SECRET
        )

        val pushAgent = PushAgent.getInstance(this)

        pushAgent.register(object : IUmengRegisterCallback {
            override fun onSuccess(deviceToken: String?) {
                Log.i(TAG, "注册成功：deviceToken=${deviceToken}")
                Toast.makeText(this@KieApplication, "注册成功：deviceToken=${deviceToken}", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(p0: String?, p1: String?) {
                Log.e(TAG, "注册失败：p0=${p0}, p1=${p1}")
                Toast.makeText(this@KieApplication, "注册失败：p0=${p0}, p1=${p1}", Toast.LENGTH_LONG).show()
            }
        })

        MiPushRegistar.register(this, BuildConfig.XIAOMI_APPID, BuildConfig.XIAOMI_APPKEY)
    }

    companion object {
        const val TAG = "KieApplication"

    }
}