# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR-файл в контейнер
COPY build/libs/*.jar app.jar

# Открываем порт для доступа к приложению
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
