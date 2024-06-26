FROM ubuntu:24.04
RUN apt-get update
RUN apt install openjdk-17-jdk -y
RUN apt install git -y
RUN apt install vim -y
RUN mkdir /upload && \
    chmod 777 /upload && \
    git clone https://github.com/Gominbong/spring_board_service.git && \
    cd /spring_board_service/ && \
    chmod +x * && \
    ./gradlew build