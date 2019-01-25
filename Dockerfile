FROM scholzj/centos-java-base:latest

ARG version=latest
ENV VERSION ${version}

COPY ./scripts/ /bin

ADD target/kafka-address-book.jar /

CMD ["/bin/run.sh", "/kafka-address-book.jar"]