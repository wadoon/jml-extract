package jml;

import jml.impl.SimpleJmlCommentPrinter;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Test {
    @org.junit.jupiter.api.Test
    void a() {
        ASTParser parser = JmlCore.createParser(new HashMap<>());
        parser.createASTs(
                new String[]{"../example/Abc.java"}, new String[]{"utf-8"}, new String[]{"bind"}, new FileASTRequestor() {
                    @Override
                    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
                        for (IProblem problem : ast.getProblems())
                            System.out.format("[%s] %s file://%s %d\n",
                                    problem.isError() ? "ERR " : problem.isInfo() ? "INFO" : "WARN",
                                    problem.toString(),
                                    new String(problem.getOriginatingFileName()),
                                    problem.getSourceLineNumber()
                            );
                        try (PrintWriter out = new PrintWriter(System.out)) {
                            JmlProject p = JmlCore.createProject();
                            p.dumpJson(out, Stream.of(ast));
                        }
                    }
                }, new NullProgressMonitor() {
                    @Override
                    public void beginTask(String name, int totalWork) {
                        System.out.println(name);
                    }
                });
    }

    @org.junit.jupiter.api.Test
    void b() throws IOException {
        JmlProject parser = JmlCore.createProject(JavaCore.VERSION_1_8);
        /*parser.parser.setResolveBindings(true);
        parser.parser.setBindingsRecovery(true);
        parser.parser.setStatementsRecovery(true);*/
        CompilationUnit ast = parser.compileUnit("../example/Abc.java");
        for (IProblem problem : ast.getProblems())
            System.out.format("[%s] %s file://%s %d\n",
                    problem.isError() ? "ERR " : problem.isInfo() ? "INFO" : "WARN",
                    problem.toString(),
                    new String(problem.getOriginatingFileName()),
                    problem.getSourceLineNumber()
            );
        try (PrintWriter out = new PrintWriter(System.out)) {
            parser.dumpJson(out, Stream.of(ast));
        }
    }

    @org.junit.jupiter.api.Test
    void c() {
        ASTParser parser = JmlCore.createParser(new HashMap<>());
        parser.setEnvironment(null, null, null, true);
        parser.setSource("public class Test {Integer i = 2;}".toCharArray());
        parser.setUnitName("Test");
        ASTNode node = parser.createAST(null);
        CompilationUnit ast = (CompilationUnit) node.getRoot();
        for (IProblem problem : ast.getProblems())
            System.out.format("[%s] %s file://%s %d\n",
                    problem.isError() ? "ERR " : problem.isInfo() ? "INFO" : "WARN",
                    problem.toString(),
                    new String(problem.getOriginatingFileName()),
                    problem.getSourceLineNumber()
            );
        try (PrintWriter out = new PrintWriter(System.out)) {
            JmlProject p = JmlCore.createProject();
            p.dumpJson(out, Stream.of(ast));
        }
    }


    @org.junit.jupiter.api.Test
    public void test() throws IOException {
        JmlProject p = JmlCore.createProject();
        p.setEnvironment("../example");
        p.setJmlAstCreationEnabled(true);
        Collection<CompilationUnit> cus = p.compileFiles("../example");
        for (CompilationUnit root : cus)
            for (IProblem problem : root.getProblems())
                System.out.format("[%s] %s file://%s %d\n",
                        problem.isError() ? "ERR " : problem.isInfo() ? "INFO" : "WARN",
                        problem.toString(),
                        new String(problem.getOriginatingFileName()),
                        problem.getSourceLineNumber()
                );
        for (CompilationUnit unit : cus) {
            List<JmlComment> comments = ASTProperties.getJmlComments(unit);
            PrettyPrinter pp = new PrettyPrinter(comments, new SimpleJmlCommentPrinter());
            unit.accept(pp);
            System.out.println(pp.getResult());
        }
        try (FileWriter out = new FileWriter("blubber.json")) {
            p.dumpJson(out);
        }
    }

    @org.junit.jupiter.api.Test
    public void testExpr() throws IOException {
        JmlProject p = JmlCore.createProject();
        p.setEmptyEnvironment();
        PartialAst<Expression> e = p.compileExpression("2+'a'/2");
        System.out.println(e);
        System.out.println(e.getClass());
        try (Writer out = new PrintWriter(System.out)) {
            if (e.getResult() != null) {
                p.dumpJson(out, Stream.of(e.getResult()));
            } else {
                for (Object a : e.getContext().getProblems()) {
                    System.err.println(a);
                }
            }
        }
    }


    @org.junit.jupiter.api.Test
    public void testStmt() throws IOException {
        JmlProject p = JmlCore.createProject();
        p.setEmptyEnvironment();
        PartialAst<Block> e = p.compileStatements("List<Integer> seq = null;");
        final CompilationUnit root = e.getContext();
        for (IProblem problem : root.getProblems()) {
            System.out.println(problem);
        }
//        p.parser.createBindings(new IJavaElement[]{root.getJavaElement()}, null);
        System.out.println(e);
        System.out.println(e.getClass());
        try (Writer out = new PrintWriter(System.out)) {
            p.dumpJson(out, Stream.of(e.getResult()));
        }
    }
}