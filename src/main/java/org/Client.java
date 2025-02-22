package org;

import com.jcraft.jsch.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class Client {
    private static String ADRESS;
    private static int PORT;
    private static String LOGIN;
    private static String PASSWORD;
    private static String FILENAME;
    private static ChannelSftp channel;
    private static Session session;
    private static Json json;
    private static String jsonString;

    public Client(String address, int port, String login, String password, String filename) {
        ADRESS = address;
        PORT = port;
        LOGIN = login;
        PASSWORD = password;
        FILENAME = filename;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите адрес SFTP-сервера: ");
        ADRESS = scanner.nextLine();
        System.out.print("Введите порт SFTP-сервера: ");
        PORT = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите логин SFTP-сервера: ");
        LOGIN = scanner.nextLine();
        System.out.print("Введите пароль SFTP-сервера: ");
        PASSWORD = scanner.nextLine();

        if(connect(ADRESS, PORT, LOGIN, PASSWORD)){
            while(true) {
                System.out.println("Введите название файла:");
                FILENAME = scanner.nextLine();
                jsonString = downloadFile(FILENAME);
                json = new Json(jsonString);

                if (jsonString.equals("[]") || json.ADRESSES.isEmpty()) {
                    System.out.println("Попробуйте снова.");
                } else {
                    System.out.println("Файл успешно загружен.");
                    break;
                }
            }

            while(true){
                System.out.println("\n===== МЕНЮ КЛИЕНТА =====");
                System.out.println("1. Показать список пар 'домен – IP'");
                System.out.println("2. Найти IP по домену");
                System.out.println("3. Найти домен по IP");
                System.out.println("4. Добавить новую пару");
                System.out.println("5. Удалить пару");
                System.out.println("6. Завершить работу");
                System.out.print("Выберите действие: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice){
                    case 1:
                        json.showAll();
                        break;
                    case 2:
                        System.out.println("Введите domain:");
                        json.findIp(scanner.nextLine());
                        break;
                    case 3:
                        System.out.println("Введите ip:");
                        json.findDomain(scanner.nextLine());
                        break;
                    case 4:
                        System.out.println("Введите новый domain:");
                        String domain = scanner.nextLine();
                        System.out.println("Введите новый ip:");
                        String ip = scanner.nextLine();
                        json.addObj(domain, ip);
                        break;
                    case 5:
                        System.out.println("Введите данные для удаления:");
                        json.removeObj(scanner.nextLine());
                        break;
                    case 6:
                        System.out.println("Программа завершена");
                        saveFile(FILENAME, json.toJsonString());
                        System.exit(0);
                }
            }
        }
    }

    private static String downloadFile(String FILENAME){
        try{
            InputStream inputStream = channel.get(FILENAME);
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "[]";
        }catch (SftpException e) {
            System.err.println("Ошибка загрузки файла: " + e.getMessage());
            return "[]";
        }
    }

    private static void saveFile(String FILENAME, String data){
        try{
            InputStream inputStream = new ByteArrayInputStream(data.getBytes());
            channel.put(inputStream, FILENAME);
            System.out.println("Файл успешно сохранен на сервере.");
        }catch (SftpException e){
            System.err.println("Ошибка сохранения файла: " + e.getMessage());
        }
    }

    private static boolean connect(String host, int port, String login, String password){
        try{
            JSch jsch = new JSch();
            session = jsch.getSession(login, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); //Предупреждение: не использовать отключенную проверку ключа сервера, только для тестирования.

            session.connect();
            System.out.println("Сессия установлена");

            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            System.out.println("Канал открыт");
            return true;
        }
        catch (JSchException e) {
            System.err.println("Ошибка при подключении: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
