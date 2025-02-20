# Клиент для работы с SFTP-сервером
Простой клиент для просмотра и редактирования файлов формата .json на SFTP-сервере.
## Используемые ресурсы
- Jsch - для работы с SFTP.
## Инструкция по сборке клиента
Сборка будет проводится при помощи Maven.
Для сборки клиента необходимо:
- Установить Java Development Kit (JDK) версии 8 и выше.
- Установить Maven для сборки проекта.
- Клонировать данный репозиторий.

Далее, в корневой папке проекта:
- Очистите старые сборки
```
mvn clean
```
- Соберите проект с зависимостями
```
mvn package
```
Собранный проект будет находится в папке `target/` . Исполняемый JAR файл будет называться `InfoTecs_Client-1.0-SNAPSHOT.jar`.

## Инструкция по использованию клиента

Для запуска клиента в его корневой папке введите команду

`java -jar target/InfoTecs_Client-1.0-SNAPSHOT.jar`

Клиент должен начать свою работу. Далее, следуйте запросам в командной строке:
```
Введите адрес SFTP-сервера: <Адрес вашего сервера>
Введите порт SFTP-сервера: <Порт вашего сервера>
Введите логин SFTP-сервера: <Логин вашего сервера>
Введите пароль SFTP-сервера: <Пароль вашего сервера>
```
При успешном установлении сессии и открытия канала передачи данных нужно будет ввести путь к вашему файлу на сервере:

`Введите название файла: <путь/к/вашему/файлу>`

Далее, файл будет считан клиентом и станет доступным для его просмотра/редактирования. Откроется меню с командами:
```
===== МЕНЮ КЛИЕНТА =====
1. Показать список пар 'домен – IP'
2. Найти IP по домену
3. Найти домен по IP
4. Добавить новую пару
5. Удалить пару
6. Завершить работу
Выберите действие: 
```
Пользователю будет необходимо ввести желаемую команду. Сохранение файла на сервер происходит при завершении работы программы. 
## Важно
В программе изначально выключено StrictHostKeyChecking (проверка ключа хоста при установлении SSH-соединения, если ключ хоста сервера не совпадает с сохранённым в локальной базе данных ключом или сервер не был ранее в списке известных, соединение будет установлено без предупреждения). Это сделано только для целей тестирования работы клиента - выключенная проверка снижает безопасность, так как делает систему уязвимой для атак "man-in-the-middle". 

Изменить данный параметр конфигурации клиента можно в классе Client.java https://github.com/kirmaru/InfoTecs_Client/blob/691777305304ed052a07fb7e7a94bfa7496b9648/src/main/java/org/Client.java#L124
