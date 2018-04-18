package sebner.dev.swdestinyutilitykotlin.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.pow
import kotlin.math.round

/*
 * Various kotlin syntactic sugar methods
 */
fun ImageView.loadImage(url: String) {
    Glide.with(context).load(url).into(this)
}

inline fun<T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun Double.roundToTwoDecimals(): Double {
    val power = (10.0).pow(2)
    return round(this * power) / power
}

fun <T> apiCallback(success: ((Response<T>) -> Unit)?, failure: ((t: Throwable) -> Unit)? = null): Callback<T> {
    return object : Callback<T> {
        override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) { success?.invoke(response) }
        override fun onFailure(call: Call<T>, t: Throwable) { failure?.invoke(t) }
    }
}

fun <T> Call<T>.enqueue(success: (response: Response<T>) -> Unit,
                        failure: (t: Throwable) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) = success(response)

        override fun onFailure(call: Call<T>?, t: Throwable) = failure(t)
    })
}