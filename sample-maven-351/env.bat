@ECHO OFF
SET ATTIVIO_HOME=C:\code\attivio\aie_3.5.1
SET ATTIVIO_PROJECT=C:\code\GitHub\aie-maven\.\sample-maven-351
SET PATH=%ATTIVIO_HOME%\bin;%PATH%
IF "%ATTIVIO_ZOOKEEPER_INFO%"=="" (
  SET ZOOINFO=%2
) ELSE (
  SET ZOOINFO=%ATTIVIO_ZOOKEEPER_INFO%
)
