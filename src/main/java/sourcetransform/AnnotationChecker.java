package sourcetransform;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class AnnotationChecker extends ModifierVisitor<Void> {

    TestData testData;

    public AnnotationChecker(TestData testData) {
        super();
        this.testData = testData;
    }

    @Override
    public Visitable visit(MethodDeclaration md, Void arg) {
        super.visit(md, arg);
        if (md.getAnnotationByName("Before").isPresent() || md.getAnnotationByName("BeforeEach").isPresent()) {
            testData.hasBefore = true;
            appendBefore(md);
        } else if (md.getAnnotationByName("After").isPresent() || md.getAnnotationByName("AfterEach").isPresent()) {
            testData.hasAfter = true;
            appendAfter(md);
        } else if (md.getAnnotationByName("Test").isPresent()) {
            appendWithin(md);
        }
        return md;
    }

    private void appendBefore(MethodDeclaration md) {
        if (md.getBody().isPresent()) {
            md.getBody().get().getStatements().addAll(Code.before);
        } else {
            md.setBody(new BlockStmt().setStatements(Code.before));
        }
    }

    private void appendWithin(MethodDeclaration md) {
        if (!md.getBody().isPresent()) return;
        md.getBody().get().getStatements().addAll(0, Code.within);
    }

    private void appendAfter(MethodDeclaration md) {
        if (md.getBody().isPresent()) {
            md.getBody().get().getStatements().addAll(0, Code.after);
        } else {
            md.setBody(new BlockStmt().setStatements(Code.before));
        }
    }
}

