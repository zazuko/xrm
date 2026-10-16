# XRM command line tool (no Eclipse needed).
#   docker build -t xrm .
#   docker run --rm -v "$PWD":/work xrm generate /work/my-project
#
# The jar is platform independent, so it is built once on the build platform
# and only the runtime stage differs per architecture.
FROM --platform=$BUILDPLATFORM maven:3.9.16-eclipse-temurin-17 AS build
WORKDIR /src
COPY com.zazuko.rdfmapping.dsl.parent ./com.zazuko.rdfmapping.dsl.parent
RUN --mount=type=cache,target=/root/.m2 \
    cd com.zazuko.rdfmapping.dsl.parent \
 && mvn -B -q package -DskipTests \
      -pl com.zazuko.rdfmapping.dsl.target,com.zazuko.rdfmapping.dsl,com.zazuko.rdfmapping.dsl.ide \
 && cp com.zazuko.rdfmapping.dsl.ide/target/*-cli.jar /xrm.jar

FROM eclipse-temurin:17-jre
LABEL org.opencontainers.image.source="https://github.com/zazuko/xrm" \
      org.opencontainers.image.description="Expressive RDF Mapper (XRM) command line generator" \
      org.opencontainers.image.licenses="MIT"
COPY --from=build /xrm.jar /opt/xrm/xrm.jar
WORKDIR /work
ENTRYPOINT ["java", "-jar", "/opt/xrm/xrm.jar"]
