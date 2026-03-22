package com.example.flappybird;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    // --- TV1: QUẢN LÝ HỆ THỐNG ---
    private Thread thread;
    private boolean isPlaying;
    private SurfaceHolder holder;
    private int screenX, screenY;
    public boolean isGameOver = false;
    private Paint scorePaint;

    // --- CÁC THÀNH PHẦN ĐỘC LẬP ---
    private BitmapBank bitmapBank;   // TV2 phụ trách
    private Bird bird;              // TV3 phụ trách
    private PipeManager pipeManager;// TV4 phụ trách
    private GameSystems systems;     // TV5 phụ trách

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.holder = getHolder();
        this.screenX = screenX;
        this.screenY = screenY;

        // KHỞI TẠO KHUNG (Dù class rỗng vẫn chạy được)
        bitmapBank = new BitmapBank(context);
        systems = new GameSystems(context);
        bird = new Bird(screenX, screenY);
        pipeManager = new PipeManager(screenX, screenY);

        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(100);
    }

    @Override
    public void run() {
        while (isPlaying) {
            update(); // TV1 điều phối logic
            draw();   // TV1 điều phối hiển thị
            control();
        }
    }

    private void update() {
        if (isGameOver) return;

        // --- MỖI THÀNH VIÊN TỰ TEST TRÊN NHÁNH CỦA MÌNH ---
        if (bird != null) bird.update();               // TV3 test chim rơi
        if (pipeManager != null) pipeManager.update(); // TV4 test ống chạy

        // TV5 test va chạm
        if (GameSystems.checkCollision(bird, pipeManager.getPipes())) {
            isGameOver = true;
            if (systems != null) systems.playHit();
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();

            // 1. TV2 TEST BACKGROUND
            if (BitmapBank.getBackground() != null) {
                canvas.drawBitmap(BitmapBank.getBackground(), 0, 0, null);
            } else {
                canvas.drawColor(Color.rgb(113, 197, 207)); // Màu nền tạm
            }

            // 2. TV4 TEST ỐNG
            if (pipeManager != null) pipeManager.draw(canvas);

            // 3. TV3 TEST CHIM
            if (bird != null) bird.draw(canvas);

            // 4. TV1 & TV2 TEST UI
            canvas.drawText("Score: " + pipeManager.getScore(), 50, 150, scorePaint);
            if (isGameOver) {
                canvas.drawText("GAME OVER", screenX/4f, screenY/2f, scorePaint);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isGameOver) {
                restartGame();
            } else {
                // TV3 TEST CẢM ỨNG NHẢY
                if (bird != null) bird.jump();
                // TV5 TEST ÂM THANH
                if (systems != null) systems.playWing();
            }
        }
        return true;
    }

    private void restartGame() {
        bird = new Bird(screenX, screenY);
        pipeManager = new PipeManager(screenX, screenY);
        isGameOver = false;
    }

    private void control() {
        try { Thread.sleep(17); } catch (InterruptedException e) {}
    }

    public void resume() { isPlaying = true; thread = new Thread(this); thread.start(); }
    public void pause() { isPlaying = false; try { thread.join(); } catch (Exception e) {} }
}