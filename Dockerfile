FROM adoptopenjdk/openjdk11
COPY build/libs/*.jar onetwoclass.jar
ENTRYPOINT ["java", "-jar", "onetwoclass.jar"]