package cc.foxa.kie

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cc.foxa.kie.databinding.ActivityMainBinding
import cc.foxa.kie.databinding.ItemCommentBinding


class MainAdapter : ListAdapter<Comment, CommentViewHolder>(CommentDiffer) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CommentViewHolder(
    private val binding: ItemCommentBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment) {
        binding.comment = comment
        binding.executePendingBindings()
        binding.container.setOnLongClickListener {
            val clipboard = it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("KIE_SIMPLE_TEXT", comment.content)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(it.context, "已复制内容", Toast.LENGTH_SHORT).show()

            true
        }
    }
}

object CommentDiffer : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.content == newItem.content
    }
}