### Some notes on the gradle build process

We are using the Java Convention plugin in order to handle subprojects with common configuration.

Libraries common to all projects will be defined inside buildSrc clean-architecture-java-spring.java-conventions.gradle file.
Read this before anything :
- https://docs.gradle.org/current/userguide/declaring_dependencies_between_subprojects.html
- https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/

Dependencies follow the clean architecture requirements (core module has no dependencies inside the project):
core
details -> core
app -> details -> core