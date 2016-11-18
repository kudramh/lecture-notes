==============================================================
Maven Archetype Karaf Project
==============================================================
@> mvn archetype:generate \
  -DarchetypeGroupId=org.apache.karaf.archetypes \
  -DarchetypeArtifactId=karaf-command-archetype \
  -DarchetypeVersion=4.0.7 \
  -DgroupId=com.kudram \
  -DartifactId=com.kudram.command \
  -Dversion=1.0.0 \
  -Dpackage=com.kudram.karaf.shell

==============================================================
Maven Installation
==============================================================
@> mvn install

==============================================================
Karaf Command Installation
==============================================================
@karaf> bundle:install -s mvn:com.kudram/com.kudram.command/1.0.0

==============================================================
Karaf Command Installation
==============================================================
@karaf> kudram:echo -o parameter1 value1
