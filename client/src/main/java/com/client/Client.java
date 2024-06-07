package com.client;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static Socket socket;
    private static final SortedMap<Integer, String> songList = new TreeMap<>();
    
    private static void fillList(String message) {
        String[] songs = message.split("\n");
        for (String song : songs) {
            int num = Integer.parseInt(song.substring(0, song.indexOf(". ") ) );
            songList.put(num, song.substring(song.indexOf(". ") + 2));
        }
        
//        File folder = new File("songs");
//        
//        if (folder.exists() && folder.isDirectory()) {
//            File[] files = folder.listFiles();
//
//            if (files != null) {
//                int index = 1;
//                for (File file : files) {
//                    if (file.isFile() && file.getName().endsWith(".mp3")) {
//                        songList.put(index++, file.getName());
//                    }
//                }
//            }
//        } else {
//            System.out.println("Папка \"songs\" не существует или не является директорией.");
//        }
    }
    
    public static class RecievingMessageThread extends Thread {
        public RecievingMessageThread() {};
        String folder = "songs/";
        
        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                while (!socket.isClosed() ) {
                    try {
                        String header = dis.readUTF(); // Чтение заголовка

                        if (null == header) {
                            System.out.println("Неизвестный заголовок: " + header);
                        } else switch (header) {
                            case "MESSAGE" -> {
                                int length = dis.readInt(); // Чтение длины сообщения
                                byte[] messageBytes = new byte[length];
                                dis.readFully(messageBytes); // Чтение сообщения
                                String message = new String(messageBytes, "UTF-8");
                                System.out.println("Сообщение от сервера:\n" + message);
                            }
                            case "FILE" -> {
                                    String fileName = dis.readUTF(); // Чтение имени файла
                                    long fileSize = dis.readLong(); // Чтение размера файла
                                    try (FileOutputStream fos = new FileOutputStream(folder + fileName)) {
                                        byte[] buffer = new byte[4096];
                                        long totalBytesRead = 0;
                                        while (totalBytesRead < fileSize) {
                                            int bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize - totalBytesRead));
                                            fos.write(buffer, 0, bytesRead);
                                            totalBytesRead += bytesRead;
                                        }
                                        System.out.println("Файл получен и сохранен как " + fileName);
                                    }
                            }
                            case "LIST" -> {
                                int length = dis.readInt(); // Чтение длины сообщения
                                byte[] messageBytes = new byte[length];
                                dis.readFully(messageBytes); // Чтение сообщения
                                String message = new String(messageBytes, "UTF-8");
                                fillList(message);
                                System.out.println("Сообщение от сервера:\n" + message);
                            }
                            default -> System.out.println("Неизвестный заголовок: " + header);
                        }
                    } catch (EOFException e) {
                        System.out.println(e.getMessage());
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    private static void sendMessage(String message) throws IOException {
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        byte[] messageBytes = message.getBytes("UTF-8");
        int length = messageBytes.length;

        dos.writeInt(length); // Отправляем длину сообщения
        dos.write(messageBytes); // Отправляем само сообщение
        dos.flush();
    }
    
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 8080;
        Scanner console = new Scanner(System.in);

        try { 
            socket = new Socket(serverAddress, port);
            System.out.println("Подключено к серверу!");
            RecievingMessageThread receive = new RecievingMessageThread();
            receive.start();
            MP3Player player = new MP3Player();
            
            while (!socket.isClosed() ) {
                // Отправка сообщения
                String message = console.nextLine();
                if (message.startsWith("play ")) {
                    String path = songList.get(Integer.valueOf(message.substring(5)));
                    player.play(path);
                } else if (message.equals("pause")) {
                    player.pause();
                } else if (message.equals("resume")){
                    player.resume();
                } else if (message.equals("stop")) {
                    player.close();
                } else if (message.startsWith("get ")) sendMessage(message);
                else if (message.startsWith("seek ") ) {
                    player.seek(Integer.parseInt(message.substring(5)));
                } else if (message.equals("close")) {
                    sendMessage(message);
                    player.close();
                    break;
                } else System.out.println("Необработанное сообщение");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
    }
}