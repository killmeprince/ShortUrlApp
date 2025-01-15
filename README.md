URL Shortener Service — это RESTful-приложение для сокращения длинных URL и управления ими.



!!!!!!!
Используйте API через Postman или PowerShell.
!!!!!!!


*****
ВОЗМОЖНОСТИ:
*****


Создание коротких ссылок: Принимает длинный URL и возвращает уникальную короткую ссылку для каждого пользователя.

Управление ограничениями:

1.Лимит количества переходов.

2.Ограничение времени жизни ссылки.

Поддержка нескольких пользователей: Каждому пользователю присваивается уникальный UUID.

Управление ссылками: Пользователи могут редактировать и удалять свои ссылки.

Переадресация: Переход по короткой ссылке на оригинальный ресурс.


*****
Как пользоваться сервисом?
*****

Нужен свободный 8080 порт.
Запуск с помощью: mvn spring-boot:run, ну и на локалхосте будет приложение.
Доступ к бд по адресу: http://localhost:8080/h2-console
Логин: sa
Пароль оставить пустым.


*****
Поддерживаемые команды:
*****


1. Создание короткой ссылки (/api/shorten)
   Пример:
Invoke-RestMethod -Uri "http://localhost:8080/api/shorten" `
    -Method POST `
    -Headers @{"Content-Type"="application/json"} `
    -Body '{"originUrl":"https://www.example.com","userId":"7d10b455-4a8e-49f0-8281-fdf8bbdffd04"}'

2. Получение информации о ссылке (/api/info/{shortUrl})
   Пример:
Invoke-RestMethod -Uri "http://localhost:8080/api/info/c9dec5e4" -Method GET

3. Переадресация по короткой ссылке (/api/{shortUrl})
   Пример:
Invoke-RestMethod -Uri "http://localhost:8080/api/c9dec5e4" -Method GET
4. Получение всех ссылок пользователя (/api/user/{userId})
   Пример:
Invoke-RestMethod -Uri "http://localhost:8080/api/user/7d10b455-4a8e-49f0-8281-fdf8bbdffd04" -Method GET
5. Обновление лимита кликов(/api/updateLimit/{shortUrl})
   Пример:
Invoke-RestMethod -Uri "http://localhost:8080/api/updateLimit/c9dec5e4" `
    -Method PUT `
    -Headers @{"Content-Type"="application/json"} `
    -Body @{clientClicksLimit=50}
6. Удаление ссылки (/api/{shortUrl})
   Пример:
Invoke-RestMethod -Uri "http://localhost:8080/api/c9dec5e4" `
    -Method DELETE `
    -Headers @{"Content-Type"="application/json"} `
    -Body @{userId="7d10b455-4a8e-49f0-8281-fdf8bbdffd04"}


   
*****
Описание модулей
*****




1. controllers/ShortUrlController.java

Основной контроллер, предоставляющий API для работы с короткими ссылками.

2. services/UrlService.java

Содержит логику создания, управления и удаления ссылок.

3. services/LinkCleaningService.java

Запускает задачу для автоматического удаления устаревших ссылок.

4. entities/Url.java

JPA-сущность для работы с базой данных, представляющая данные коротких ссылок.

5. repository/UrlRepository.java

Репозиторий для взаимодействия с базой данных.

6. conf/AppProperties.java

Конфигурация приложения: параметры по умолчанию, такие как время жизни ссылки и лимит переходов.
