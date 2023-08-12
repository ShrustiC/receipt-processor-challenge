# Fetching latest version of Java (Not using open jdk as its deprecated)
FROM eclipse-temurin:17-jdk

# Setting up work directory
WORKDIR /app

# Automating all manual steps hereafter
# Copying Maven Wrapper and POM
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

#Downloading maven dependencies
RUN ./mvnw dependency:go-offline

#Copying application source code
COPY src ./src

# Starting the application
CMD ["./mvnw", "spring-boot:run"]