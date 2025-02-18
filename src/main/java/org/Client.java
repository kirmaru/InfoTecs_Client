package org;

import com.jcraft.jsch.*;
import java.io.InputStream;
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
        connect(ADRESS, PORT, LOGIN, PASSWORD);


        if(connect(ADRESS, PORT, LOGIN, PASSWORD)){
            System.out.println("Введите название файла:");
            FILENAME = scanner.nextLine();
            jsonString = downloadFile(FILENAME);
            json = new Json(jsonString);

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

    private static boolean connect(String host, int port, String login, String password){
        try{
            JSch jsch = new JSch();
            session = jsch.getSession(login, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");

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
