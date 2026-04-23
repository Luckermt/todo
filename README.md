# Todo REST API

Простое приложение для управления задачами, реализованное на Spring Boot с REST API и хранением в H2 (в памяти).  
Поддерживает создание, просмотр, обновление и удаление задач, а также сортировку по приоритету и дедлайну.


## 🔧 Требования

- Java 17 или выше
- Maven 3.8+ (рекомендуется использовать встроенный Maven Wrapper)
- Docker (опционально, для контейнеризации)


## 💻 Локальный запуск

1. **Клонируйте репозиторий**
   git clone https://github.com/your-username/todo-app.git
   cd todo-app

2. **Соберите проект и запустите тесты**
./mvnw clean package

3. **Запустите приложение**
./mvnw spring-boot:run

**Или после сборки:**

java -jar target/todo-*.jar

**Приложение будет доступно по адресу:**
http://localhost:8080/api/todos

База данных H2 автоматически создаётся при старте.

🐳 **Запуск через Docker**

  1. **Соберите Docker-образ**

    docker build -t todo-app .

  2. **Запустите контейнер**

    docker run -p 8080:8080 todo-app

Приложение будет доступно так же на порту 8080.

1. **Создание задачи (POST)**
a) Задача с высоким приоритетом и указанным сроком

curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Сдать проект",
    "description": "Завершить to-do list",
    "completed": false,
    "priority": "HIGH",
    "deadline": "2026-03-30T23:59:59"
  }'

b) **Задача со средним приоритетом без срока**

curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Купить продукты",
    "description": "Молоко, хлеб, яйца",
    "completed": false,
    "priority": "MEDIUM"
  }'


c) **Задача с низким приоритетом и сроком на сегодня**


curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Позвонить другу",
    "description": "Поздравить с днём рождения",
    "completed": false,
    "priority": "LOW",
    "deadline": "2026-03-23T18:00:00"
  }'

2. **Получение всех задач (GET)**

curl -X GET http://localhost:8080/api/todos


3. **Получение одной задачи по ID (GET /{id})**

curl -X GET http://localhost:8080/api/todos/1

4. **Обновление задачи (PUT /{id})**
a) **Изменение приоритета и срока**

curl -X PUT http://localhost:8080/api/todos/2 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Купить продукты",
    "description": "Молоко, хлеб, яйца, масло",
    "completed": true,
    "priority": "HIGH",
    "deadline": "2026-03-24T10:00:00"
  }'


b) **Удаление срока (установить null)**

curl -X PUT http://localhost:8080/api/todos/3 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Позвонить другу",
    "description": "Поздравить с днём рождения",
    "completed": false,
    "priority": "MEDIUM",
    "deadline": null
  }'

5. **Удаление задачи (DELETE /{id})**

curl -X DELETE http://localhost:8080/api/todos/3

🧪 **Тестирование и покрытие кода**

1. **Запуск тестов**

./mvnw test

2. **Генерация отчёта о покрытии (JaCoCo)**

Отчёт создаётся автоматически при выполнении фазы verify:

./mvnw verify

После выполнения команды отчёт будет доступен по пути:
target/site/jacoco/index.html

Отчёт о покрытии кода, генерируемый JaCoCo, представляет собой набор HTML-страниц, которые открываются в браузере. Главный файл отчёта находится по пути:
target/site/jacoco/index.html

3. **Проверка порога покрытия**

В проекте настроено обязательное покрытие не менее 50%.
Если покрытие ниже порога, сборка завершится с ошибкой.
Проверка выполняется командой:

./mvnw jacoco:check

или автоматически при mvn verify.

🧹 **Линтинг (Checkstyle)**

В проекте используется Checkstyle с конфигурацией на основе Google Java Style Guide.
Файл конфигурации: checkstyle-google.xml.
Запуск проверки стиля кода
bash

./mvnw checkstyle:check

При обнаружении нарушений сборка завершится с ошибкой.
Отчёт о нарушениях сохраняется в target/checkstyle-result.xml.

⚙️ **CI/CD Pipeline**

**В репозитории настроен GitHub Actions** (.github/workflows/maven.yml).
Pipeline запускается при:

    создании Pull Request в ветки main или master

    push в ветку main

**Этапы пайплайна:**
build	Компиляция проекта (mvn clean compile)
lint	Проверка стиля кода с помощью Checkstyle (mvn checkstyle:check)
test	Запуск тестов, генерация отчёта JaCoCo и проверка покрытия (mvn verify)
docker_build	Сборка Docker-образа и публикация в Docker Hub (только при push в main)

**Переменные окружения**

Для публикации образа в Docker Hub необходимо добавить в настройки репозитория секреты:

    DOCKER_USERNAME – имя пользователя Docker Hub

    DOCKER_PASSWORD – пароль или Access Token (рекомендуется)

**Тегирование Docker-образа**

При успешном прохождении всех шагов создаётся образ с тегами:

    luckermt/todo-app:latest

    luckermt/todo-app:<git_commit_sha>
