package com.server;

import java.io.*;
import java.net.*;
import java.util.SortedMap;

public class ClientThread extends Thread {
    Socket socket;
    SortedMap<Integer, String> songList;
    Integer port;
    String directory = "songs/";
    
    public ClientThread(Socket socket, SortedMap<Integer, String> songList) {
        this.socket = socket;
        this.songList = songList;
        port = socket.getPort();
    }
    
    private boolean HandleMessage(String message) throws IOException {
        System.out.println("Сообщение от клиента(" + port + "): " + message);
        if (message.startsWith("get ")) {
            message = message.substring(4);
            int number = Integer.parseInt(message);
            sendFile(number);
            System.out.println("Файл '" + message + "' отправлен клиенту " + port);
            return false;
        } else if ("close".equals(message)) {
            socket.close();
            System.out.println("Cоединение(" + port + ") закрыто.");
            return true;
        } else {
            System.out.println("Необработанное сообщение от (" + port + "): " + message);
            return false;
        }
    }   
    
    private String receiveMessage() throws IOException {
        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);

        int length = dis.readInt(); // Читаем длину сообщения
        byte[] messageBytes = new byte[length];

        dis.readFully(messageBytes); // Читаем само сообщение

        return new String(messageBytes, "UTF-8");
    }
    
    private void sendFile(Integer number) throws IOException {
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        File file = new File(directory + songList.get(number));

        dos.writeUTF("FILE"); // Заголовок для файла
        dos.writeUTF(file.getName()); // Имя файла
        dos.writeLong(file.length()); // Размер файла

        byte[] buffer = new byte[4096];
        int bytesRead;

        try (FileInputStream fis = new FileInputStream(file)) {
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
        }

        dos.flush();
    }
    
    private void sendMessage(String message, String title) {
        try {
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            dos.writeUTF(title); // Заголовок для сообщения
            byte[] messageBytes = message.getBytes("UTF-8");
            int length = messageBytes.length;

            dos.writeInt(length); // Отправляем длину сообщения
            dos.write(messageBytes); // Отправляем само сообщение
            dos.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String createSongList() {
        StringBuilder message = new StringBuilder();
        songList.forEach((key, value) -> message.append(value).append("\n"));
        return message.toString();
    }
    
    @Override
    public void run() {
        sendMessage(createSongList(), "LIST" );
        while (true) {
            try{
                // Получение сообщения
                String message = receiveMessage();
                if (HandleMessage(message)) break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}
