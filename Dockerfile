# Используем официальный образ OpenJDK как базовый
FROM openjdk:17-jdk-alpine AS builder

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем файлы Gradle
COPY gradle /app/gradle
COPY gradlew /app/
COPY build.gradle.kts /app/
COPY settings.gradle.kts /app/

# Добавляем права на выполнение для gradlew
RUN chmod +x gradlew

# Загружаем зависимости
RUN ./gradlew dependencies

# Копируем весь исходный код
COPY src /app/src

# Сборка JAR файла
RUN ./gradlew shadowJar

# Используем другой базовый образ для выполнения
FROM openjdk:17-jdk-alpine

# Копируем собранный JAR файл из предыдущего этапа
COPY --from=builder /app/build/libs/your-app.jar /app/app.jar

# Определяем переменные окружения
ENV DATABASE_URL=jdbc:postgresql://dpg-cqp5kl2j1k6c73ddk5a0-a.oregon-postgres.render.com:5432/wb_meetings_app_database
ENV DATABASE_USER=wb_meetings_app_database_user
ENV DATABASE_PASSWORD=c90zAYlnaaDELBJXrPbBUlN8LRhoD7HI

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/app.jar"]