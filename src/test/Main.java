package test;


import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;

import javax.tools.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        JavacTool javac = (JavacTool) ToolProvider.getSystemJavaCompiler();
        System.out.println(javac.getClass());

        Writer out = new PrintWriter(System.out);
        DiagnosticListener<? super JavaFileObject> listener = new DiagnosticListener<JavaFileObject>() {
            @Override
            public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                try {
                    out.append(diagnostic.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        JavacFileManager fileManager = javac.getStandardFileManager(listener,
                Locale.getDefault(), Charset.defaultCharset());
        Iterable<String> options = List.of();
        Iterable<String> classes = List.of();
        Iterable<? extends JavaFileObject> compilationUnits = findFiles(fileManager, "example");
        JavacTask task = javac.getTask(out, fileManager, null, null, null, compilationUnits);
        var parsedElements = task.parse();
        var elements = task.analyze();
        for (var cu : parsedElements) {
            TreeVisitor<?, ?> visitor = new MyTreeScanner();
            cu.accept(visitor, null);
        }
        for (var clazz : elements) {
            //e.getEnclosingElement().
            for (var method : clazz.getEnclosedElements()) {
                for(var subs : method.getEnclosedElements()) {
                    var t = subs.asType();
                    System.out.println(subs);
                    System.out.println(t);
                }
            }
        }
    }

    private static Iterable<? extends JavaFileObject> findFiles(JavacFileManager fileManager,
                                                                String src) throws IOException {
        JavaFileManager.Location location = StandardLocation.SOURCE_PATH;
        try (Stream<Path> walk = Files.walk(Paths.get(src))) {
            return fileManager.getJavaFileObjectsFromPaths(
                    walk
                            .filter(it -> !Files.isDirectory(it))
                            .collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        }
        return null;
    }

    private static class MyTreeScanner extends TreeScanner<Object, Object> {
        @Override
        public Object scan(Tree tree, Object o) {
            System.out.println("scan: " + tree);
            return super.scan(tree, o);
        }
    }
        /*JavaFileManager.Location location = StandardLocation.locationFor(
                new File("src").getAbsolutePath());
        return fileManager.list(location, "", Set.of(JavaFileObject.Kind.SOURCE), true);

                            .map(it -> {
                        String name = it.toString();
                        try {
                            return fileManager.getJavaFileForInput(location,
                                    name.replace(".java", "")
                                            .replace(src + "/", "")
                                            .replace("/", "."),
                                    JavaFileObject.Kind.SOURCE);
                        } catch (IOException e) {
                            e.printStackTrace();
                            assert false;
                        }
                        return null;
                    })
         */

}
