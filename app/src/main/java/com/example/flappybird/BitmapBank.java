package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapBank {
    private static Bitmap background, bird, pipeTop, pipeBottom;

    public BitmapBank(Context context) {
        // 1. Nạp và scale Background (Full màn hình)
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.flappybirdbg);
        background = scaleBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        // 2. Nạp và scale Chim
        bird = BitmapFactory.decodeResource(context.getResources(), R.drawable.flappybird);
        bird = scaleBitmap(bird, Constants.BIRD_WIDTH, Constants.BIRD_HEIGHT);

        // 3. Nạp và scale Ống
        pipeTop = BitmapFactory.decodeResource(context.getResources(), R.drawable.toppipe);
        pipeTop = scaleBitmap(pipeTop, Constants.PIPE_WIDTH, Constants.PIPE_HEIGHT);

        pipeBottom = BitmapFactory.decodeResource(context.getResources(), R.drawable.bottompipe);
        pipeBottom = scaleBitmap(pipeBottom, Constants.PIPE_WIDTH, Constants.PIPE_HEIGHT);
    }

    public Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }

    public static Bitmap getBackground() { return background; }
    public static Bitmap getBird() { return bird; }
    public static Bitmap getPipeTop() { return pipeTop; }
    public static Bitmap getPipeBottom() { return pipeBottom; }
}