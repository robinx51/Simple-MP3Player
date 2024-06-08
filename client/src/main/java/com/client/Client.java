package com.client;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private Socket socket;
    public final SortedMap<Integer, String> songList = new TreeMap<>();
    public final LinkedList<String> fileList = new LinkedList<>();
    private final Form form;
    
    public Client(Form form) {
        this.form = form;
        String serverAddress = "localhost";
        int port = 8080;

        try { 
            socket = new Socket(serverAddress, port);
            System.out.println("Подключено к серверу!");
            RecievingMessageThread receive = new RecievingMessageThread();
            receive.start();
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
    }
    
    private void fillLists(String message) {
        String[] songs = message.split("\n");
        int num = 0;
        for (String song : songs) {
            songList.put(num++, song);
        }
        form.setList();
        
        File folder = new File("songs");
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".mp3")) {
                        fileList.add(file.getName());
                    }
                }
            }
        } else {
            System.out.println("Папка \"songs\" не существует или не является директорией.");
        } 
    }
    
    public boolean checkList(String name) {
        return fileList.contains(name);
    }
    
    public class RecievingMessageThread extends Thread {
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
                                        fileList.add(fileName);
                                        form.checkSong(fileName);
                                    }
                            }
                            case "LIST" -> {
                                int length = dis.readInt(); // Чтение длины сообщения
                                byte[] messageBytes = new byte[length];
                                dis.readFully(messageBytes); // Чтение сообщения
                                String message = new String(messageBytes, "UTF-8");
                                fillLists(message);
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
    
    public void sendMessage(String message) {
        try{ 
            if (socket != null) {
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                byte[] messageBytes = message.getBytes("UTF-8");
                int length = messageBytes.length;

                dos.writeInt(length); // Отправляем длину сообщения
                dos.write(messageBytes); // Отправляем само сообщение
                dos.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}