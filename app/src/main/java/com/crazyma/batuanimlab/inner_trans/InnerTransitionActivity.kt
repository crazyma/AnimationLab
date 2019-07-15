package com.crazyma.batuanimlab.inner_trans

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_inner_transition.*

class InnerTransitionActivity : AppCompatActivity() {

    private var viewShrinkHeight = 0
    private var rootHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_transition)
        viewShrinkHeight = (resources.displayMetrics.density * 100).toInt()
    }

    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.toTopButton -> {
                if (rootHeight == 0) {
                    rootHeight = rootLayout.height
                }

                val duration = 1000L
                ValueAnimator.ofInt(viewShrinkHeight, rootHeight).apply {
                    this.duration = duration
                    addUpdateListener {
                        innerTransitionView.layoutParams.height = it.animatedValue as Int
                        innerTransitionView.requestLayout()
                    }
                    start()
                }



                innerTransitionView.moveToTopPosition(duration)
            }

            R.id.toClipButton -> {
                if (rootHeight == 0) {
                    rootHeight = rootLayout.height
                }

                val duration = 1000L
                ValueAnimator.ofInt(rootHeight, viewShrinkHeight).apply {
                    this.duration = duration
                    addUpdateListener {
                        innerTransitionView.layoutParams.height = it.animatedValue as Int
                        innerTransitionView.requestLayout()
                    }
                    start()
                }


                innerTransitionView.moveToClipPosition()
            }
        }
    }

}