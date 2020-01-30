package jml.annotation;

import com.google.gson.Gson;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.BindingKey;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;


public class Main {
    private static IProgressMonitor monitor = new NullProgressMonitor() {
        @Override
        public void beginTask(String name, int totalWork) {
            System.out.format("%s : %d%n", name, totalWork);
        }

        @Override
        public void done() {
            System.out.println("done");
        }
    };

    public static void main(String[] args) throws JavaModelException {
        char[] source = "public class Test {}".toCharArray();
        ASTParser parser = ASTParser.newParser(AST.JLS13);
        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_13, options);
        parser.setCompilerOptions(options);
        parser.setResolveBindings(true);
        String[] classpathEntries = new String[0];
        String[] sourcePathEntries = new String[]{new File("example").getAbsolutePath()};
        parser.setEnvironment(
                classpathEntries,
                sourcePathEntries,
                new String[]{"utf-8"},
                true);

        String[] sourceFilePaths = new String[]{"example/Test.java"};
        String[] encodings = new String[]{"utf-8"};
        String[] bindingKeys = new String[]{"astring"};
        FileASTRequestor requestor = new FileASTRequestor() {
            @Override
            public void acceptAST(String sourceFilePath, CompilationUnit ast) {
                System.out.println(ast);
                Gson gson = new Gson();
                gson.toJson(ast, new PrintWriter(System.out));
            }

            @Override
            public void acceptBinding(String bindingKey, IBinding binding) {
                System.out.format("%s : %s\n", bindingKey, binding);
            }
        };
        parser.createASTs(sourceFilePaths,
                encodings, bindingKeys, requestor, monitor);
    }
}
