package com.crazyma.batuanimlab.adaptive_tab

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.databinding.ActivityAdaptiveTabBinding
import com.google.android.material.tabs.TabLayoutMediator

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
        setupViewPager()

        Handler().postDelayed({
            binding.tabLayout.applyStyleByDeviceAndContentWidth()
        }, 3000)
    }

    private fun setupViewPager() {
        val adapter = PagerAdapter()
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = createText(position)
        }.attach()
    }

    private fun createText(position: Int): String {
        val count = position + 1
        val builder = StringBuilder()
        for (i in 0 until count) {
            builder.append("TAb")
        }
        builder.append(" $count")
        return builder.toString()
    }
}