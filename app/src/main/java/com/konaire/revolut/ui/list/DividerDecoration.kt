package com.konaire.revolut.ui.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View

import com.konaire.revolut.R

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
class DividerDecoration(
    context: Context,
    private val leftPadding: Int = 0,
    colorResource: Int = android.R.color.darker_gray,
    private val includeLastItem: Boolean = false
): RecyclerView.ItemDecoration() {
    private val paint: Paint

    init {
        val paint = Paint()
        val color = ContextCompat.getColor(context, colorResource)
        val size = context.resources.getDimensionPixelSize(R.dimen.divider_size)

        this.paint = paint.apply {
            this.strokeWidth = size.toFloat()
            this.color = color
        }
    }

    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        if (parent == null) {
            return
        }

        var i = 0
        var y: Int
        var child: View
        var params: RecyclerView.LayoutParams
        val left = parent.paddingLeft + leftPadding
        val right = parent.width - parent.paddingRight
        val length = parent.childCount - (if (includeLastItem) 0 else 1)

        while (i < length) {
            child = parent.getChildAt(i++)
            params = child.layoutParams as RecyclerView.LayoutParams
            y = child.bottom + params.bottomMargin

            c?.drawLine(left.toFloat(), y.toFloat(), right.toFloat(), y.toFloat(), paint)
        }
    }
}