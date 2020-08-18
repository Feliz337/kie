package cc.foxa.kie

import android.view.LayoutInflater
import android.view.ViewGroup
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