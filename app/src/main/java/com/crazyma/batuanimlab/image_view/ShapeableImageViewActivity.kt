package com.crazyma.batuanimlab.image_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_shapeable_image.*

/**
 * @author Batu
 */
class ShapeableImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shapeable_image)
        setupImageView()
    }

    private fun setupImageView() {

        listOf(
            imageView1, imageView2, imageView3, imageView4,
            imageView5, imageView6, imageView7, imageView8
        ).forEach {
            Glide.with(this)
                .load("https://www.washingtonpost.com/resizer/BUWi4MdISXS-R4MWrjkMy5ikbpk=/arc-anglerfish-washpost-prod-washpost/public/FTGO64JUAJGJTFZ5WVPR6XRQDM.jpg")
                .apply(RequestOptions().centerCrop())
                .into(it)
        }
    }

}