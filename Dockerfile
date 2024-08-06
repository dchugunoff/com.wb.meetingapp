# Use the official Gradle image to create a build artifact.
# https://hub.docker.com/r/library/gradle
FROM gradle:7.4.2-jdk11 as build

# Copy local code to the container image.
WORKDIR /home/gradle/project
COPY . .

# Build a release artifact.
RUN gradle shadowJar

# Use the official openjdk image to run the application.
# https://hub.docker.com/r/library/openjdk
FROM openjdk:11-jre-slim

# Copy the jar to the production image from the builder stage.
COPY --from=build /home/gradle/project/build/libs/com.wb.meetingapp-all.jar /app/meetingapp.jar

# Run the web service on container startup.
CMD ["java", "-jar", "/app/meetingapp.jar"]