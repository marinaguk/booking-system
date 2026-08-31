# BookingSystem

REST API для бронирования жилья: арендодатель размещает объект, пользователь бронирует. 

Написан без использования фреймворков, с помощью стандартного HttpServer, чтобы понять как это все работает изнутри.
Архитектура трёхслойная: репозитории (БД), сервисы (бизнес-логика), контроллеры (HTTP).


### Стек
Java 17\
Maven\
PostgreSQL 18\
Gson 2.11.0\
Hibernate 6.4.4.Final\
JPA API 3.1.0\
SLF4J 2.0.13\
Logback 1.5.6\
Jbcrypt 0.4

### Запуск
1. Создать БД: CREATE DATABASE "BookingDatabase";
2. Скопировать src/main/resources/META-INF/persistence.xml.example
   в persistence.xml, подставить свои url, user, password
3. mvn clean compile
4. Открыть проект в IDE и запустить класс bookingApp.Main
5. Сервер стартует на http://localhost:8080

### Аутентификация 
POST /user/login возвращает sessionId в теле. Дальше его надо передавать в заголовке Session-Id.\
Эндпоинты, помеченные ниже как auth, без этого заголовка отвечают 401.

### Эндпоинты
Все ответы — application/json.

| Метод | Путь | Параметры и тело | Auth | Статусы |
|---|---|---|---|---|
| POST | /booking | {"propertyId": 25, "startDate": "2027-05-20", "endDate": "2027-05-30"} | да | 201, 400, 401, 404, 405, 500 |
| GET | /booking | ?id=1 | да | 200, 400, 401, 403, 404, 405, 500 |
| DELETE | /booking | ?id=1 | да | 204, 400, 401, 403, 404, 405, 500 |
| GET | /booking/my | ?sort=date&direction=asc\|desc | да | 200, 400, 401, 405, 500 |
| POST | /property/search | ?page=1&size=5&sort=name\|price&sortDirection=asc\|desc<br>{"city": "moscow", "startDate": "2026-03-01", "endDate": "2026-03-05", "minPrice": 500, "maxPrice": 2000} | нет | 200, 400, 405, 500 |
| GET | /property | ?id=1 | нет | 200, 400, 404, 405, 500 |
| DELETE | /property | ?id=1 | да | 204, 400, 401, 403, 404, 405, 500 |
| GET | /property/availability | ?id=1 | нет | 200, 400, 404, 405, 500 |
| GET | /property/allbookings | ?id=5&page=1&size=5 | да | 200, 400, 401, 403, 404, 405, 500 |
| POST | /user/register | {"name": "boris", "password": "1234"} | нет | 201, 400, 405, 500 |
| POST | /user/login | {"name": "boris", "password": "1234"} | нет | 200, 400, 401, 405, 500 |
| POST | /user/property | {"name": "Econom Hostel Nsk", "city": "novosibirsk", "price": 500} | да | 201, 400, 401, 405, 500 |
| GET | /user | ?id=15 | нет | 200, 400, 404, 405, 500 |

POST /booking, /user/register и /user/property возвращают 201 с заголовком Location
на созданный ресурс. 405 сопровождается заголовком Allow.


### Что планирую улучшить
1) booking/{id} вместо ?id=
2) JWT вместо сессий в памяти 
3) тесты
4) собрать исполняемый jar через maven-shade-plugin, чтобы запуск не требовал IDE
5) миграция на Spring Boot


