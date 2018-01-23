package sebner.dev.swdestinyutilitykotlin.ui

import android.support.v4.app.Fragment
import android.view.View

abstract class FilterFragment: Fragment() {
    abstract fun onFilterButtonPressed(v: View)
}