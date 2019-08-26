package com.crazyma.batuanimlab.crop_row

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_crop_row.*

class CropRowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_row)




        Glide.with(imageView)
            .load("https://gcs.dcard.tw/reaction/aa0d425f-d530-4478-9a77-fe3aedc79eea1539599257655.png")
            .apply(RequestOptions.bitmapTransform(PartialLeftRoundCrop()))
            .into(imageView)


    }

}