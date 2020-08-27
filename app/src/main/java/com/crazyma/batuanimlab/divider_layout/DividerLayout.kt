package com.crazyma.batuanimlab.divider_layout

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.crazyma.batuanimlab.R

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
        }

    @ColorInt
    var textColor: Int = 0

    @StyleRes
    var textAppearanceId: Int = 0

    private val strings = mutableListOf<String>()
    private var padding: Int = 0

    init {
        orientation = HORIZONTAL
        intervalWidth = (DEFAULT_INTERVAL_UNIT * context.resources.displayMetrics.density).toInt()

        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DividerLayout,
            0, 0
        )

        try {
            textAppearanceId = a.getResourceId(R.styleable.DividerLayout_textAppearance, 0)
            dividerResId = a.getResourceId(R.styleable.DividerLayout_divider, 0)
            textColor = a.getColor(R.styleable.DividerLayout_textColor, Color.BLACK)
        } finally {
            a.recycle()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState()!!)
        savedState.apply {
            strings = this@DividerLayout.strings
            dividerResId = this@DividerLayout.dividerResId
            textColor = this@DividerLayout.textColor
            textAppearanceId = this@DividerLayout.textAppearanceId
            intervalWidth = this@DividerLayout.intervalWidth
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
            textColor = state.textColor
            textAppearanceId = state.textAppearanceId
            intervalWidth = state.intervalWidth

            populateTextViews(strings)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    fun setMessages(messages: List<String>) {
        storeMessage(messages)
        populateTextViews(strings)
    }

    fun clear() {
        clearMessage()
        clearAllTextView()
    }

    private fun storeMessage(messages: List<String>) {
        strings.clear()
        strings.addAll(messages)
    }

    private fun clearMessage() {
        strings.clear()
    }

    private fun populateTextViews(messages: List<String>) {
        val bound = Rect()
        val textPaint = TextView(context).paint

        messages.forEach {
            textPaint.getTextBounds(it, 0, it.length, bound)
        }

        val layoutTotalWidth = width
        var w = 0
        var boundedIndex = -1
        var showLastBoundedMessage = false

        run loop@{
            messages.forEachIndexed { i, message ->
                if (i != 0) {
                    w += intervalWidth
                }
                if (w >= layoutTotalWidth) {
                    boundedIndex = i
                    showLastBoundedMessage = false
                    return@loop
                }

                textPaint.getTextBounds(message, 0, message.length, bound)
                w += bound.width()

                if (w >= layoutTotalWidth) {
                    boundedIndex = i
                    showLastBoundedMessage = true
                    return@loop
                }
            }
        }

        removeAllViews()
        messages.forEachIndexed { i, message ->
            if (boundedIndex != -1 && i >= boundedIndex && !showLastBoundedMessage) return@forEachIndexed

            populateTextView(message, i > 0)
        }
    }

    private fun populateTextView(message: String, drawablePrefix: Boolean) {
        val textView = TextView(context).apply {
            text = message
            if (textAppearanceId != 0) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    setTextAppearance(textAppearanceId)
                } else {
                    setTextAppearance(context, textAppearanceId)
                }
            }

            if (textColor != 0) {
                setTextColor(textColor)
            }

            compoundDrawablePadding = padding
            val drawable = ContextCompat.getDrawable(context, dividerResId)
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
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

        @ColorInt
        var textColor: Int = 0

        @StyleRes
        var textAppearanceId: Int = 0

        var intervalWidth: Int = 0

        constructor(source: Parcel) : super(source) {
            source.readStringList(strings)
            dividerResId = source.readInt()
            textColor = source.readInt()
            textAppearanceId = source.readInt()
            intervalWidth = source.readInt()
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeStringList(strings)
            out.writeInt(dividerResId)
            out.writeInt(textColor)
            out.writeInt(textAppearanceId)
            out.writeInt(intervalWidth)
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