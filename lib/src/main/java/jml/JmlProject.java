package jml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jml.impl.AstToMapVisitor;
import jml.services.*;
import lombok.Getter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexander Weigl
 * @version 1 (1/30/20)
 */
public class JmlProject {
    private Lookup lookup = new Lookup(JmlCore.defaultServices);
    private final ASTParser parser;
    private IProgressMonitor monitor;
    @Nullable
    private IJmlCommentRepository jmlRepo;
    private Map<String, CompilationUnit> compiledUnits = new HashMap<>();
    private int uniqueCounter;
    private boolean suppressJmlAttaching = false;
    private boolean suppressJmlAnnotationProcessing = false;

    private void registerComments(String p, CompilationUnit ast) {
        compiledUnits.put(p, ast);
        List<Comment> commentList = new ArrayList<>(ast.getCommentList().size());
        IJmlDetection detection = lookup.get(IJmlDetection.class);
        try {
            String content = Files.readString(Paths.get(p));
            for (var it : ast.getCommentList()) {
                Comment c = (Comment) it;
                String str = content.substring(c.getStartPosition(), c.getStartPosition() + c.getLength());
                if (detection.isJmlComment(str)) {
                    ASTProperties.setIsJmlComment(c, true);
                    ASTProperties.setContent(c, str);
                    final var type = detection.getType(str);
                    ASTProperties.setJmlCommentType(c, type);
                    ASTProperties.setAnnotations(c, detection.getAnnotationKeys(str));
                    commentList.add(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!suppressJmlAttaching) {
            lookup.get(IJmlAttacher.class).attach(ast, commentList);
        }

        if (!suppressJmlAnnotationProcessing) {
            lookup.lookupAll(IJmlAnnotationProcessor.class).forEach(it -> it.process(ast));
        }
    }

    JmlProject(ASTParser parser) {
        this.parser = parser;
    }

    /**
     * @see ASTParser#setEnvironment(String[], String[], String[], boolean)
     */
    public void setEnvironment(String[] classPathEntries, String[] sourceEntries,
                               String encoding, boolean includeRunningVMBootClasspath) {
        String[] encodings = new String[sourceEntries.length];
        Arrays.fill(encodings, encoding);
        for (int i = 0; i < sourceEntries.length; i++) {
            sourceEntries[i] = new File(sourceEntries[i]).getAbsolutePath();
        }
        for (int i = 0; i < classPathEntries.length; i++) {
            classPathEntries[i] = new File(classPathEntries[i]).getAbsolutePath();
        }
        parser.setEnvironment(classPathEntries, sourceEntries, encodings, includeRunningVMBootClasspath);
    }

    public void setEnvironment(String... sourceEntries) {
        setEnvironment(new String[0], sourceEntries, "utf-8", true);
    }


    public String getJmlSpecification(ASTNode node) {
        return "";//TODO getJmlRepo().get(node);
    }

    public void compileExpression(String expr) {

    }

    public void compileBody(String body) {

    }


    public Collection<CompilationUnit> compileAllIn(String folder) throws IOException {
        List<Path> col = Files.walk(Paths.get(folder))
                .filter(it -> !Files.isDirectory(it))
                .collect(Collectors.toList());
        List<String> javaFiles =
                col.stream().filter(it -> it.toString().endsWith(".java"))
                        .map(it -> it.toAbsolutePath().toString())
                        .collect(Collectors.toList());

        List<String> jmlFiles =
                col.stream().filter(it -> it.toString().endsWith(".jml"))
                        .map(it -> it.toAbsolutePath().toString())
                        .collect(Collectors.toList());

        String bindingKey = "" + (++uniqueCounter);
        String[] bindingKeys = new String[javaFiles.size()];
        String[] sourceFiles = new String[javaFiles.size()];
        String[] encodings = new String[javaFiles.size()];
        javaFiles.toArray(sourceFiles);
        Arrays.fill(bindingKeys, bindingKey);
        Arrays.fill(bindingKeys, "utf-8");
        ASTCollector collector = new ASTCollector();
        parser.createASTs(sourceFiles, encodings, bindingKeys, collector, monitor);
        var cus = collector.getCompiledUnits().values();
        collector.getCompiledUnits().forEach((this::registerComments));
        return cus;
    }


    public void setCompilerOptions(Map<String, String> options) {
        parser.setCompilerOptions(options);
    }

    public void setStatementsRecovery(boolean enabled) {
        parser.setStatementsRecovery(enabled);
    }

    public void setIgnoreMethodBodies(boolean enabled) {
        parser.setIgnoreMethodBodies(enabled);
    }

    public void dumpJson(Writer out) {
        var seq = compiledUnits.entrySet().stream().map(entry -> {
            var cu = entry.getValue();
            var v = new AstToMapVisitor();
            cu.accept(v);
            return v.root;
        }).collect(Collectors.toList());
        Gson gson = new GsonBuilder().setPrettyPrinting().disableInnerClassSerialization()
                .serializeSpecialFloatingPointValues()
                .excludeFieldsWithModifiers(Modifier.PRIVATE)
                .create();
        gson.toJson(seq, out);
    }

    IJmlCommentRepository getJmlRepo() {
        if (jmlRepo == null) {
            jmlRepo = lookup.get(IJmlCommentRepositoryFactory.class).create(lookup);
            lookup.register(jmlRepo, IJmlCommentRepository.class);
        }
        return jmlRepo;
    }

    public IProgressMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    public boolean isSuppressJmlAttaching() {
        return suppressJmlAttaching;
    }

    public void setSuppressJmlAttaching(boolean suppressJmlAttaching) {
        this.suppressJmlAttaching = suppressJmlAttaching;
    }
}

class ASTCollector extends FileASTRequestor {
    @Getter
    private final Map<String, CompilationUnit> compiledUnits = new HashMap<>();

    @Getter
    private final Map<String, List<IBinding>> bindings = new HashMap<>();

    @Override
    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
        compiledUnits.put(sourceFilePath, ast);
    }

    @Override
    public void acceptBinding(String bindingKey, IBinding binding) {
        System.out.format("%s : %s\n", bindingKey, binding);
    }
}
