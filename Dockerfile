FROM openjdk:21

WORKDIR /app

COPY ./target/student2-0.0.1-SNAPSHOT.jar /app/student2-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "student2-0.0.1-SNAPSHOT.jar"]
