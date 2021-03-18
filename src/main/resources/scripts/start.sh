#!/bin/sh
java -jar web-0.0.1-SNAPSHOT.jar & echo $! > ./pid.file &