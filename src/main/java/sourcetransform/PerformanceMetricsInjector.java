package sourcetransform;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class PerformanceMetricsInjector {
    private File file;
    private CompilationUnit cu;

    public PerformanceMetricsInjector(File file) throws FileNotFoundException {
        this.file = file;
        cu = StaticJavaParser.parse(this.file);
    }

    public void injectMeasurementCode() {
        // import libraries
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

        // change method body with a visitor
        TestCaseVisitor tcv = new TestCaseVisitor();
        tcv.visit(cu, null);
    }

    public void writeToFile() throws IOException {
        byte[] bytes = cu.toString().getBytes();
        Files.write(file.toPath(), bytes);
    }

    public void writeToConsole() {
        System.out.println(cu.toString());
    }

}

/*
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import net.bytebuddy.asm.Advice;
import java.io.FileWriter;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;

 */


