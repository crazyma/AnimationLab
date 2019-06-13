package com.crazyma.batuanimlab

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_card_flig.*

class CardFlipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_flig)
    }

    fun buttonClicked(v: View){
        launchAnim()
    }

    private fun launchAnim(){
        val showAnimation = AnimatorInflater.loadAnimator(this, R.animator.card_flip) as AnimatorSet
        showAnimation.setTarget(textView)
        showAnimation.start()

    }
}