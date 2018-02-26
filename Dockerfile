FROM java:8-jre-alpine

ADD ./target/demo-*.jar /opt/app/run.jar

ENV SPRING_PROFILES_ACTIVE="default"
ENV SPRING_CONFIG_LOCATION="/run/secrets/"

EXPOSE 8080

CMD java -jar /opt/app/run.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE --spring.config.location=$SPRING_CONFIG_LOCATION
