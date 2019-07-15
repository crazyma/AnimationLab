package com.crazyma.batuanimlab.inner_trans

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_inner_transition.*

class InnerTransitionActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_transition)
    }

    fun buttonClicked(v: View){
        when(v.id){
            R.id.toTopButton -> {
                innerTransitionView.moveToTopPosition()
            }

            R.id.toClipButton -> {
                innerTransitionView.moveToClipPosition()
            }
        }
    }

}