package com.example.peacock.wheatherapplication;

/**
 * Created by peacock on 3/30/16.
 */

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlideCircleTransform extends BitmapTransformation {
    public GlideCircleTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        return ImageUtil.getCircularBitmapImage(source);
    }

    @Override
    public String getId() {
        return "Glide_Circle_Transformation";
    }
}