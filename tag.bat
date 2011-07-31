set file=%~f1
set file=%file:\=/%
set file=%file:&=\&%
java -jar sbt-launch-0.7.5.jar "run \"%file%\""
pause