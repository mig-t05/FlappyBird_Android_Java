package com.example.flappybird;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PipeManager {
    private List<Pipe> pipes = new ArrayList<>();
    private int screenX, screenY, score = 0;
    private long lastPipeTime;

    public PipeManager(int screenX, int screenY) {
        this.screenX = screenX; this.screenY = screenY;
        spawnPipe();
    }

    private void spawnPipe() {
        pipes.add(new Pipe(screenX));
        lastPipeTime = System.currentTimeMillis();
    }

    public void update() {
        if (System.currentTimeMillis() - lastPipeTime > 2500) spawnPipe();
        for (int i = pipes.size() - 1; i >= 0; i--) {
            pipes.get(i).update();
            if (pipes.get(i).x + 200 < 0) { pipes.remove(i); score++; }
        }
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        for (Pipe pipe : pipes) pipe.draw(canvas, p);
    }

    public List<Pipe> getPipes() { return pipes; }
    public int getScore() { return score; }

    // --- Lớp Pipe nội bộ ---
    public class Pipe {
        public int x, topY, width = 200, gap = 450;
        public Pipe(int startX) {
            this.x = startX;
            this.topY = new Random().nextInt(screenY / 2) + 200;
        }
        public void update() { x -= 12; }
        public void draw(Canvas canvas, Paint p) {
            p.setColor(Color.GREEN);
            canvas.drawRect(getTopRect(), p);
            canvas.drawRect(getBottomRect(), p);
        }
        public Rect getTopRect() { return new Rect(x, 0, x + width, topY); }
        public Rect getBottomRect() { return new Rect(x, topY + gap, x + width, screenY); }
    }
}