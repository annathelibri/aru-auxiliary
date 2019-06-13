FROM openjdk:12 AS builder

ARG version

WORKDIR /aru-auxiliary

COPY run/aru-auxiliary-${version}-all.jar aru-auxiliary.jar
COPY run/jlink.sh jlink.sh

ENV ADDITIONAL_MODULES=jdk.crypto.ec

RUN ["bash", "jlink.sh", "aru-auxiliary.jar"]

FROM frolvlad/alpine-glibc:alpine-3.9

ARG jattachVersion

WORKDIR /aru-auxiliary

RUN apk add --no-cache libstdc++

RUN wget "https://www.archlinux.org/packages/core/x86_64/zlib/download" -O /tmp/libz.tar.xz \
    && mkdir -p /tmp/libz \
    && tar -xf /tmp/libz.tar.xz -C /tmp/libz \
    && cp /tmp/libz/usr/lib/libz.so.1.2.11 /usr/glibc-compat/lib \
    && /usr/glibc-compat/sbin/ldconfig \
    && rm -rf /tmp/libz /tmp/libz.tar.xz

RUN wget https://github.com/apangin/jattach/releases/download/$jattachVersion/jattach -O /bin/jattach
RUN chmod +x /bin/jattach

COPY run/aru.java.security aru.java.security
COPY --from=builder /aru-auxiliary /aru-auxiliary

CMD ["jrt/bin/java", "-Djava.security.properties=./aru.java.security", "-jar", "aru-auxiliary.jar"]