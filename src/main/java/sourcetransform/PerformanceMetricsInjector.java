package sourcetransform;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;


public class PerformanceMetricsInjector {
    private File file;
    private CompilationUnit cu;

    public static boolean hasBefore = false;
    public static boolean hasAfter = false;
    public static boolean extendsTestCase = false;

    public PerformanceMetricsInjector(File file) throws FileNotFoundException {
        this.file = file;
        cu = StaticJavaParser.parse(this.file);
    }

    public void injectMeasurementCode() {
        // import libraries
        importLibraries();

        // fields for gauges
        addFields();

        // check existing before/after methods
        AnnotatedMethodVisitor annotatedMethodVisitor = new AnnotatedMethodVisitor();
        annotatedMethodVisitor.visit(cu, null);

        // create fixtures
        if (!hasBefore) createBefore(cu);
        if (!hasAfter) createAfter(cu);
    }

    private void addFields() {

        if (!cu.getPrimaryType().isPresent()) return;
        cu.getPrimaryType().get().addField("MetricRegistry", "chribirreg", Modifier.Keyword.PRIVATE);
        cu.getPrimaryType().get().addField("MemoryUsageGaugeSet", "chribirmGs", Modifier.Keyword.PRIVATE);
        cu.getPrimaryType().get().addField("ThreadStatesGaugeSet", "chribirtGs", Modifier.Keyword.PRIVATE);
        cu.getPrimaryType().get().addField("ClassLoadingGaugeSet", "chribircGs", Modifier.Keyword.PRIVATE);
        cu.getPrimaryType().get().addField("JvmAttributeGaugeSet", "chribirjvmGs", Modifier.Keyword.PRIVATE);
        cu.getPrimaryType().get().addField("GarbageCollectorMetricSet", "chribirgMs", Modifier.Keyword.PRIVATE);
        cu.getPrimaryType().get().addField("String", "chribiridentifier", Modifier.Keyword.PRIVATE);
        cu.getPrimaryType().get().addField("String", "chribiroutputBefore", Modifier.Keyword.PRIVATE);
        cu.getPrimaryType().get().addField("String", "chribiroutputAfter", Modifier.Keyword.PRIVATE);

    }

    private void importLibraries() {
        cu.addImport("com.codahale.metrics.ConsoleReporter", false, false);
        cu.addImport("com.codahale.metrics.CsvReporter", false, false);
        cu.addImport("com.codahale.metrics.JvmAttributeGaugeSet", false, false);
        cu.addImport("com.codahale.metrics.MetricRegistry", false, false);
        cu.addImport("com.codahale.metrics.jvm.ClassLoadingGaugeSet", false, false);
        cu.addImport("com.codahale.metrics.jvm.GarbageCollectorMetricSet", false, false);
        cu.addImport("com.codahale.metrics.jvm.MemoryUsageGaugeSet", false, false);
        cu.addImport("com.codahale.metrics.jvm.ThreadStatesGaugeSet", false, false);
        cu.addImport("java.io.FileWriter", false, false);
        cu.addImport("java.lang.invoke.MethodHandles", false, false);
        cu.addImport("java.util.Iterator", false, false);
        cu.addImport("org.junit.Before", false, false);
        cu.addImport("org.junit.BeforeClass", false, false);
        cu.addImport("org.junit.After", false, false);
    }

    private void createBefore(CompilationUnit cu) {
        if (!cu.getPrimaryType().isPresent()) return;
        cu.getPrimaryType().get().addMethod("myBefore", Modifier.Keyword.PUBLIC)
                .setAnnotations(new NodeList<AnnotationExpr>(new MarkerAnnotationExpr().setName("Before")))
                .setType("void")
                .setBody(new BlockStmt().setStatements(Code.before));
    }

    private void createAfter(CompilationUnit cu) {
        if (!cu.getPrimaryType().isPresent()) return;
        cu.getPrimaryType().get().addMethod("myAfter", Modifier.Keyword.PUBLIC)
                .setAnnotations(new NodeList<AnnotationExpr>(new MarkerAnnotationExpr().setName("After")))
                .setType("void")
                .setBody(new BlockStmt().setStatements(Code.after));
    }

    public void writeToFile() throws IOException {
        byte[] bytes = cu.toString().getBytes();
        Files.write(file.toPath(), bytes);
    }

    public void writeToConsole() {
        System.out.println(cu.toString());
    }

}

