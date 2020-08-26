package com.crazyma.batuanimlab.divider_layout

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * @author Batu
 */
class DividerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_INTERVAL_UNIT = 12
    }

    var intervalWidth: Int = 0

    @DrawableRes
    var dividerResId: Int = 0
        set(value) {
            field = value
            padding = calculateIntervalPadding(value)
            Log.d("badu", "padding: $padding")
        }

    private val strings = mutableListOf<String>()
    private var padding: Int = 0

    init {
        orientation = HORIZONTAL
        intervalWidth = (DEFAULT_INTERVAL_UNIT * context.resources.displayMetrics.density).toInt()
    }

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState()!!)
        savedState.apply {
            strings = this@DividerLayout.strings
            dividerResId = this@DividerLayout.dividerResId
        }
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)

            strings.apply {
                clear()
                addAll(state.strings)
            }
            dividerResId = state.dividerResId

            populateTextViews(strings)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("badu", "onSizeChanged: w: $w, h: $h")
    }

    fun addText(message: String) {
        val count = storeMessage(message)
        populateTextView(message, count > 1)
    }

    fun setTexts(messages: Collection<String>) {
        strings.clear()
        strings.addAll(messages)

        populateTextViews(strings)
    }

    fun clear() {
        clearMessage()
        clearAllTextView()
    }

    private fun storeMessage(message: String): Int {
        strings.add(message)
        return strings.size
    }

    private fun clearMessage() {
        strings.clear()
    }

    private fun populateTextViews(messages: List<String>) {
        removeAllViews()
        messages.forEachIndexed { index, message ->
            populateTextView(message, index > 0)
        }
    }

    private fun populateTextView(message: String, drawablePrefix: Boolean) {
        val textView = TextView(context).apply {
            text = message
            compoundDrawablePadding = padding
            val drawable = ContextCompat.getDrawable(context, dividerResId)

            setCompoundDrawablesWithIntrinsicBounds(
                if (drawablePrefix) dividerResId else 0,
                0,
                0,
                0
            )
        }
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            if (drawablePrefix) {
                marginStart = (4 * context.resources.displayMetrics.density).toInt()
            }
        }
        addView(textView, params)
    }

    private fun calculateIntervalPadding(@DrawableRes drawableResId: Int): Int {
        val drawable = ContextCompat.getDrawable(context, dividerResId)
        if (drawable != null) {
            val padding = (intervalWidth - drawable.intrinsicWidth) / 2
            return if (padding > 0) padding else 0
        }
        return 0
    }

    private fun clearAllTextView() {
        removeAllViews()
    }

    internal class SavedState : BaseSavedState {

        var strings = mutableListOf<String>()

        @DrawableRes
        var dividerResId: Int = 0

        constructor(source: Parcel) : super(source) {
            source.readStringList(strings)
            dividerResId = source.readInt()
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeStringList(strings)
            out.writeInt(dividerResId)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}