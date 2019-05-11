FROM openjdk:11-jre-slim

ARG version
ARG jattachVersion

RUN apt-get update
RUN apt-get install -y wget
RUN wget https://github.com/apangin/jattach/releases/download/$jattachVersion/jattach -O /bin/jattach
RUN chmod +x /bin/jattach

WORKDIR /aru-auxiliary

COPY aru-auxiliary-${version}-all.jar aru-auxiliary.jar

CMD ["java", "-jar", "aru-auxiliary.jar"]