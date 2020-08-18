package cc.foxa.kie

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("comments")
fun comments(recyclerView: RecyclerView, comments: List<Comment>) {
    if (recyclerView.adapter == null) recyclerView.adapter = MainAdapter()
    if (comments.isNullOrEmpty()) {
        recyclerView.isVisible = false
    } else {
        recyclerView.isVisible = true
        (recyclerView.adapter as MainAdapter).submitList(comments)
    }
}

@BindingAdapter("visibleWhen")
fun visibleWhen(view: View, condition: Boolean) {
    if (condition) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.INVISIBLE
    }
}