package com.example.flappybird;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Thiết lập full màn hình (Bỏ thanh trạng thái nếu muốn)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 2. Lấy kích thước màn hình thiết bị
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        // 3. Lưu kích thước vào Constants để các thành viên khác có thể truy cập dễ dàng
        Constants.SCREEN_WIDTH = size.x;
        Constants.SCREEN_HEIGHT = size.y;

        // 4. Khởi tạo GameView - Trung tâm điều phối của trò chơi
        gameView = new GameView(this, size.x, size.y);

        // 5. Nạp trực tiếp GameView vào màn hình thay vì dùng setContentView(R.layout...)
        setContentView(gameView);
    }

    // --- QUẢN LÝ VÒNG ĐỜI (Cực kỳ quan trọng để tránh treo máy) ---

    @Override
    protected void onPause() {
        super.onPause();
        // Dừng luồng (thread) game khi thoát ứng dụng hoặc có cuộc gọi đến
        if (gameView != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Chạy lại luồng game khi người dùng quay lại ứng dụng
        if (gameView != null) {
            gameView.resume();
        }
    }
}