# DirectoryApplication — Сервер

Серверная часть приложения "Справочник сотрудников". Разработана на фреймворке Ktor (Kotlin) с подключением к облачной базе данных PostgreSQL (Neon.tech) и авторизацией через Firebase.

## Технологии

| Технология | Описание |
|---|---|
| Ktor | Серверный фреймворк на Kotlin |
| PostgreSQL (Neon.tech) | Облачная база данных |
| Exposed | ORM для работы с БД |
| HikariCP | Пул соединений с БД |
| Firebase Admin SDK | Верификация JWT-токенов |
| kotlinx.serialization | Сериализация JSON |

## Архитектура проекта

```
src/main/kotlin/com/directory/
├── Application.kt                  # Точка входа
├── plugins/
│   ├── Routing.kt                  # Настройка маршрутов
│   ├── Security.kt                 # Firebase авторизация
│   └── Serialization.kt            # JSON сериализация
├── db/
│   ├── DatabaseFactory.kt          # Подключение к Neon.tech
│   └── tables/
│       └── EmployeesTable.kt       # Таблица сотрудников
├── data/
│   └── EmployeeRepositoryImpl.kt   # SQL-запросы
├── domain/
│   ├── models/
│   │   └── Employee.kt             # Модель сотрудника
│   └── repository/
│       └── EmployeeRepository.kt   # Интерфейс репозитория
└── routes/
    ├── dto/
    │   ├── AuthRequest.kt
    │   ├── EmployeeRequest.kt
    │   └── EmployeeResponse.kt
    ├── AuthRoutes.kt
    └── EmployeeRoutes.kt
```

## Требования

- JDK 11+
- IntelliJ IDEA
- Аккаунт на [Neon.tech](https://neon.tech)
- Проект Firebase с файлом `serviceAccountKey.json`

## Настройка и запуск

### 1. Клонировать репозиторий

```bash
git clone https://github.com/ваш-username/DirectoryApplication-Server.git
cd DirectoryApplication-Server
```

### 2. Добавить Firebase ключ

Скачать `serviceAccountKey.json` из Firebase Console:
- Настройки проекта → Service Accounts → Generate new private key

Положить файл в корень проекта:
```
DirectoryApplication-Server/
└── serviceAccountKey.json
```

### 3. Настроить базу данных

В файле `DatabaseFactory.kt` указать данные подключения к Neon.tech:
```kotlin
jdbcUrl = "jdbc:postgresql://ваш-хост/neondb?sslmode=require"
username = "ваш_пользователь"
password = "ваш_пароль"
```

Или через переменные окружения:
```
DATABASE_URL=jdbc:postgresql://...
DATABASE_USER=...
DATABASE_PASSWORD=...
```

### 4. Запустить сервер

```bash
./gradlew run
```

Сервер запустится на `http://0.0.0.0:8080`

## API Endpoints

### Публичные (без авторизации)

| Метод | Endpoint | Описание |
|---|---|---|
| GET | `/public/employees` | Список всех сотрудников |
| GET | `/public/employees/search?q=Иван` | Поиск сотрудников |

### Защищённые (требуют Firebase токен)

Заголовок: `Authorization: Bearer <Firebase ID Token>`

| Метод | Endpoint | Описание |
|---|---|---|
| GET | `/api/employees` | Список всех сотрудников |
| GET | `/api/employees/{id}` | Сотрудник по ID |
| GET | `/api/employees/search?q=текст` | Поиск |
| POST | `/api/employees` | Создать сотрудника |
| PUT | `/api/employees/{id}` | Обновить сотрудника |
| DELETE | `/api/employees/{id}` | Удалить сотрудника |

### Пример запроса

```bash
curl -H "Authorization: Bearer <токен>" \
     http://localhost:8080/api/employees
```

### Пример ответа

```json
[
  {
    "id": 1,
    "name": "Иван Иванов",
    "position": "Разработчик",
    "phone": "+7 (999) 123-45-67",
    "email": "иван@company.com",
    "department": "IT"
  }
]
```

## Запуск тестов

```bash
./gradlew test
```
