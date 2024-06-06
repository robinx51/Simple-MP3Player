package com.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    private static SortedMap<Integer, String> fillList() {
        SortedMap<Integer, String> songList = new TreeMap<>();
        File folder = new File("songs");
        
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                int index = 1;
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".mp3")) {
                        songList.put(index++, file.getName());
                    }
                }
            }
        } else {
            System.out.println("Указанная папка не существует или не является директорией.");
        }
        
        return songList;
    }
    
    public static void main(String[] args) {
        int port = 8080; // Порт, который будет использовать сервер
        
        SortedMap<Integer, String> songList = fillList();
        System.out.println("Найдено песен: " + songList.size());
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("\nСервер запущен и ожидает подключения клиента...");
            while(true) {
                // Ожидание подключения клиента
                Socket socket = serverSocket.accept();
                System.out.println("Новый клиент, порт: " + socket.getPort() );

                ClientThread client = new ClientThread(socket, songList);
                client.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}