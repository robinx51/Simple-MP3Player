package com.client;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.*;

import java.io.*;
import org.jaudiotagger.tag.TagException;

public class MP3Player {
    private AdvancedPlayer player;
    private FileInputStream fis;
    private boolean isPaused = true;
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
            new Thread(() -> {
                try {
                    player.play(pausedFrame, Integer.MAX_VALUE);
                } catch (JavaLayerException e) {
                    System.out.println(e.getMessage());
                }
            }).start();
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
            try {
                fis = new FileInputStream(folder + filePath);
                player = new AdvancedPlayer(fis);
                // Пропускаем байты, чтобы возобновить воспроизведение с сохраненной позиции
                fis.skip(totalFrames - pausedFrame);
                new Thread(() -> {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
                isPaused = false;
            } catch (IOException | JavaLayerException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void seek(int seconds) {
        int framesPerSecond = (int) (totalFrames / getDuration());
        int frame = framesPerSecond * seconds;
         
        if (player != null && fis != null) {
            try {
                player.close();
                fis.close();
                fis = new FileInputStream(folder + filePath);
                player = new AdvancedPlayer(fis);
                
                fis.skip(frame);
                new Thread(() -> {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
                isPaused = false;
            } catch (JavaLayerException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public int getDuration() {
        try {
            File audioFile = new File(folder + filePath);
            AudioFile f = AudioFileIO.read(audioFile);
            int duration = f.getAudioHeader().getTrackLength();
            
            return duration;
        } catch (CannotReadException | IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException e) {
            System.out.println(e.getMessage());
        }
        return 0;
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