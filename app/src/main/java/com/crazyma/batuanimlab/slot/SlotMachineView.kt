package com.crazyma.batuanimlab.slot

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
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
        const val RATIO_STICK_SCALE_ANIMATION = .25f
        const val RATIO_STICK_TRANSLATION_ANIMATION = .1f
        const val RATIO_HANDLE_ANIMATION = .15f

        private const val ANIM_DURATION_PULLING = 1000L
        private const val ANIM_DURATION_RELEASING = 200L
    }

    interface OnStickClickListener {
        fun onStickClicked()
    }

    val list = listOf(
        R.drawable.img_slot_card,
        R.drawable.img_nexttime,
        R.drawable.img_tryagain
    )

    var stickClickListener: OnStickClickListener? = null

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
//        setupSlotViews()
        startLightAnimation()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //  20%
        // 85% 95%
        // 60%

        stickerRect = Rect((w * .85f).toInt(), (h * .2f).toInt(), (w * .95).toInt(), (h * .6f).toInt())
        stickerAnimDistance = h * RATIO_HANDLE_ANIMATION

        Log.d("badu", "slotStickImageView x: ${slotStickImageView.x} , y: ${slotStickImageView.y}")
        Log.d("badu", "slotStickImageView width: ${slotStickImageView.width} , height: ${slotStickImageView.height}")

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
                    startPullStickAnimation()
                    stickClickListener?.onStickClicked()
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

    fun startRolling() {
        leftSlotView.startRolling()
        centerSlotView.startRolling()
        rightSlotView.startRolling()
    }

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

    fun setupSlotViews() {
        leftSlotView.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_ONE
            endDrawableIndex = 2
            initPosition()
        }
        centerSlotView.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_TWO
            endDrawableIndex = 1
            initPosition()
        }
        rightSlotView.apply {
            drawableResIds = list
            slotIndex = SLOT_INDEX_THREE
            endDrawableIndex = 2
            initPosition()
        }
    }

    private fun startLightAnimation() {
        timer?.cancel()
        timer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
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
            }, 500, 500)
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
        }.start()

    }
}