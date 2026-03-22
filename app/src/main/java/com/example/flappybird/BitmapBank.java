package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapBank {
    private static Bitmap background, bird, pipeTop, pipeBottom;

    public BitmapBank(Context context) {
        // TV2 sẽ nạp ảnh tại đây. Ví dụ:
        // background = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky);
        // background = scaleBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    public Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }

    public static Bitmap getBackground() { return background; }
    public static Bitmap getBird() { return bird; }
    public static Bitmap getPipeTop() { return pipeTop; }
    public static Bitmap getPipeBottom() { return pipeBottom; }
}