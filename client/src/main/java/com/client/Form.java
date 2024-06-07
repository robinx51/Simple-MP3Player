package com.client;

import java.awt.Image;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

public class Form extends javax.swing.JFrame {
    private final DefaultListModel model;
    private final Client client;
    private final MP3Player player;
    
    private int currentSong;
    private ImageIcon playIcon;
    private ImageIcon pauseIcon;
    
    public Form() {
        initComponents();
        model = new DefaultListModel();
        client = new Client(this);
        player = new MP3Player(this);
        
        playIcon = new ImageIcon("icons/play.png");
        pauseIcon = new ImageIcon("icons/pause.png");
        ImageIcon prevIcon = new ImageIcon("icons/prev.png");
        ImageIcon nextIcon = new ImageIcon("icons/next.png");

        // Изменение размеров иконок до 32x32 пикселей
        playIcon = resizeIcon(playIcon, 32, 32);
        pauseIcon = resizeIcon(pauseIcon, 32, 32);
        prevIcon = resizeIcon(prevIcon, 32, 32);
        nextIcon = resizeIcon(nextIcon, 32, 32);
        
        playButton.setIcon(playIcon);
        prevButton.setIcon(prevIcon);
        nextButton.setIcon(nextIcon);
    }
    
    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
    
    public void setList() {
        songJList.setModel(model);
        for (int i = 0; i < client.songList.size(); i++) {
            String song = client.songList.get(i);
            model.addElement(song);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        songJList = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        prevButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        songSlider = new javax.swing.JSlider();
        jPanel3 = new javax.swing.JPanel();
        songLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MP3-плеер");
        setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        setMaximumSize(new java.awt.Dimension(584, 332));
        setName("frame"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        songJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        songJList.setToolTipText("Выберите композицию");
        songJList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        songJList.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
        songJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                songJListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(songJList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        songSlider.setValue(0);
        songSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                songSliderMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(songSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(prevButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(songSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(prevButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(playButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nextButton, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        songLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        songLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        songLabel.setText("Name of song");
        songLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        songLabel.setMaximumSize(new java.awt.Dimension(362, 55));
        songLabel.setMinimumSize(new java.awt.Dimension(362, 55));
        songLabel.setPreferredSize(new java.awt.Dimension(362, 55));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(songLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(songLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void songJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_songJListMouseClicked
        player.close();
        playButton.setIcon(playIcon);
        songSlider.setValue(0);
        currentSong = songJList.getSelectedIndex();
    }//GEN-LAST:event_songJListMouseClicked

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if (client.songList.containsKey(currentSong + 1)) {
            songSlider.setValue(0);
            String path = client.songList.get(++currentSong);
            player.play(path);
            songJList.setSelectedIndex(currentSong);
        } else {
            songSlider.setValue(0);
            playButton.setIcon(playIcon);
        }
    }//GEN-LAST:event_nextButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        if (player.isPaused) {
            if (songSlider.getValue() > 0) player.resumeSong();
            else {
                String path = client.songList.get(currentSong);
                songLabel.setText(path);
                player.play(path);
            }
            playButton.setIcon(pauseIcon);
        } else {
            player.pause();
            playButton.setIcon(playIcon);
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        if (client.songList.containsKey(currentSong - 1)) {
            songSlider.setValue(0);
            String path = client.songList.get(--currentSong);
            player.play(path);
            songJList.setSelectedIndex(currentSong);
        }
    }//GEN-LAST:event_prevButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            client.sendMessage("close");
            player.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
    }//GEN-LAST:event_formWindowClosing

    private void songSliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_songSliderMouseReleased
        int value = songSlider.getValue();
        player.seek(value);
    }//GEN-LAST:event_songSliderMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JList<String> songJList;
    private javax.swing.JLabel songLabel;
    public javax.swing.JSlider songSlider;
    // End of variables declaration//GEN-END:variables
}
