package sourcetransform;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class App {
    // args: path to a maven project
    public static void main(String[] args) throws IOException {

        File mvnProjDir = null;

        //System.out.println(args[0]);
        if (args.length == 1) {
            mvnProjDir = new File(args[0]);
            if (!mvnProjDir.isDirectory()) {
                System.out.println("ERROR: Input is not a directory ...");
                return;
            }
        }
        if (args.length != 1) {
            System.out.println("WARNING: Use default project directory ...");
            mvnProjDir = new File("/home/christian/Desktop/my-app");
        }

        TestDirectoryVisitor testDirVisitor = new TestDirectoryVisitor();
        Files.walkFileTree(mvnProjDir.toPath(), testDirVisitor);
    }
}
