# Version
This uses JUnit4, and has been tested on JDK version 27.
## Tests

The test for this module use junit-4.13.2 and requires hamcrest-core-1.3 to be run correctly.  If you have these installed, you can replace the path to junit-4.13.2 and hamcrest-core-1.3 with the path to your own files.  This should not be required, as junit-4.13.2.jar and hamcrest-core-1.3.jar are both included in this zip.

## Compiling Tests

As the bytecode is already located in this zip, compiling the tests should not be necessary. However, if the tests need to be recompiled please run the following command, on Linux or MacOS.

```
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar *.java
```

And on Windows

```
javac -cp .;junit-4.13.2.jar;hamcrest-core-1.3.jar *.java
```

## Running Tests

To run the tests, simply run the test suite TestAll with the following command, on Linux or MacOS.

```
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore TestAll
```

And on Windows

```
java -cp .;junit-4.13.2.jar;hamcrest-core-1.3.jar org.junit.runner.JUnitCore TestAll
```

To run a particular test class, simple replace TestAll with the desired class in the above command.
