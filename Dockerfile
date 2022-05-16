FROM eclipse-temurin:18-jdk-alpine AS BUILDER

RUN apk update

WORKDIR /base
COPY . .

RUN ./gradlew installDist --no-daemon

FROM eclipse-temurin:18-jdk
RUN apt-get update -y && apt-get install bash tini -y

WORKDIR /altar

COPY --from=BUILDER /base/build/install/net.altar .

RUN rm ./bin/net.altar.bat

EXPOSE 8080

ENTRYPOINT ["tini"]
CMD ["bash", "./bin/net.altar"]