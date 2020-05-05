package ch.chribir;

import org.junit.Assert;
import org.junit.Test;
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import java.io.FileWriter;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;

public class AppTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        MetricRegistry reg = new MetricRegistry();
        MemoryUsageGaugeSet mGs = new MemoryUsageGaugeSet();
        ThreadStateGaugeSet tGs = new ThreadStatesGaugeSet();
        ClassLoadingGaugeSet cGs = new ClassLoadingGaugeSet();
        JvmAttributeGaugeSet jvmGs = new JvmAttributeGaugeSet();
        GarbageCollectorMetricSet gMs = new GarbageCollectorMetricSet();
        reg.registerAll(mGs);
        reg.registerAll(tGs);
        reg.registerAll(cGs);
        reg.registerAll(jvmGs);
        reg.registerAll(gMs);
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[1];
        String methodName = e.getMethodName();
        String className = MethodHandles.lookup().lookupClass().getSimpleName();
        String packageName = MethodHandles.lookup().lookupClass().getPackage().getName();
        String output = "\n" + packageName + "." + className + "." + methodName;
        identifier = output;
        output = output + ",before";
        Iterator<String> iterator = reg.getGauges().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = reg.getGauges().get(key).getValue().toString();
            output = output + "," + value;
        }
        try {
            FileWriter csvWriter = new FileWriter("/home/christian/Desktop/data/test.csv", true);
            csvWriter.append(output);
            csvWriter.close();
        } catch (Exception ex) {
        }
        int[] myArr = { 1, 4, 2, 9 };
        Assert.assertTrue(true);
        output = identifier;
        output = output + ",after";
        iterator = reg.getGauges().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = reg.getGauges().get(key).getValue().toString();
            output = output + "," + value;
        }
        try {
            FileWriter csvWriter = new FileWriter("/home/christian/Desktop/data/test.csv", true);
            csvWriter.append(output);
            csvWriter.close();
        } catch (Exception ex) {
        }
    }

    @Test
    public void testOne() {
        MetricRegistry reg = new MetricRegistry();
        MemoryUsageGaugeSet mGs = new MemoryUsageGaugeSet();
        ThreadStateGaugeSet tGs = new ThreadStatesGaugeSet();
        ClassLoadingGaugeSet cGs = new ClassLoadingGaugeSet();
        JvmAttributeGaugeSet jvmGs = new JvmAttributeGaugeSet();
        GarbageCollectorMetricSet gMs = new GarbageCollectorMetricSet();
        reg.registerAll(mGs);
        reg.registerAll(tGs);
        reg.registerAll(cGs);
        reg.registerAll(jvmGs);
        reg.registerAll(gMs);
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[1];
        String methodName = e.getMethodName();
        String className = MethodHandles.lookup().lookupClass().getSimpleName();
        String packageName = MethodHandles.lookup().lookupClass().getPackage().getName();
        String output = "\n" + packageName + "." + className + "." + methodName;
        identifier = output;
        output = output + ",before";
        Iterator<String> iterator = reg.getGauges().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = reg.getGauges().get(key).getValue().toString();
            output = output + "," + value;
        }
        try {
            FileWriter csvWriter = new FileWriter("/home/christian/Desktop/data/test.csv", true);
            csvWriter.append(output);
            csvWriter.close();
        } catch (Exception ex) {
        }
        String[] myArr = { "hello", "world", "foo", "bar" };
        Assert.assertEquals(4, myArr.length);
        output = identifier;
        output = output + ",after";
        iterator = reg.getGauges().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = reg.getGauges().get(key).getValue().toString();
            output = output + "," + value;
        }
        try {
            FileWriter csvWriter = new FileWriter("/home/christian/Desktop/data/test.csv", true);
            csvWriter.append(output);
            csvWriter.close();
        } catch (Exception ex) {
        }
    }

    @Test
    public void testTwo() {
        MetricRegistry reg = new MetricRegistry();
        MemoryUsageGaugeSet mGs = new MemoryUsageGaugeSet();
        ThreadStateGaugeSet tGs = new ThreadStatesGaugeSet();
        ClassLoadingGaugeSet cGs = new ClassLoadingGaugeSet();
        JvmAttributeGaugeSet jvmGs = new JvmAttributeGaugeSet();
        GarbageCollectorMetricSet gMs = new GarbageCollectorMetricSet();
        reg.registerAll(mGs);
        reg.registerAll(tGs);
        reg.registerAll(cGs);
        reg.registerAll(jvmGs);
        reg.registerAll(gMs);
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[1];
        String methodName = e.getMethodName();
        String className = MethodHandles.lookup().lookupClass().getSimpleName();
        String packageName = MethodHandles.lookup().lookupClass().getPackage().getName();
        String output = "\n" + packageName + "." + className + "." + methodName;
        identifier = output;
        output = output + ",before";
        Iterator<String> iterator = reg.getGauges().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = reg.getGauges().get(key).getValue().toString();
            output = output + "," + value;
        }
        try {
            FileWriter csvWriter = new FileWriter("/home/christian/Desktop/data/test.csv", true);
            csvWriter.append(output);
            csvWriter.close();
        } catch (Exception ex) {
        }
        Assert.assertTrue(true);
        output = identifier;
        output = output + ",after";
        iterator = reg.getGauges().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = reg.getGauges().get(key).getValue().toString();
            output = output + "," + value;
        }
        try {
            FileWriter csvWriter = new FileWriter("/home/christian/Desktop/data/test.csv", true);
            csvWriter.append(output);
            csvWriter.close();
        } catch (Exception ex) {
        }
    }
}
