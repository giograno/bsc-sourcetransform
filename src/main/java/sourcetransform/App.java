package sourcetransform;


import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class App {
    // args: path to a maven project
    public static void main(String[] args) {

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
            mvnProjDir = new File("/home/christian/Desktop/hadoop");
        }

        // Travers the directory tree with a visitor to modify the test code
        TestDirectoryVisitor testDirVisitor = new TestDirectoryVisitor();
        try {
            Files.walkFileTree(mvnProjDir.toPath(), testDirVisitor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add metric dependencies to the pom
        try {
            addMetricDepsToPom(new File(mvnProjDir.getAbsolutePath()+"/pom.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void addMetricDepsToPom(File pomFile) throws IOException, XmlPullParserException {
        System.out.println("Add dependencies to pom: ");
        System.out.println(pomFile.getAbsolutePath());

        // define metrics dependencies
        Dependency metricsCore = new Dependency();
        metricsCore.setGroupId("io.dropwizard.metrics");
        metricsCore.setArtifactId("metrics-core");
        metricsCore.setVersion("3.2.6");
        //metricsCore.setVersion("3.2.2");

        Dependency metricsJvm = new Dependency();
        metricsJvm.setGroupId("io.dropwizard.metrics");
        metricsJvm.setArtifactId("metrics-jvm");
        metricsJvm.setVersion("3.2.6");
        //metricsJvm.setVersion("3.2.2");

        // setup maven pom model
        Reader reader = new FileReader(pomFile);
        MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
        Model model = xpp3Reader.read(reader);

        List<Dependency> dependencyList = new ArrayList<Dependency>(model.getDependencies());
        dependencyList.add(metricsCore);
        dependencyList.add(metricsJvm);


        model.setDependencies(dependencyList);

        MavenXpp3Writer xpp3Writer = new MavenXpp3Writer();

        xpp3Writer.write(new FileWriter(pomFile), model);


        reader.close();
    }
}
