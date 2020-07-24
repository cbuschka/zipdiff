#!/bin/bash

VERSION=1.0.2
ZIPDIFF_DIR=${HOME}/.zipdiff/${VERSION}
ZIPDIFF_JAR=${ZIPDIFF_DIR}/zipdiff.jar

mkdir -p ${ZIPDIFF_DIR}

if [ ! -f "${ZIPDIFF_JAR}" ]; then
  echo "Downloading zipdiff-${VERSION}..." >/dev/stderr
  curl -sL -o /tmp/zipdiff.jar https://github.com/cbuschka/zipdiff/releases/download/zipdiff-${VERSION}/zipdiff.jar
  mv /tmp/zipdiff.jar ${ZIPDIFF_JAR}
fi

java -jar ${ZIPDIFF_JAR} "$@"
