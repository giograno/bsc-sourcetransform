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
import java.util.prefs.Preferences;

public class App {
    public static String outputMeasurementPath;
    public static String projectName;
    public static String commit_hash;
    public static int currentIteration;
    public final static String CURRENT_ROUND = "iDFlakies-round";

    // args: path to a maven project, path to output file, project name, commit hash
    // iteration number get now read via Java preferences
    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println("WARNING: Wrong number of args ...");
            return;
        }

        File mvnProjDir = new File(args[0]);
        if (!mvnProjDir.isDirectory()) {
            System.out.println("ERROR: Input is not a directory ...");
            return;
        }

        // Use static variables to share the argument to Code.java
        outputMeasurementPath = args[1];
        projectName = args[2];
        commit_hash = args[3];

        // reading Java preferences for current iteration
        Preferences prefs = Preferences.userRoot();
        currentIteration = prefs.getInt(CURRENT_ROUND, -1);
        File pomFile = new File(mvnProjDir.getAbsolutePath() + "/pom.xml");
        Model pomModel = null;
        try {
            pomModel = getModel(pomFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        boolean isJUnit5 = pomModel.getDependencies().stream().map(dependency -> dependency.getGroupId())
                .anyMatch(d -> d.equals("org.junit.jupiter"));

        // Travers the directory tree with a visitor to modify the test code
        TestDirectoryVisitor testDirVisitor = new TestDirectoryVisitor(isJUnit5);
        try {
            Files.walkFileTree(mvnProjDir.toPath(), testDirVisitor);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Add metric dependencies to the pom (this is now done through iDFlakies
//        try {
//            addMetricDepsToPom(pomModel, pomFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static Model getModel(File pomFile) throws IOException, XmlPullParserException {
        Reader reader = new FileReader(pomFile);
        MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
        Model model = xpp3Reader.read(reader);
        return model;
    }

    @Deprecated
    private static void addMetricDepsToPom(Model model, File pomFile) throws IOException {
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

        List<Dependency> dependencyList = new ArrayList<>(model.getDependencies());
        dependencyList.add(metricsCore);
        dependencyList.add(metricsJvm);

        model.setDependencies(dependencyList);

        MavenXpp3Writer xpp3Writer = new MavenXpp3Writer();

        xpp3Writer.write(new FileWriter(pomFile), model);

        reader.close();
    }
}
