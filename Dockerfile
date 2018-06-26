FROM enmasseproject/java-base:8-3

ARG version=latest
ENV VERSION ${version}
ADD target/kafka-address-book.jar /

CMD ["/bin/launch_java.sh", "/kafka-address-book.jar"]