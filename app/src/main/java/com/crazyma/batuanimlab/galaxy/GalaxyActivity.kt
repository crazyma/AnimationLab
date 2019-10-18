package com.crazyma.batuanimlab.galaxy

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_galaxy.*

/**
 * @author Batu
 */
class GalaxyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galaxy)
    }

    fun buttonClicked(v: View){
        galaxyView.startAnim()
    }

}