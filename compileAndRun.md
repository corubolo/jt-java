# How to compile and run #

1) check out the project from SVN :
`svn checkout http://jt-java.googlecode.com/svn/trunk/ jt-java-read-only`

2) install Maven:
https://maven.apache.org/download.html#Installation

3) cd to the jt-java-read-only/JTViewerGC folder and run
`mvn package`
This will compile and package the software.

4)  To execute the software, you can then use:
`mvn exec:java -Dexec.mainClass="uk.ac.liv.jt.viewer.JTViewer" -Dexec.classpathScope=runtime`

The project is based on Eclipse so you could import the project directly from eclipse if you are familiar with it.