package sourcetransform;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class TestCaseVisitor extends ModifierVisitor<Void> {

    @Override
    public Visitable visit(MethodDeclaration md, Void arg) {
        super.visit(md, arg);
        addBefore(md);
        addAfter(md);
        return md;
    }

    private void addBefore(MethodDeclaration md) {

        NodeList<Statement> oldStmts = md.getBody().get().getStatements();

        md.setBody(new BlockStmt().setStatements(
                new NodeList<Statement>(
                        StaticJavaParser.parseStatement("MetricRegistry reg = new MetricRegistry();"),
                        StaticJavaParser.parseStatement("MemoryUsageGaugeSet mGs = new MemoryUsageGaugeSet();"),
                        StaticJavaParser.parseStatement("ThreadStateGaugeSet tGs = new ThreadStatesGaugeSet();"),
                        StaticJavaParser.parseStatement("ClassLoadingGaugeSet cGs = new ClassLoadingGaugeSet();"),
                        StaticJavaParser.parseStatement("JvmAttributeGaugeSet jvmGs = new JvmAttributeGaugeSet();"),
                        StaticJavaParser.parseStatement("GarbageCollectorMetricSet gMs = new GarbageCollectorMetricSet();"),
                        StaticJavaParser.parseStatement("reg.registerAll(mGs);"),
                        StaticJavaParser.parseStatement("reg.registerAll(tGs);"),
                        StaticJavaParser.parseStatement("reg.registerAll(cGs);"),
                        StaticJavaParser.parseStatement("reg.registerAll(jvmGs);"),
                        StaticJavaParser.parseStatement("reg.registerAll(gMs);"),
                        StaticJavaParser.parseStatement("StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();"),
                        StaticJavaParser.parseStatement("StackTraceElement e = stacktrace[1];"),
                        StaticJavaParser.parseStatement("String methodName = e.getMethodName();"),
                        StaticJavaParser.parseStatement("String className = MethodHandles.lookup().lookupClass().getSimpleName();"),
                        StaticJavaParser.parseStatement("String packageName = MethodHandles.lookup().lookupClass().getPackage().getName();"),
                        StaticJavaParser.parseStatement("String output = \"\\n\" + packageName + \".\" + className + \".\" + methodName;"),
                        StaticJavaParser.parseStatement("identifier = output;"),
                        StaticJavaParser.parseStatement("output = output + \",before\";"),
                        StaticJavaParser.parseStatement("Iterator<String> iterator = reg.getGauges().keySet().iterator();"),
                        new WhileStmt().setCondition(StaticJavaParser.parseExpression("iterator.hasNext()")).setBody(
                                new BlockStmt().setStatements(new NodeList<Statement>(
                                        StaticJavaParser.parseStatement("String key   = iterator.next();"),
                                        StaticJavaParser.parseStatement("String value = reg.getGauges().get(key).getValue().toString();"),
                                        StaticJavaParser.parseStatement("output = output + \",\" + value;")
                                ))
                        ),
                        new TryStmt().setTryBlock(new BlockStmt().setStatements(new NodeList<Statement>(
                                StaticJavaParser.parseStatement("FileWriter csvWriter = new FileWriter(\"/home/christian/Desktop/data/test.csv\", true);"),
                                StaticJavaParser.parseStatement("csvWriter.append(output);"),
                                StaticJavaParser.parseStatement("csvWriter.close();")
                        ))).setCatchClauses(new NodeList<CatchClause>(new CatchClause().setParameter(new Parameter().setType("Exception").setName("ex"))))
                        )));

        // append the old method body
        md.getBody().get().getStatements().addAll(oldStmts);
    }

    private void addAfter(MethodDeclaration md) {

        NodeList<Statement> oldStmts = md.getBody().get().getStatements();

        oldStmts.addAll(new NodeList<Statement>(
                StaticJavaParser.parseStatement("output = identifier;"),
                StaticJavaParser.parseStatement("output = output + \",after\";"),
                StaticJavaParser.parseStatement("iterator = reg.getGauges().keySet().iterator();"),
                new WhileStmt().setCondition(StaticJavaParser.parseExpression("iterator.hasNext()")).setBody(
                        new BlockStmt().setStatements(new NodeList<Statement>(
                                StaticJavaParser.parseStatement("String key   = iterator.next();"),
                                StaticJavaParser.parseStatement("String value = reg.getGauges().get(key).getValue().toString();"),
                                StaticJavaParser.parseStatement("output = output + \",\" + value;")
                        ))
                ),
                new TryStmt().setTryBlock(new BlockStmt().setStatements(new NodeList<Statement>(
                        StaticJavaParser.parseStatement("FileWriter csvWriter = new FileWriter(\"/home/christian/Desktop/data/test.csv\", true);"),
                        StaticJavaParser.parseStatement("csvWriter.append(output);"),
                        StaticJavaParser.parseStatement("csvWriter.close();")
                ))).setCatchClauses(new NodeList<CatchClause>(new CatchClause().setParameter(new Parameter().setType("Exception").setName("ex"))))
        ));

    }
}

/*
    AFTER

    // get test case meta information
    String output = identifier;
    output = output + ",after";

    Iterator<String> iterator = reg.getGauges().keySet().iterator();
    while(iterator.hasNext()) {
      String key   = iterator.next();
      String value = reg.getGauges().get(key).getValue().toString();
      output = output + "," + value;
        }

//    iterator = reg.getMetrics().keySet().iterator();
//    while(iterator.hasNext()) {
//      String key   = iterator.next();
//      String value = reg.getMetrics().get(key).toString();
//      output = output + "," + "("+key+")" + value;
//    }

        // write to file
        try {
        FileWriter csvWriter = new FileWriter("/home/christian/Desktop/data/test.csv", true);
        csvWriter.append(output);
        csvWriter.close();
        } catch (Exception ex) {}

 */




/*
BEFORE
    reg = new MetricRegistry();
    mGs = new MemoryUsageGaugeSet();
    tGs = new ThreadStatesGaugeSet();
    cGs = new ClassLoadingGaugeSet();
    jvmGs = new JvmAttributeGaugeSet();
    gMs = new GarbageCollectorMetricSet();

    reg.registerAll(mGs);
    reg.registerAll(tGs);
    reg.registerAll(cGs);
    reg.registerAll(jvmGs);
    reg.registerAll(gMs);

    // get test case meta information
    StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
    StackTraceElement e = stacktrace[1];//maybe this number needs to be corrected
    String methodName = e.getMethodName();
    String className = MethodHandles.lookup().lookupClass().getSimpleName();
    String packageName = MethodHandles.lookup().lookupClass().getPackage().getName();

    String output = "\n" + packageName + "." + className + "." + methodName;
    identifier = output;
    output = output + ",before";

    Iterator<String> iterator = reg.getGauges().keySet().iterator();
    while(iterator.hasNext()) {
      String key   = iterator.next();
      String value = reg.getGauges().get(key).getValue().toString();
      output = output + "," + value;
        }


        // write to file
        try {
        FileWriter csvWriter = new FileWriter("/home/christian/Desktop/data/test.csv", true);
        csvWriter.append(output);
        csvWriter.close();
        } catch (Exception ex) {}

*/



