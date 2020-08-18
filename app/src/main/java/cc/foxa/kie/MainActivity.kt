package cc.foxa.kie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cc.foxa.kie.databinding.ActivityMainBinding
import cc.foxa.kie.databinding.DialogPostCommentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.umeng.message.PushAgent
import java.util.*

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()

    lateinit var binding : ActivityMainBinding

    var deviceToken :String? = null

    val preferences by lazy { SharedPreferencesUtils(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as KieApplication).registerPush { isSuccess, token ->
            if (isSuccess) deviceToken = token
            viewModel.setDeviceToken(deviceToken)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.commentSwipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }



        viewModel.commentLoading.observe(this) {
            binding.commentSwipeRefresh.isRefreshing = it
        }

        val adapter = MainAdapter()
        binding.recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        viewModel.comments.observe(this) {
            adapter.submitList(it)
        }


        binding.btnPostComment.setOnClickListener {
            val dialogBinding = DialogPostCommentBinding.inflate(layoutInflater, null, false)
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("发送")
                .setView(dialogBinding.root)
                .setPositiveButton("确定") { _, _ ->
                    viewModel.postComment(dialogBinding.content.editText!!.text.toString())
                    binding.recyclerView.smoothScrollToPosition(0)

                }
                .setNegativeButton("取消") { which, _ ->
                    which.dismiss()
                }
                .create()
            dialog.show()
        }

        viewModel.toast.observe(this, EventObserver {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

        })

        viewModel.scrollListTo.observe(this, EventObserver {
//            binding.recyclerView.smoothScrollToPosition(it)
//            layoutManager.scrollToPosition(it)
            layoutManager.smoothScrollToPosition(binding.recyclerView, null, 0)
//            binding.executePendingBindings()

        })

//        viewModel.postButtonVisible.observe(this) {
//            if (it) binding.btnPostComment.show()
//            else binding.btnPostComment.hide()
//        }

        binding.statusSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setPushEnabled(isChecked)
        }

        viewModel.writePushStateToSP.observe(this, EventObserver {
            preferences.pushEnabled = it
        })

        viewModel.pushStateFromSP = preferences.pushEnabled





    }

    override fun onPause() {
        super.onPause()
        preferences.pushEnabled = viewModel.overviewPushEnabled.value!!
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
        viewModel.setDeviceToken(deviceToken)
        viewModel.pushStateFromSP = preferences.pushEnabled
    }


}