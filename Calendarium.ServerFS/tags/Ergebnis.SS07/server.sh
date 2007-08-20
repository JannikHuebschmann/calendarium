#!/bin/sh
. je.sh

cd $CALENDARIUM_HOME
$JAVA_BIN -classpath $JCP server.Server
