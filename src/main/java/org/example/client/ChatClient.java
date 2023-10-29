package org.example.client;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatClient {
    private static final Map<String, String> users = new HashMap<>();
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println("Сервер: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();
            BufferedReader consoleReader = new BufferedReader(
                    new InputStreamReader(System.in));
            while (true) {
                System.out.println("Порт: 12345");
                System.out.println("1. Зарегистрироваться");
                System.out.println("2. Войти");
                System.out.print("Выбор категорий: ");
                String menu = consoleReader.readLine();
                switch (menu) {
                    case "1" -> Handler.register();
                    case "2" -> Handler.start();
                }
                System.out.println("Добро пожаловать " + Handler.userName + " (чтобы вернутся назад, нажмите '0')");
                System.out.println("Введите сообщение, чтобы отправить: ");
                boolean chatState = true;
                while (chatState) {
                    String userInput = consoleReader.readLine();
                    if (!"0".equals(userInput)) {
                        writer.println(userInput);
                    } else {
                        chatState = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static class Handler {
        public static final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        private static String userName;
        private static String password;

        public static void register() throws IOException {
            System.out.println("Введите имя для регистрации");
            userName = consoleReader.readLine();
            if (!users.containsKey(userName)) {
                System.out.println("Введите пароль для регистрации");
                password = consoleReader.readLine();
                users.put(userName, password);
            } else {
                System.out.println("Пользователь с таким именем существует!");
                start();
            }
        }
        public static void start() throws IOException {
            System.out.println("Введите имя");
            userName = consoleReader.readLine();
            if (users.containsKey(userName)) {
                System.out.println("Введите пароль");
                password = consoleReader.readLine();
                if (!users.get(userName).contains(password)) {
                    System.out.println("Введен не корректный пароль");
                    start();
                }
            } else {
                System.out.println("Данные отсуствуют");
                register();
            }
        }
    }
}