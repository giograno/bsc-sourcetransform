package sourcetransform;

import com.codahale.metrics.JvmAttributeGaugeSet;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import org.junit.Test;

import java.util.Iterator;

public class MetricsPrinter {

    MetricRegistry chribirreg = new MetricRegistry();
    MemoryUsageGaugeSet chribirmGs = new MemoryUsageGaugeSet();
    ThreadStatesGaugeSet chribirtGs = new ThreadStatesGaugeSet();
    ClassLoadingGaugeSet chribircGs = new ClassLoadingGaugeSet();
    JvmAttributeGaugeSet chribirjvmGs = new JvmAttributeGaugeSet();
    GarbageCollectorMetricSet chribirgMs = new GarbageCollectorMetricSet();

    @Test
    public void printMetrics() {
        chribirreg.registerAll(chribirmGs);
        chribirreg.registerAll(chribirtGs);
        chribirreg.registerAll(chribircGs);
        chribirreg.registerAll(chribirjvmGs);
        chribirreg.registerAll(chribirgMs);

//        System.out.println(chribirreg.getMetrics());

        int counter = 0;
        Iterator<String> chribiriterator = chribirreg.getGauges().keySet().iterator();
        if (chribiriterator.hasNext()) {
            counter ++;
            String chribirkey = chribiriterator.next();
            System.out.println("Metrics is : " + chribirkey);
            String chribirvalue = chribirreg.getGauges().get(chribirkey).getValue().toString();
        }
        System.out.println("counter is " + counter);
    }
}
