package com.example.flappybird;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bird {
    private int x, y, velocity;
    private int width = 100, height = 100;

    public Bird(int screenX, int screenY) {
        this.x = screenX / 4;
        this.y = screenY / 2;
        this.velocity = 0;
    }

    public void update() {
        // TV3 viết logic rơi tự do: velocity += Constants.GRAVITY; y += velocity;
    }

    public void jump() {
        // TV3 viết logic nhảy: velocity = -30;
    }

    public void draw(Canvas canvas) {
        if (BitmapBank.getBird() != null) {
            canvas.drawBitmap(BitmapBank.getBird(), x, y, null);
        } else {
            // Vẽ hình vuông tạm thời để test
            Paint p = new Paint(); p.setColor(Color.RED);
            canvas.drawRect(getRect(), p);
        }
    }

    public Rect getRect() { return new Rect(x, y, x + width, y + height); }
    public int getY() { return y; }
}