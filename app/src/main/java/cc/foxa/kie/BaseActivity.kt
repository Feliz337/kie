package cc.foxa.kie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umeng.message.PushAgent

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PushAgent.getInstance(this).onAppStart()

    }
}