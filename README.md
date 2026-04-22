1. Создание задачи (POST)
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


b) Задача со средним приоритетом без срока

curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Купить продукты",
    "description": "Молоко, хлеб, яйца",
    "completed": false,
    "priority": "MEDIUM"
  }'


c) Задача с низким приоритетом и сроком на сегодня


curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Позвонить другу",
    "description": "Поздравить с днём рождения",
    "completed": false,
    "priority": "LOW",
    "deadline": "2026-03-23T18:00:00"
  }'

2. Получение всех задач (GET)

curl -X GET http://localhost:8080/api/todos


3. Получение одной задачи по ID (GET /{id})

curl -X GET http://localhost:8080/api/todos/1

4. Обновление задачи (PUT /{id})
a) Изменение приоритета и срока

curl -X PUT http://localhost:8080/api/todos/2 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Купить продукты",
    "description": "Молоко, хлеб, яйца, масло",
    "completed": true,
    "priority": "HIGH",
    "deadline": "2026-03-24T10:00:00"
  }'


b) Удаление срока (установить null)

curl -X PUT http://localhost:8080/api/todos/3 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Позвонить другу",
    "description": "Поздравить с днём рождения",
    "completed": false,
    "priority": "MEDIUM",
    "deadline": null
  }'

5. Удаление задачи (DELETE /{id})

curl -X DELETE http://localhost:8080/api/todos/3

