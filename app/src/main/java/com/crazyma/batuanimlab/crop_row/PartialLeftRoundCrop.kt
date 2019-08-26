package com.crazyma.batuanimlab.crop_row

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class PartialLeftRoundCrop : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return toTransform
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }


    override fun equals(other: Any?): Boolean {
        return other != null && other is PartialLeftRoundCrop
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    companion object {
        //  TODO by Batu: change id value
        private const val ID = "com.bumptech.glide.transformations.FillSpace"
        private val ID_BYTES = ID.toByteArray(charset(STRING_CHARSET_NAME))
    }

}