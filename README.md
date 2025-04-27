# Library Management System

## Описание проекта
Library — это веб-приложение для управления библиотечным фондом. Оно позволяет выдавать книги пользователям в аренду и оформлять их возврат. Приложение разработано с использованием Spring Boot, Thymeleaf и Bootstrap.

<img width="1121" alt="Screenshot 2025-04-28 at 01 17 15" src="https://github.com/user-attachments/assets/c60d69cb-e052-478b-9828-90a4fd094c20" />

## Функционал
- Поиск книг в каталоге
- Оформление аренды книги пользователем
- Запрет на повторное оформление книги, пока она на руках у пользователя
- Контроль количества доступных копий книг
- Управление пользователями и книгами
- Отслеживание истории аренды
<img width="1122" alt="Screenshot 2025-04-28 at 01 17 25" src="https://github.com/user-attachments/assets/8df61243-0c60-4ab9-bbf9-90b67cf3ea83" />
<img width="1198" alt="Screenshot 2025-04-28 at 01 18 07" src="https://github.com/user-attachments/assets/55897949-0f33-444c-a296-89681d64efa5" />

## Технологии
### Backend
- **Spring Boot** — основной фреймворк
- **Spring Data JPA** — для работы с базой данных
- **Spring MVC** — для управления веб-интерфейсом
- **Spring AOP (AspectJ)** — для логирования
- **MySQL** — база данных
- **Lombok** — для упрощения кода
- **JUnit + Mockito + AssertJ** — для тестирования

### Frontend
- **Thymeleaf** — шаблонизатор
- **Bootstrap** — стилизация интерфейса

## Структура проекта
```
├── src/main/java/com/romsa/library
│   ├── controller        # Контроллеры Spring MVC
│   ├── entity            # Сущности JPA (User, Book, BookLoan)
│   ├── repository        # Репозитории Spring Data JPA
│   ├── service           # Бизнес-логика приложения
│   ├── aspect            # Логирование действий
│   ├── config            # Конфигурационные классы
│   ├── LibraryApplication.java  # Главный класс Spring Boot
│
├── src/main/resources
│   ├── templates         # HTML-шаблоны для Thymeleaf
│   ├── static/css        # CSS-стили
│   ├── application.yml   # Конфигурация Spring Boot
│
├── src/test/java/com/romsa/library
│   ├── service           # Тесты сервисов (JUnit, Mockito, AssertJ)
│   ├── repository        # Тесты репозиториев
│
├── pom.xml               # Maven зависимости
├── README.md             # Описание проекта
```

## Запуск проекта
1. **Настроить базу данных**
   - Установить MySQL
   - Создать базу данных `library`
   - Изменить настройки в `application.yml`

2. **Собрать и запустить приложение**
```sh
mvn clean install
mvn spring-boot:run
```

3. **Открыть в браузере**
   - `http://localhost:8080`

## Тестирование
Запуск тестов:
```sh
mvn test
```

