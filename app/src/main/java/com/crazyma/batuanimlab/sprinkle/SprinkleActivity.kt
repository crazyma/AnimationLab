package com.crazyma.batuanimlab.sprinkle

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_sprinkle.*

class SprinkleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sprinkle)
    }

    fun buttonClicked(v: View){
        sprinkleView.runAnim()
    }

}