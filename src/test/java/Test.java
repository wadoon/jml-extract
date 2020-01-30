import com.google.testing.compile.JavaFileObjects;
import jml.annotation.ShowProcessor;

import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

import static com.google.testing.compile.Compiler.javac;

public class Test {
    public JavaFileObject source(Class<?> clazz) {
        return JavaFileObjects.forResource(clazz.getCanonicalName()
                .replace('.', '/') + ".java");
    }

    public static JavaFileObject source(String fqn) throws IOException {
        byte[] b = Files.readAllBytes(new File("src/main/java/" +
                fqn.replace('.', '/') + ".java").toPath());
        return JavaFileObjects.forSourceLines(fqn, new String(b));
    }

    public static void main(String[] args) throws IOException {
        var compiler = ToolProvider.getSystemJavaCompiler();
        var fileManager = compiler.getStandardFileManager(null, null, null);
        File files1 = new File("src/main/java/test/JMLTest.java");
        Iterable<? extends JavaFileObject> compilationUnits1 =
                fileManager.getJavaFileObjects(files1);
        Iterable<String> options = List.of();
        var annotationProcessors = List.of(new ShowProcessor());
        var task = compiler.getTask(new PrintWriter(System.out),
                fileManager, null, options, List.of(), compilationUnits1);
        task.setProcessors(annotationProcessors);
        var r = task.call();
    }
}