#!/bin/bash

LIBS=".:http.jar:client.jar"

java -cp $LIBS -Djava.util.logging.config.file=logging.properties Tester $1 $2 url.txt

