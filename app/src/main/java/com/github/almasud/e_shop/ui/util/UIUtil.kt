package com.github.almasud.e_shop.ui.util

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.almasud.e_shop.R

object UIUtil {

    /**
     * Show/Hide the progressbar layout and disable/enable the UI
     */
    fun loadingUI(
        screenViewGroup: ViewGroup,
        progressView: View,
        textView: TextView? = null,
        isShow: Boolean,
        title: String? = null
    ) {
        if (isShow) {
            if (title != null) {
                textView?.text = title
            } else {
                textView?.setText(R.string.please_wait)
            }
        }

        progressView.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
        enableDisableViewGroup(screenViewGroup, !isShow)
    }

    /**
     * Enables/Disables all child views in a view group.
     *
     * @param viewGroup the view group
     * @param enabled   `true` to enable, `false` to disable
     * the views.
     */
    fun enableDisableViewGroup(viewGroup: ViewGroup, enabled: Boolean) {
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            view.isEnabled = enabled
            if (view is ViewGroup) {
                enableDisableViewGroup(view, enabled)
            }
        }
    }

    fun getFirstOne(str: String): String {
        return if (str.length < 2) str else str.substring(0, 1)
    }

}