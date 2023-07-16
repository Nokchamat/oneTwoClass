FROM adoptopenjdk/openjdk11
RUN mkdir -p deploy
WORKDIR /deploy
COPY ./build/libs/*.jar onetwoclass.jar
ENTRYPOINT ["java", "-jar", "/deploy/onetwoclass.jar"]