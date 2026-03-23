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
    private int screenX, screenY;
    private int pipeSpacing = 600; // Khoảng cách giữa 2 cột ống liên tiếp
    private int score = 0;

    public PipeManager(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;

        // BƯỚC TỐI ƯU 1: Khởi tạo sẵn 3 ống ngay từ đầu, không dùng timer nữa
        for (int i = 0; i < 3; i++) {
            // Xếp 3 ống nối đuôi nhau ra phía ngoài màn hình bên phải
            pipes.add(new Pipe(screenX + i * pipeSpacing));
        }
    }

    public void update() {
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.update();

            // BƯỚC TỐI ƯU 2: TÁI CHẾ ỐNG (Thay vì xóa đi tạo lại)
            if (pipe.x + pipe.width < 0) {
                // Tìm xem hiện tại ống nào đang nằm xa nhất về bên phải
                int max_x = 0;
                for (Pipe p : pipes) {
                    if (p.x > max_x) {
                        max_x = p.x;
                    }
                }

                // Di chuyển cái ống vừa khuất màn hình ra sau lưng cái ống xa nhất đó
                pipe.x = max_x + pipeSpacing;
                pipe.randomizeHeight(); // Sinh lại khe hở mới
                pipe.isPassed = false;  // Reset cờ tính điểm cho lần lặp sau
            }

            // HỖ TRỢ TV5: Logic tính điểm đơn giản
            // Giả sử chim bay ở khoảng 1/3 màn hình, khi ống vượt qua tọa độ này thì cộng điểm
            if (!pipe.isPassed && pipe.x + pipe.width < screenX / 3) {
                score++;
                pipe.isPassed = true;
            }
        }
    }

    // --- HÀM DRAW NÀY ĐỂ BẠN CHẠY TEST TRƯỚC (TV2 SẼ SỬA LẠI ĐỂ VẼ ẢNH SAU) ---
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        for (Pipe pipe : pipes) {
            pipe.draw(canvas, p);
        }
    }

    public List<Pipe> getPipes() { return pipes; }
    public int getScore() { return score; }

    // ==========================================
    // --- LỚP PIPE NỘI BỘ (Inner Class) ---
    // ==========================================
    public class Pipe {
        public int x, topY, width = 180, gap = 450;
        public boolean isPassed = false; // Cờ đánh dấu chim đã bay qua chưa (Cho TV5)

        public Pipe(int startX) {
            this.x = startX;
            randomizeHeight();
        }

        // Tách hàm random ra để dùng lại khi tái chế
        public void randomizeHeight() {
            int minHeight = 200; // Ống không được ngắn hơn 200px
            int maxHeight = screenY - minHeight - gap;

            if (maxHeight > minHeight) {
                this.topY = new Random().nextInt(maxHeight - minHeight) + minHeight;
            } else {
                this.topY = minHeight; // Tránh lỗi crash nếu màn hình quá nhỏ
            }
        }

        public void update() {
            x -= 12; // Tốc độ di chuyển
        }

        public void draw(Canvas canvas, Paint p) {
            p.setColor(Color.GREEN);
            canvas.drawRect(getTopRect(), p);
            canvas.drawRect(getBottomRect(), p);
        }

        public Rect getTopRect() { return new Rect(x, 0, x + width, topY); }
        public Rect getBottomRect() { return new Rect(x, topY + gap, x + width, screenY); }
    }
}