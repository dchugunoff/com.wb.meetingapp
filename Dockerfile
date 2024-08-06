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
ENV DATABASE_URL=jdbc:postgresql://your-db-host:5432/your-db-name
ENV DATABASE_USER=your-db-username
ENV DATABASE_PASSWORD=your-db-password

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/app.jar"]