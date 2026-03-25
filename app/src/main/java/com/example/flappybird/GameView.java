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
    private boolean isStart = false;
    private int gameState = Constants.STATE_MENU;
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
        if (isStart && !isGameOver) {

            // --- MỖI THÀNH VIÊN TỰ TEST TRÊN NHÁNH CỦA MÌNH ---
            if (bird != null) bird.update();               // TV3 test chim rơi
            if (pipeManager != null) pipeManager.update(); // TV4 test ống chạy

            // TV5 test va chạm
            if (GameSystems.checkCollision(bird, pipeManager.getPipes())) {
                isGameOver = true;
                if (systems != null) systems.playHit();
            }
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
            if (!isStart) {
                // NẾU CHƯA BẮT ĐẦU: Vẽ Menu (isStart là biến boolean bạn tự đặt)
                drawStartMenu(canvas);

            }else {
                // 2. TV4 TEST ỐNG
                if (pipeManager != null) pipeManager.draw(canvas);

                // 3. TV3 TEST CHIM
                if (bird != null) bird.draw(canvas);

                // 4. TV1 & TV2 TEST UI
                canvas.drawText("Score: " + pipeManager.getScore(), 50, 150, scorePaint);
                if (isGameOver) {
                    canvas.drawText("GAME OVER", screenX / 4f, screenY / 2f, scorePaint);
                }
            }


            holder.unlockCanvasAndPost(canvas);
        }
    }
    private void drawStartMenu(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(100);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("FLAPPY BIRD", Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 3, p);

        p.setTextSize(60);
        canvas.drawText("CHẠM ĐỂ BẮT ĐẦU", Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2, p);
    }

    private void drawGameOverMenu(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(120);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("GAME OVER", Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 3, p);

        // Vẽ nút chơi lại đơn giản
        p.setColor(Color.WHITE);
        p.setTextSize(70);
        canvas.drawText("BẤM ĐỂ CHƠI LẠI", Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT / 2, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isStart) {
                isStart = true; // Menu sẽ biến mất ở lần vẽ tiếp theo
            } else if (isGameOver) {
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