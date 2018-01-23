package sebner.dev.swdestinyutilitykotlin.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

/*
 * Various kotlin syntactic sugar methods
 */
fun ImageView.loadImage(url: String) {
    Glide.with(context).load(url).into(this)
}

inline fun<T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if(value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}