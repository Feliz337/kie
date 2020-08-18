package cc.foxa.kie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.umeng.message.UmengNotifyClickActivity
import org.android.agoo.common.AgooConstants

class NotifyClickActivity : UmengNotifyClickActivity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_notify_click)

    }

    override fun onMessage(intent: Intent?) {
        super.onMessage(intent)
        Log.i(TAG, intent?.getStringExtra(AgooConstants.MESSAGE_BODY) ?: "")

    }

    companion object {
        const val TAG = "NotifyClickActivity"
    }
}