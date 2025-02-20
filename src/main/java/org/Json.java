package org;

import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Json {
    String jsonString;
    List<Map<String, String>> ADRESSES;

    public Json() {
        this.jsonString = null;
        this.ADRESSES = new ArrayList<>();
    }

    public Json(String jsonString) {
        this.jsonString = jsonString;
        this.ADRESSES = parse(jsonString);
    }
    public static List<Map<String, String>> parse(String json){
        int start = json.indexOf("[");
        int end = json.lastIndexOf("]");
        if (start == -1 || end == -1) {
            System.out.println("Ошибка: JSON не содержит массив.");
            return new ArrayList<>();
        }
        String peopleArray = json.substring(start + 1, end);

        String[] objects = peopleArray.split("\\},\\s*\\{");

        List<Map<String, String>> peopleList = new ArrayList<>();
        Set<String> uniqueValues = new HashSet<>();

        for (String obj : objects) {
            obj = obj.replace("{", "").replace("}", "");
            String[] pairs = obj.split(",");

            Map<String, String> person = new HashMap<>();
            Set<String> currentValues = new HashSet<>();
            boolean isDuplicate = false;

            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length != 2) continue;

                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");

                person.put(key, value);

                if (uniqueValues.contains(value)) {
                    isDuplicate = true;
                }
                currentValues.add(value);
            }

            if (!isDuplicate) {
                peopleList.add(person);
                uniqueValues.addAll(currentValues);
            }
        }
        return peopleList;
    }

    public boolean validate(String string) {
        try {
            InetAddress ip = InetAddress.getByName(string);
            if (ip instanceof java.net.Inet4Address) {
                return true;
            }
        } catch (UnknownHostException e) {
            System.out.print(" Предупреждение: некорректный IP-адрес.");
        }
        return false;
    }

    public void removeObj(String value) {
        boolean removed = ADRESSES.removeIf(person -> person.get("domain").equals(value) || person.get("ip").equals(value));
        if (removed) {
            System.out.println("Запись с '" + value + "' была удалена.");
        } else {
            System.out.println("Запись с '" + value + "' не найдена.");
        }
    }

    public void removeObjByDomain(String domain) {
        boolean removed = ADRESSES.removeIf(person -> person.get("domain").equals(domain));

        if (removed) {
            System.out.println("Удалена запись с доменом: " + domain);
        } else {
            System.out.println("Запись с доменом '" + domain + "' не найдена.");
        }
    }

    public void removeObjByIp(String ip) {
        boolean removed = ADRESSES.removeIf(person -> person.get("ip").equals(ip));

        if (removed) {
            System.out.println("Удалена запись с IP-адресом: " + ip);
        } else {
            System.out.println("Запись с IP-адресом '" + ip + "' не найдена.");
        }
    }

    public void showAll() {
        ADRESSES.sort(Comparator.comparing(o -> o.get("domain")));
        for (Map<String, String> person : ADRESSES) {
            System.out.print("\n domain: " + person.get("domain") + ", ip: " + person.get("ip"));
            validate(person.get("ip"));
        }
    }

    public String findDomain(String ip) {
        for (Map<String, String> person : ADRESSES) {
            if (person.get("ip").equals(ip)) {
                return person.get("domain");
            }
        }
        return "Домен не найден.";
    }

    public String findIp(String domain) {
        for (Map<String, String> person : ADRESSES) {
            if (person.get("domain").equals(domain)) {
                return person.get("ip");
            }
        }
        return "IP-адрес не найден.";
    }

    public void addObj(String domain, String ip) {
        if (!validate(ip)) {
            System.out.println("Добавление невозможно: некорректный IP-адрес.");
            return;
        }

        for (Map<String, String> person : ADRESSES) {
            if (person.get("domain").equals(domain) || person.get("ip").equals(ip)) {
                System.out.println("Добавление невозможно: домен или IP уже существуют.");
                return;
            }
        }

        Map<String, String> obj = new HashMap<>();
        obj.put("domain", domain);
        obj.put("ip", ip);
        ADRESSES.add(obj);
        System.out.println("Добавлено: " + domain + " -> " + ip);
    }

    public String toJsonString() {
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < ADRESSES.size(); i++) {
            Map<String, String> person = ADRESSES.get(i);
            jsonBuilder.append("{\"domain\":\"").append(person.get("domain"))
                    .append("\", \"ip\":\"").append(person.get("ip"))
                    .append("\"}");
            if (i < ADRESSES.size() - 1) {
                jsonBuilder.append(", ");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}

