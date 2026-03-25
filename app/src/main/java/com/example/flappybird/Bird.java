package com.example.flappybird;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bird {

    private static final int GRAVITY = 2;
    private static final int JUMP_FORCE = -30;

    private int x, y, velocity;
    private int width = 100, height = 100;

    public Bird(int screenX, int screenY) {
        this.x = screenX / 4;
        this.y = screenY / 2;
        this.velocity = 0;
    }

    public void update() {
        velocity += GRAVITY;
        y += velocity;
    }

    public void jump() {
        velocity = JUMP_FORCE;
    }

    public void draw(Canvas canvas) {
        if (BitmapBank.getBird() != null) {
            this.width = BitmapBank.getBird().getWidth();
            this.height = BitmapBank.getBird().getHeight();

            canvas.drawBitmap(BitmapBank.getBird(), x, y, null);
        } else {
            Paint p = new Paint();
            p.setColor(Color.RED);
            canvas.drawRect(getRect(), p);
        }
    }

    public Rect getRect() {
        return new Rect(x, y, x + width, y + height);
    }

    public int getY() {
        return y;
    }
}