package sourcetransform;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class App
{
    // args: path to a maven project
    public static void main(String[] args) throws IOException {

        //System.out.println(args[0]);

        //File mvnProjDir = new File("/home/christian/Desktop/test/");
        File mvnProjDir = new File("/home/christian/Desktop/hadoop/");


        /*
        - if test folder then modify all test classes (TODO: create function)
        -
         */

        TestDirectoryVisitor testDirVisitor = new TestDirectoryVisitor();
        Files.walkFileTree(mvnProjDir.toPath(), testDirVisitor);







        //String path = "/home/christian/Desktop/bsc-sourcetransform/src/main/resources/TestFile.java";
        String path = "/home/christian/Desktop/bsc-sourcetransform/src/main/resources/TestKerberosAuthenticator.java";

        File file = new File(path);
        PerformanceMetricsInjector pmi = new PerformanceMetricsInjector(file);
        //pmi.injectMeasurementCode();
        //pmi.writeToConsole();
        //pmi.writeToFile();

        // add dependency to pom.xml

        // traverse over all test classes

            // add injection code to the test cases (methods with Test annotation)

    }
}
