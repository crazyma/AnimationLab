package com.crazyma.batuanimlab.progress

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_batu_progress_view.*

class BatuProgressViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batu_progress_view)
    }

    fun buttonClicked(v: View){
        val drawable =ContextCompat.getDrawable(this, R.drawable.img_waitlist_alert_info)
        batuProgressView.indicatorDrawable = drawable

        ObjectAnimator.ofFloat(batuProgressView,"percentage",1f,0f,1f).apply {
            duration = 1000
            start()
        }

    }

}