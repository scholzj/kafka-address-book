DOCKERFILE_DIR     ?= ./
DOCKER_REGISTRY    ?= docker.io
DOCKER_ORG         ?= scholzj
DOCKER_PROJECT     ?= kafka-address-book
DOCKER_TAG         ?= latest
DOCKER_VERSION_ARG ?= latest

all: java_build docker_build docker_push
build: java_build

java_build:
	echo "Building JAR file ..."
	mvn package

java_clean:
	echo "Cleaning Maven build ..."
	mvn clean

docker_build:
	echo "Building Docker image ..."
	docker build --build-arg version=$(DOCKER_VERSION_ARG) -t $(DOCKER_ORG)/$(DOCKER_PROJECT):$(DOCKER_TAG) $(DOCKERFILE_DIR)

docker_tag:
	echo "Tagging $(DOCKER_ORG)/$(DOCKER_PROJECT):$(DOCKER_TAG) to $(DOCKER_REGISTRY)/$(DOCKER_ORG)/$(DOCKER_PROJECT):$(DOCKER_TAG) ..."
	docker tag $(DOCKER_ORG)/$(DOCKER_PROJECT):$(DOCKER_TAG) $(DOCKER_REGISTRY)/$(DOCKER_ORG)/$(DOCKER_PROJECT):$(DOCKER_TAG)

docker_push: docker_tag
	echo "Pushing $(DOCKER_REGISTRY)/$(DOCKER_ORG)/$(DOCKER_PROJECT):$(DOCKER_TAG) ..."
	docker push $(DOCKER_REGISTRY)/$(DOCKER_ORG)/$(DOCKER_PROJECT):$(DOCKER_TAG)