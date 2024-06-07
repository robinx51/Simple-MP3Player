package com.client;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.TagException;

import java.io.*;
import javax.swing.*;
import java.util.logging.*;
import java.awt.event.*;

public class MP3Player {
    private final Form form;
    private AdvancedPlayer player;
    private FileInputStream fis;
    
    public boolean isPaused = true;
    private long totalFrames;
    private int pausedFrame;
    
    private final String folder = "songs/";
    private String filePath;
    private int duration;
    
    private final Timer timer;
    
    public MP3Player(Form form) {
        this.form = form;
        Logger.getLogger("org.jaudiotagger").setLevel(Level.SEVERE);
        timer = new Timer(1000, (ActionEvent e) -> {
            if (player != null && !isPaused) {
                // Получаем текущий кадр или время воспроизведения и преобразуем его в секунды
                try {
                    long currentFrame = fis.available();
                    
                    int fps = (int) (totalFrames / getDuration());
                    int currentSeconds = duration - (int) currentFrame / fps;
                    //System.out.println("Текущая секунда: " + currentSeconds);
                    form.songSlider.setValue(currentSeconds);
                    if (currentSeconds == duration) stopTimer();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
    }
    
    public void startTimer() {
        timer.start();
    }
    
    public void stopTimer() {
        timer.stop();
    }
    
    public void play(String filePath) {
        this.filePath = filePath;
        duration = getDuration();
        form.songSlider.setMaximum(duration);
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
            startTimer();
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
                stopTimer();
                fis.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void resumeSong() {
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
                startTimer();
                isPaused = false;
            } catch (IOException | JavaLayerException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void seek(int seconds) {
        int fps = (int) (totalFrames / getDuration());
        int frame = fps * seconds;
        
        if (player != null && fis != null) {
            try {
                player.close();
                stopTimer();
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
                startTimer();
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
            int durationTemp = f.getAudioHeader().getTrackLength();
            
            return durationTemp;
        } catch (CannotReadException | IOException | TagException | InvalidAudioFrameException | ReadOnlyFileException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void close() {
        pausedFrame = 0;
        stopTimer();
        
        if (player != null) {
            player.close();
        }
        try {
            if (fis != null) {
                fis.close();
                isPaused = true;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}