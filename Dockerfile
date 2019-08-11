FROM openjdk:12 AS builder

ARG version

WORKDIR /aru-auxiliary

COPY run/aru-auxiliary-${version}-all.jar aru-auxiliary.jar
COPY run/jlink.sh jlink.sh

ENV ADDITIONAL_MODULES=jdk.crypto.ec

RUN ["bash", "jlink.sh", "aru-auxiliary.jar"]

FROM adriantodt/alpine-zlib-jattach:3.9-1.2.11-1.5

WORKDIR /aru-auxiliary

COPY run/aru.java.security aru.java.security
COPY --from=builder /aru-auxiliary /aru-auxiliary

CMD ["jrt/bin/java", "-Djava.security.properties=./aru.java.security", "-jar", "aru-auxiliary.jar"]