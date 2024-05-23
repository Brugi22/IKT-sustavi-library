#FROM maven:3.9-eclipse-temurin-21-jammy as build-stage
#WORKDIR /opt/library-service
#COPY pom.xml .
#RUN mvn dependency:go-offline
#COPY ./src ./src
#RUN mvn clean package -Dmaven.test.skip=true

#FROM eclipse-temurin:21-jre-jammy
#WORKDIR /opt/library-service
#COPY --from=build-stage /opt/library-service/target/library-service-0.0.1-SNAPSHOT.jar ./app.jar
#ENTRYPOINT ["java","-jar","app.jar"]
