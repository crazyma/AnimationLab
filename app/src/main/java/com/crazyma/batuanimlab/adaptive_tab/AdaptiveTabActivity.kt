package com.crazyma.batuanimlab.adaptive_tab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.databinding.ActivityAdaptiveTabBinding

/**
 * @author Batu
 */
class AdaptiveTabActivity : AppCompatActivity() {

    private val binding: ActivityAdaptiveTabBinding by lazy {
        ActivityAdaptiveTabBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}