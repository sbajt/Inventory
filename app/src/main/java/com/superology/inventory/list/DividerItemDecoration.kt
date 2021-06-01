package com.superology.inventory.list

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class DividerItemDecoration(val context: Context?) :
    RecyclerView.ItemDecoration() {

    private val TAG = DividerItemDecoration::class.java.canonicalName
    private val dividerHeight: Int by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            context?.resources?.displayMetrics
        ).toInt()
    }
    private val horizontalMargin: Int by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f,
            context?.resources?.displayMetrics
        ).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top += dividerHeight
        }
        if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount?.minus(1)))
            outRect.bottom += 2 * dividerHeight

        outRect.left += horizontalMargin
        outRect.right += horizontalMargin
        outRect.bottom += dividerHeight
    }

}