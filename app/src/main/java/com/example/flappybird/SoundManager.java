package com.example.flappybird;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import java.util.List;
import android.graphics.Rect;

public class SoundManager {

    private SoundPool soundPool;
    private int soundWing;
    private int soundHit;
    private int soundScore;

    private int score = 0;

    public SoundManager(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        soundWing = soundPool.load(context, R.raw.wing, 1);
        soundHit = soundPool.load(context, R.raw.hit, 1);
        soundScore = soundPool.load(context, R.raw.score, 1);
    }

    public void playWing() {
        if (soundWing != 0) {
            soundPool.play(soundWing, 1, 1, 1, 0, 1);
        }
    }

    public void playHit() {
        if (soundHit != 0) {
            soundPool.play(soundHit, 1, 1, 1, 0, 1);
        }
    }

    public void playScore() {
        if (soundScore != 0) {
            soundPool.play(soundScore, 1, 1, 1, 0, 1);
        }
    }

    public static boolean checkCollision(Bird bird, List<PipeManager.Pipe> pipes) {
        if (bird == null || pipes == null) return false;

        Rect birdRect = bird.getRect();
        for (PipeManager.Pipe pipe : pipes) {
            // Check va chạm với ống TRÊN hoặc ống DƯỚI
            if (Rect.intersects(birdRect, pipe.getTopRect()) ||
                    Rect.intersects(birdRect, pipe.getBottomRect())) {
                return true;
            }
        }

        // Check nếu chim rơi xuống đất hoặc bay quá trời
        if (bird.getY() <= 0 || bird.getY() >= Constants.SCREEN_HEIGHT) {
            return true;
        }

        return false;
    }
    
    public void addScore() {
        score++;
        playScore();
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}