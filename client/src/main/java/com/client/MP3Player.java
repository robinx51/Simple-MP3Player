package com.client;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import java.io.*;

public class MP3Player {
    private AdvancedPlayer player;
    private FileInputStream fis;
    private Thread playThread;
    private volatile boolean isPaused = true;
    private long totalFrames;
    private int pausedFrame;
    private final String folder = "songs/";
    private String filePath;

    public void play(String filePath) {
        this.filePath = filePath;
        close();
        try {
            File file = new File(folder + filePath);
            totalFrames = file.length();
            
            fis = new FileInputStream(file);
            player = new AdvancedPlayer(fis);
            isPaused = false;
            playThread = new Thread(() -> {
                try {
                    player.play(pausedFrame, Integer.MAX_VALUE);
                } catch (JavaLayerException e) {
                    System.out.println(e.getMessage());
                }
            });
            playThread.start();
        } catch (JavaLayerException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void pause() {
        if (!isPaused) {
            isPaused = true;
            try {
                pausedFrame = fis.available();
                player.close();
                fis.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void resume() {
        if (isPaused) {
            isPaused = false;
            try {
                fis = new FileInputStream(folder + filePath);
                player = new AdvancedPlayer(fis);
                // Пропускаем байты, чтобы возобновить воспроизведение с сохраненной позиции
                fis.skip(totalFrames - pausedFrame);
                playThread = new Thread(() -> {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                        System.out.println(e.getMessage());
                    }
                });
                playThread.start();
            } catch (IOException | JavaLayerException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void close() {
        pausedFrame = 0;
        if (player != null) {
            player.close();
        }
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}