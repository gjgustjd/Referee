package com.example.referee.common

import android.content.Context
import android.util.DisplayMetrics
import kotlin.math.roundToInt

object CommonUtil {
    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        val px = (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt();
        return px;
    }

    fun pxToDp(context: Context, px: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        val dp = (px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt();
        return dp
    }
}