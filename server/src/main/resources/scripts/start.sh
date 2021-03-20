#!/bin/sh
java -jar server-0.0.1-SNAPSHOT.jar & echo $! > ./pid.file &