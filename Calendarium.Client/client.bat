call init.bat

set SWING_HOME=%JAVA_JRE%\lib

set JCP=%JAVA_JRE%\lib\rt.jar;%SWING_HOME%\swingall.jar;%CALENDARIUM_HOME%; 
set JAVA_BIN=%JAVA_JRE%\bin\java
if exist %JAVA_HOME%\bin\jre.exe set JAVA_BIN=%JAVA_HOME%\bin\jre

set CLASSPATH=.
java -Djava.security.policy=java.policy client.Client
