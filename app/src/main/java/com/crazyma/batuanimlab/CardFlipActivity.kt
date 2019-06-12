package com.crazyma.batuanimlab

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

        textView.animate().apply {
            rotationY(360f)
            duration = 1000
        }.start()
    }
}