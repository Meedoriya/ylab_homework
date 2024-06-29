# ДЗ №2 JDBC и Миграции


## Требования

- Репозитории теперь должны писать ВСЕ сущности в БД PostgreSQL
- Идентификаторы при сохранении в БД должны выдаваться через sequence
- DDL-скрипты на создание таблиц и скрипты на предзаполнение таблиц должны выполняться только инструментом миграции Liquibase
- Скрипты миграции Liquibase должны быть написаны в нотации XML или YAML
- Скриптов миграции должно быть несколько. Как минимум один на создание всех таблиц, другой - на предзаполнение данными
- Служебные таблицы должны быть в отдельной схеме
- Таблицы сущностей хранить в схеме public запрещено
- В тестах необходимо использовать test-containers
- В приложении должен быть `docker-compose.yml`, в котором должны быть прописаны инструкции для развертывания PostgreSQL в докере. Логин, пароль и база должны быть отличными от тех, что прописаны в образе по-умолчанию. Приложение должно работать с БД, развернутой в докере с указанными параметрами.
- Приложение должно поддерживать конфиг-файлы. Всё, что относится к подключению БД, а также к миграциям, должно быть сконфигурировано через конфиг-файл.

## Выполненные задачи

1. **Репозитории теперь пишут все сущности в БД PostgreSQL:**
  - Реализованы репозитории для всех сущностей (`User`, `Workspace`, `ConferenceRoom`, `Booking`), которые взаимодействуют с PostgreSQL.

2. **Идентификаторы при сохранении в БД выдаются через sequence:**
  - Для всех сущностей созданы sequence в схеме `coworking_service`.

3. **DDL-скрипты на создание таблиц и скрипты на предзаполнение таблиц выполняются инструментом миграции Liquibase:**
  - Все необходимые DDL-скрипты и скрипты на предзаполнение данных реализованы с использованием Liquibase.

4. **Скрипты миграции Liquibase написаны в нотации XML:**
  - Все скрипты миграции представлены в формате XML.

5. **Создано несколько скриптов миграции:**
  - Один скрипт для создания всех таблиц.
  - Второй скрипт для предзаполнения таблиц данными.

6. **Служебные таблицы Liquibase (databasechangelog и databasechangeloglock) хранятся в отдельной схеме:**
  - Создана отдельная схема `liquibase` для хранения служебных таблиц.

7. **Таблицы сущностей хранятся не в схеме public:**
  - Все таблицы сущностей хранятся в схеме `coworking_service`.

8. **В тестах используется test-containers:**
  - Все тесты используют test-containers для изолированного тестирования с использованием PostgreSQL.

9. **Добавлен `docker-compose.yml` для развертывания PostgreSQL в докере:**
  - Файл `docker-compose.yml` содержит инструкции для развертывания PostgreSQL с указанными логином, паролем и базой данных.

10. **Приложение поддерживает конфиг-файлы для настройки подключения к БД и миграций:**
  - Добавлены конфиг-файлы для настройки подключения к базе данных и Liquibase.

## Структура проекта

```plaintext
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org
│   │   │       └── alibi
│   │   │           ├── application
│   │   │           ├── domain
│   │   │           ├── in
│   │   │           ├── out
│   │   │           └── service
│   │   ├── resources
│   │       ├── db
│   │       │   └── changelog
│   │       │       ├── changelog-1-create-tables.xml
│   │       │       ├── changelog-2-insert-data.xml
│   │       │       └── changelog.xml
│   │       └── application.properties
│   └── test
│       ├── java
│           └── org
│               └── alibi
│      
├── docker-compose.yml
├── pom.xml
└── README.md
```

### Требования и пояснение к реализации

- **Репозитории теперь должны писать ВСЕ сущности в БД PostgreSQL**  
  Все репозитории были переписаны для работы с базой данных PostgreSQL. Для подключения к базе данных используется JDBC.

- **Идентификаторы при сохранении в БД должны выдаваться через sequence**  
  Для каждой сущности были созданы последовательности в базе данных, и идентификаторы генерируются с использованием этих последовательностей. Это реализовано в Liquibase миграциях.

- **DDL-скрипты на создание таблиц и скрипты на предзаполнение таблиц должны выполняться только инструментом миграции Liquibase**  
  Все DDL-скрипты и скрипты предзаполнения данных были вынесены в Liquibase миграции. Это обеспечивает автоматическое создание и наполнение таблиц при запуске приложения.

- **Скрипты миграции Liquibase должны быть написаны в нотации XML или YAML**  
  Все скрипты миграции написаны в нотации XML. Это позволяет легко управлять версионностью и изменениями в структуре базы данных.

- **Скриптов миграции должно быть несколько. Как минимум один на создание всех таблиц, другой - на предзаполнение данными**  
  Реализованы два основных скрипта миграции: один для создания всех необходимых таблиц, другой - для предзаполнения таблиц данными.

- **Служебные таблицы должны быть в отдельной схеме**  
  Служебные таблицы Liquibase (`databasechangelog` и `databasechangeloglock`) были вынесены в отдельную схему `liquibase`.

- **Таблицы сущностей хранить в схеме public запрещено**  
  Все таблицы сущностей были созданы в схеме `coworking_service`, а не в схеме `public`.

- **В тестах необходимо использовать test-containers**  
  Для выполнения интеграционных тестов с базой данных используется библиотека `Testcontainers`. Это позволяет поднять экземпляр PostgreSQL в Docker контейнере для проведения тестов.

- **В приложении должен быть docker-compose.yml, в котором должны быть прописаны инструкции для развертывания PostgreSQL в Docker. Логин, пароль и база должны быть отличными от тех, что прописаны в образе по-умолчанию. Приложение должно работать с БД, развернутой в Docker с указанными параметрами**  
  В проект добавлен `docker-compose.yml` файл, который содержит инструкции для развертывания PostgreSQL с пользовательскими логином, паролем и именем базы данных. Приложение успешно подключается к базе данных, развернутой в Docker.

- **Приложение должно поддерживать конфиг-файлы. Всё, что относится к подключению БД, а также к миграциям, должно быть сконфигурировано через конфиг-файл**  
  Настройки подключения к базе данных и параметры миграции Liquibase вынесены в конфигурационный файл `application.properties`. Это позволяет легко изменять настройки без необходимости модифицировать код приложения.

## Заключение

Приложение успешно реализует управление коворкинг-пространством с функционалом регистрации, авторизации, бронирования и управления рабочими местами и конференц-залами, а также позволяет пользователям просматривать свои бронирования и фильтровать их по различным параметрам.
