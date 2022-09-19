FROM maven:3.8.1-adoptopenjdk-11

COPY ./tools ./tools
COPY ./server ./server

RUN cd ./server && mvn clean install

CMD bash ./tools/entrypoint.sh
