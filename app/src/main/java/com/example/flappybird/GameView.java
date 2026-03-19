package com.example.flappybird;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    // --- QUẢN LÝ LUỒNG (THREAD) & TRẠNG THÁI ---
    private Thread thread;
    private boolean isPlaying;
    private SurfaceHolder holder;
    private int screenX, screenY;

    // Trạng thái Game
    public boolean isGameOver = false;
    private Paint textPaint; // Để TV2 vẽ chữ Score/GameOver

    // --- ĐỐI TƯỢNG TỪ CÁC THÀNH VIÊN KHÁC ---
    private Bird bird;              // TV3 phụ trách class này
    private List<Pipe> pipes;       // TV4 phụ trách class này
    private SoundManager soundManager; // TV5 phụ trách class này

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.holder = getHolder();
        this.screenX = screenX;
        this.screenY = screenY;

        // 1. Khởi tạo các đối tượng (Khi TV3, TV4, TV5 đã xong class của họ)
        // bird = new Bird(getResources(), screenX, screenY);
        // pipes = new ArrayList<>();
        // soundManager = new SoundManager(context);

        // Khởi tạo Paint cho UI (TV2 có thể chỉnh sửa thêm)
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(100);
    }

    @Override
    public void run() {
        while (isPlaying) {
            if (!isGameOver) {
                update(); // Bước 1: Tính toán vị trí (Logic)
            }
            draw();       // Bước 2: Hiển thị lên màn hình (Graphics)
            sleep();      // Bước 3: Kiểm soát tốc độ 60FPS
        }
    }

    // --- NHIỆM VỤ CỦA TV1: ĐIỀU PHỐI LOGIC ---
    private void update() {
        // Gọi update của Bird (TV3 làm)
        if (bird != null) {
            bird.update();
        }

        // Gọi update của Pipes (TV4 làm)
        // Ví dụ: logic sinh ống mới và di chuyển ống

        // Kiểm tra va chạm (TV5 làm hoặc TV1 điều phối)
        // if (Collision.check(bird, pipes)) { isGameOver = true; }
    }

    // --- NHIỆM VỤ CỦA TV2: HIỂN THỊ (RENDER) ---
    private void draw() {

    }

    // --- NHIỆM VỤ CỦA TV1: ĐIỀU KHIỂN ---
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isGameOver) {
                // Logic Reset game (TV1 làm)
                restartGame();
            } else {
                // Báo cho Bird nhảy lên (TV3 viết hàm jump())
                if (bird != null) {
                    bird.jump();
                    // soundManager.playJumpSound(); (TV5 làm)
                }
            }
        }
        return true;
    }

    private void restartGame() {
        // Reset tọa độ chim, xóa danh sách ống...
        isGameOver = false;
    }

    private void sleep() {
        try {
            // Nghỉ 17ms để đạt ~60 khung hình/giây
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Quản lý vòng đời (TV1 đảm bảo game không chạy ngầm gây tốn pin)
    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}