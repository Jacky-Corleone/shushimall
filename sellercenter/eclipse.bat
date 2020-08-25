@echo -----------------------------------------------------------------------------
@echo 生成eclipse工程
@echo -----------------------------------------------------------------------------
set MAVEN_HOME=D:\apache-maven-3.2.5
set Path=%MAVEN_HOME%\bin
mvn eclipse:clean eclipse:eclipse -DdownloadSources=true
@pause