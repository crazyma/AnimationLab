package com.crazyma.batuanimlab.slot

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.crazyma.batuanimlab.R
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_ONE
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_THREE
import com.crazyma.batuanimlab.slot.SlotFlyView.Companion.SLOT_INDEX_TWO
import kotlinx.android.synthetic.main.layout_slot_machine.view.*
import java.util.*

class SlotMachineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {

        const val STATE_IDLE = 0
        const val STATE_PULLING = 1
        const val STATE_ROLLING = 2
        const val STATE_FINISH = 3

        private const val RATIO_STICK_SCALE_ANIMATION = .25f
        private const val RATIO_STICK_TRANSLATION_ANIMATION = .1f
        private const val RATIO_HANDLE_ANIMATION = .15f

        private const val ANIM_FLESH_INTERVAL = 500L
        private const val ANIM_DURATION_PULLING = 1000L
        private const val ANIM_DURATION_RELEASING = 200L
    }

    interface SlotMachineListener {
        fun onStickClicked()
        fun onRollingEnd()
    }

    var repeatable = true

    var listener: SlotMachineListener? = null

    private var slotState = STATE_IDLE

    private var timer: Timer? = null
    private var stickTouched = false
    private var slotViewWidth = 0
    private var slotViewMarginTop = 0
    private var leftSlotViewMarginStart = 0
    private var centerSlotViewMarginStart = 0
    private var rightSlotViewMarginStart = 0
    private var stickerRect = Rect()
    private var stickerAnimDistance = 0f

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_slot_machine, this, true)
        startLightAnimation()
        setupLaunchButton()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        stickerRect = Rect((w * .85f).toInt(), (h * .2f).toInt(), (w * .95).toInt(), (h * .6f).toInt())
        stickerAnimDistance = h * RATIO_HANDLE_ANIMATION

        slotViewWidth = (w * 68f / 360f).toInt()

        slotViewMarginTop = (w * 0.215f).toInt()
        leftSlotViewMarginStart = (w * 0.20f).toInt()
        centerSlotViewMarginStart = (w * 0.395f).toInt()
        rightSlotViewMarginStart = (w * 0.59f).toInt()

        Handler().post {
            adjustSlotViewPosition()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startLightAnimation()
    }

    override fun onDetachedFromWindow() {
        endLightAnimation()
        super.onDetachedFromWindow()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (stickerRect.contains(event.x.toInt(), event.y.toInt())) {
                    stickTouched = true
                }
                true
            }

            MotionEvent.ACTION_UP -> {
                if (stickerRect.contains(event.x.toInt(), event.y.toInt()) && stickTouched) {
                    askPullingStick()
                }
                stickTouched = false
                true
            }

            MotionEvent.ACTION_CANCEL -> {
                stickTouched = false
                true
            }

            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val savedState = SavedState(super.onSaveInstanceState()!!)
        savedState.apply {
            slotState = this@SlotMachineView.slotState
        }
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)

            state.run {
                this@SlotMachineView.slotState = when (slotState) {
                    STATE_FINISH -> STATE_FINISH
                    else -> STATE_IDLE
                }
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun startRolling() {
        leftSlotView.startRolling()
        centerSlotView.startRolling()
        rightSlotView.startRolling {
            slotState = STATE_FINISH
            listener?.onRollingEnd()
        }
    }

    private fun isReady() = leftSlotView.isReady() && centerSlotView.isReady() && rightSlotView.isReady()

    private fun adjustSlotViewPosition() {
        (leftSlotView.layoutParams as LayoutParams).apply {
            width = slotViewWidth
            height = slotViewWidth
            setMargins(leftSlotViewMarginStart, slotViewMarginTop, 0, 0)
        }.let {
            leftSlotView.layoutParams = it
        }

        (centerSlotView.layoutParams as LayoutParams).apply {
            width = slotViewWidth
            height = slotViewWidth
            setMargins(centerSlotViewMarginStart, slotViewMarginTop, 0, 0)
        }.let {
            centerSlotView.layoutParams = it
        }

        (rightSlotView.layoutParams as LayoutParams).apply {
            width = slotViewWidth
            height = slotViewWidth
            setMargins(rightSlotViewMarginStart, slotViewMarginTop, 0, 0)
        }.let {
            rightSlotView.layoutParams = it
        }
    }

    /**
     * Setup the SltView, including the 'slot index', 'ended resource index' and 'resource list'.
     * Also, call the init ui method.
     *
     * You MUST call this method to play this machine, especially configuration change happened.
     */
    fun setupSlotViews(bitmapList: List<Bitmap>, resultIndex: IntArray) {
        leftSlotView.apply {
            this.bitmaps = bitmapList
            slotIndex = SLOT_INDEX_ONE
            endBitmapIndex = resultIndex[0]

            if (slotState == STATE_FINISH) finalPosition()
            else initPosition()
        }
        centerSlotView.apply {
            this.bitmaps = bitmapList
            slotIndex = SLOT_INDEX_TWO
            endBitmapIndex = resultIndex[1]

            if (slotState == STATE_FINISH) finalPosition()
            else initPosition()
        }
        rightSlotView.apply {
            this.bitmaps = bitmapList
            slotIndex = SLOT_INDEX_THREE
            endBitmapIndex = resultIndex[2]

            if (slotState == STATE_FINISH) finalPosition()
            else initPosition()
        }

        launchButton.isEnabled = true
    }

    private fun askPullingStick() {
        if (isReady()) {
            when (slotState) {
                STATE_IDLE -> {
                    startPullStickAnimation()
                }
                STATE_FINISH -> {
                    if (repeatable) {
                        startPullStickAnimation()
                    }
                }
            }
        }
    }

    private fun setupLaunchButton() {
        launchButton.setOnClickListener {
            askPullingStick()
        }
    }

    private fun startLightAnimation() {
        timer?.cancel()
        timer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    Handler(Looper.getMainLooper()).post {
                        findViewById<ImageView>(R.id.slotLightImageView)?.apply {
                            tag = when (tag) {
                                true -> {
                                    setImageResource(R.drawable.img_slot_light01)
                                    false
                                }
                                else -> {
                                    setImageResource(R.drawable.img_slot_light02)
                                    true
                                }
                            }
                        }
                    }
                }
            }, ANIM_FLESH_INTERVAL, ANIM_FLESH_INTERVAL)
        }
    }

    private fun endLightAnimation() {
        timer?.cancel()
    }

    private fun startPullStickAnimation() {

        val handleAnim = AnimatorSet().apply {
            playSequentially(ObjectAnimator.ofFloat(
                slotHandleImageView,
                "translationY",
                stickerAnimDistance
            ).apply {
                interpolator = DecelerateInterpolator()
                duration = ANIM_DURATION_PULLING
            }, ObjectAnimator.ofFloat(
                slotHandleImageView,
                "translationY",
                0f
            ).apply {
                interpolator = AccelerateInterpolator()
                duration = ANIM_DURATION_RELEASING
            })
        }

        val stickTranslationOffset = slotStickImageView.height * RATIO_STICK_TRANSLATION_ANIMATION
        val stickAnim1 = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(
                    slotStickImageView,
                    "translationY",
                    stickTranslationOffset
                ),
                ObjectAnimator.ofFloat(
                    slotStickImageView,
                    "scaleY",
                    1 - RATIO_STICK_SCALE_ANIMATION
                )
            )
            duration = ANIM_DURATION_PULLING
        }

        val stickAnim2 = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(
                    slotStickImageView,
                    "translationY",
                    0f
                ),
                ObjectAnimator.ofFloat(
                    slotStickImageView,
                    "scaleY",
                    1f
                )
            )
            duration = ANIM_DURATION_RELEASING
        }

        val stickAnim = AnimatorSet().apply {
            playSequentially(stickAnim1, stickAnim2)
        }

        AnimatorSet().apply {
            playTogether(handleAnim, stickAnim)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    slotState = STATE_ROLLING
                    startRolling()
                }

                override fun onAnimationCancel(animation: Animator?) {
                    slotState = STATE_IDLE
                }

                override fun onAnimationStart(animation: Animator?) {
                    slotState = STATE_PULLING
                }

            })
        }.start()

    }

    internal class SavedState : BaseSavedState {
        //        var value: Int = 0 //this will store the current value from ValueBar
        var slotState = 0

        constructor(source: Parcel) : super(source) {
            slotState = source.readInt()
        }

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(slotState)
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