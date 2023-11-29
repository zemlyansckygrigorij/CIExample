FROM openjdk:8
EXPOSE 8080
ADD build/libs/CIExample-0.0.1-SNAPSHOT.jar springboot-image-actions.jar
ENTRYPOINT ["java","-jar","/springboot-image-actions.jar"]