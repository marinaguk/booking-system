# BookingSystem

REST API для бронирования жилья: арендодатель размещает объект, пользователь бронирует.

Первая версия была написана без фреймворков, на стандартном `HttpServer`, чтобы понять, как всё устроено изнутри (тег `v1-httpserver`).
Текущая версия перенесена на Spring Boot.

Архитектура трёхслойная: контроллеры (HTTP), сервисы (бизнес-логика), репозитории (БД).
После входа пользователь получает JWT-токен и прикладывает его к каждому запросу. Сервер проверяет токен и ничего о вошедших не хранит.

### Стек
Java 17\
Spring Boot 4.1.1: Spring Web MVC, Spring Data JPA, Spring Security, Bean Validation\
Hibernate 7\
PostgreSQL 18\
Maven

### Запуск
1. Создать БД: `CREATE DATABASE "BookingDatabase";`
2. Создать таблицы: `psql -U postgres -d BookingDatabase -f schema.sql`\
   Приложение само таблицы не создаёт, а при старте проверяет, что схема совпадает с сущностями (`ddl-auto=validate`).
3. Задать переменные окружения:

   | Переменная | Пример |
   |---|---|
   | `DB_URL` | `jdbc:postgresql://localhost:5432/BookingDatabase` |
   | `DB_USER` | `postgres` |
   | `DB_PASSWORD` | пароль от БД |
   | `JWT_SECRET` | случайная строка не короче 32 символов, например `openssl rand -base64 32` |

4. Запустить одним из способов:
   - `mvn spring-boot:run`
   - собрать jar и запустить его: `mvn package`, затем `java -jar target/BookingSystem-1.0-SNAPSHOT.jar`
   - из IDE: класс `bookingApp.BookingSystemApplication`
5. Сервер стартует на http://localhost:8080

### Аутентификация
`POST /user/login` возвращает токен:

```json
{"token": "eyJhbGciOiJIUzI1NiJ9..."}
```

Дальше его надо передавать в заголовке:

```
Authorization: Bearer <token>
```

Токен действует 1 час. Эндпоинты, помеченные ниже как auth, без токена или с недействительным токеном отвечают 401.

### Ошибки
Ошибки возвращаются в формате [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) (`ProblemDetail`):

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Booking not found",
  "instance": "/booking/999"
}
```

Ошибки валидации дополнительно содержат поле `errors` с сообщением по каждому полю.

### Эндпоинты
Все ответы - JSON. Любой эндпоинт может также вернуть 405 (с заголовком `Allow`) и 500.

| Метод | Путь | Параметры и тело | Auth | Статусы |
|---|---|---|---|---|
| POST | /user/register | {"name": "boris", "password": "1234"} | нет | 201, 400 |
| POST | /user/login | {"name": "boris", "password": "1234"} | нет | 200, 400, 401 |
| GET | /user/{id} | | да | 200, 400, 401, 404 |
| POST | /user/property | {"name": "Econom Hostel Nsk", "city": "novosibirsk", "price": 500} | да | 201, 400, 401 |
| POST | /property/search | ?page=1&size=5&sortBy=name\|price&sortDirection=asc\|desc<br>{"city": "moscow", "startDate": "2026-03-01", "endDate": "2026-03-05", "minPrice": 500, "maxPrice": 2000} | нет | 200, 400 |
| GET | /property/{id} | | нет | 200, 400, 404 |
| DELETE | /property/{id} | | да | 204, 400, 401, 403, 404 |
| GET | /property/{id}/availability | | нет | 200, 400 |
| GET | /property/{id}/bookings | ?page=1&size=5 | да | 200, 400, 401, 403, 404 |
| POST | /booking | {"propertyId": 25, "startDate": "2027-05-20", "endDate": "2027-05-30"} | да | 201, 400, 401, 404 |
| GET | /booking/my | ?sortDirection=asc\|desc | да | 200, 400, 401 |
| GET | /booking/{id} | | да | 200, 400, 401, 403, 404 |
| DELETE | /booking/{id} | | да | 204, 400, 401, 403, 404 |

`POST /user/register`, `POST /user/property` и `POST /booking` возвращают 201 с заголовком `Location` на созданный ресурс.

Удалять и смотреть чужие брони и объекты может только их владелец или пользователь с ролью `ADMIN`, иначе 403.
