package com.example.flappybird;

import android.content.Context;
import android.graphics.Rect;
import java.util.List;

public class GameSystems {
    public GameSystems(Context context) {
        // TV5 load SoundPool tại đây
    }

    public void playWing() {} // Âm thanh vỗ cánh
    public void playHit() {}  // Âm thanh va chạm

    public static boolean checkCollision(Bird bird, List<PipeManager.Pipe> pipes) {
        if (bird == null || pipes == null) return false;

        // Kiểm tra va chạm với từng ống
        for (PipeManager.Pipe p : pipes) {
            if (Rect.intersects(bird.getRect(), p.getTopRect()) ||
                    Rect.intersects(bird.getRect(), p.getBottomRect())) {
                return true;
            }
        }
        // Kiểm tra chạm đất/trần
        if (bird.getY() <= 0 || bird.getY() >= Constants.SCREEN_HEIGHT) return true;

        return false;
    }
}