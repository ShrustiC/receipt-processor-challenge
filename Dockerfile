# Fetching latest version of Java (Not using open jdk as its deprecated)
FROM eclipse-temurin:17-jdk

# Setting up work directory
WORKDIR /app

# Automating all manual steps hereafter
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

# Starting the application
CMD ["./mvnw", "spring-boot:run"]