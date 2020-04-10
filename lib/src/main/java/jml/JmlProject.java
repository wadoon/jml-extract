package jml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jml.impl.AstSerializer;
import jml.services.*;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (1/30/20)
 */
public class JmlProject {
    private Lookup lookup = new Lookup(JmlCore.defaultServices);
    final ASTParser parser;
    private IProgressMonitor monitor;
    private Map<String, CompilationUnit> compiledUnits = new HashMap<>();

    /**
     * A counter used for creating unique names.
     */
    private int uniqueCounter;

    private String encoding = "utf-8";

    /**
     *
     */
    private boolean jmlEnabled = true;
    /**
     *
     */
    private boolean jmlAttachingEnabled = true;
    /**
     *
     */
    private boolean jmlAnnotationProcessingEnabled = true;

    /**
     *
     */
    private @NotNull AST astFactory = null;

    /**
     * Experimental
     */
    private boolean jmlAstCreationEnabled = false;
    /**
     * Experimental
     */
    private boolean jmlReAnnotationEnabled = false;
    /**
     * Experimental
     */
    private boolean jmlTypeInferenceEnabled = false;

    private void annotateWithJml(String p, CompilationUnit ast) {
        try {
            String sourceCode = JmlCore.readString(p);
            annotateWithJml(p, ast, sourceCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void annotateWithJml(String p, CompilationUnit ast, String sourceCode) {
        if (!jmlEnabled) {
            return;
        }

        compiledUnits.put(p, ast);

        List<JmlComment> commentList = new ArrayList<>(ast.getCommentList().size());
        IJmlDetection detection = lookup.get(IJmlDetection.class);
        for (Object it : ast.getCommentList()) {
            Comment c = (Comment) it;
            String str = sourceCode.substring(c.getStartPosition(), c.getStartPosition() + c.getLength());
            if (detection.isJmlComment(str)) {
                final JmlComment jc = ASTProperties.wrap(c);
                final JmlComment.Type type = detection.getType(str);
                jc.setEnabled(true);
                jc.setContent(str);
                jc.setType(type);
                jc.setAnnotations(new JmlAnnotations(detection.getAnnotationKeys(str)));
                commentList.add(jc);
            }
        }

        if (jmlAttachingEnabled) {
            lookup.get(IJmlAttacher.class).attach(ast, commentList);

            if (jmlAnnotationProcessingEnabled)
                lookup.lookupAll(IJmlAnnotationProcessor.class).forEach(it -> it.process(ast));

            if (jmlAstCreationEnabled) {
                final IJmlParser factory = lookup.get(IJmlParser.class);
                final Collection<IJmlTagger> tagger = lookup.lookupAll(IJmlTagger.class);

                for (JmlComment comment : commentList) {
                    factory.create(comment);
                    if (jmlReAnnotationEnabled) {
                        tagger.forEach(it -> it.modifyTags(comment));
                    }

                    if (jmlTypeInferenceEnabled) {
                        lookup.get(IJmlTypeInference.class).inferTypes(this, comment);
                    }
                }
            }
        }
    }

    JmlProject(AST ast, ASTParser parser) {
        astFactory = ast;
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

    public @Nullable List<JmlComment> getJmlSpecification(ASTNode node) {
        return ASTProperties.getReferencedJmlComments(node, false);
    }

    /**
     * @return
     */
    public @NotNull AST getAstFactory() {
        return astFactory;
    }
    
    public PartialAst<Expression> compileExpression(String expr) {
        String name = String.format("Expr%010d", ++uniqueCounter);
        String source = String.format("public class %s { public Object a() { return %s; } }",
                name, expr);
        parser.setUnitName(name);
        parser.setSource(source.toCharArray());
        CompilationUnit cu = (CompilationUnit) parser.createAST(monitor);
        Block body = ((TypeDeclaration) cu.types().get(0)).getMethods()[0].getBody();
        try {
            ReturnStatement s = (ReturnStatement) body.statements().get(0);
            Expression result = s.getExpression();
            this.annotateWithJml(name, cu, source);
            return new PartialAst<>(result, cu);
        } catch (IndexOutOfBoundsException e) {
            return new PartialAst<>(null, cu);
        }
    }

    public PartialAst<Block> compileStatements(String statements) {
        String name = String.format("Statements%010d", ++uniqueCounter);
        String source = String.format("public class %s { public void a() { %s } }",
                name, statements);
        parser.setUnitName(name);
        parser.setSource(source.toCharArray());
        CompilationUnit cu = (CompilationUnit) parser.createAST(monitor);
        Block body = ((TypeDeclaration) cu.types().get(0)).getMethods()[0].getBody();
        annotateWithJml(name, cu, source);
        return new PartialAst<>(body, cu);
    }

    public CompilationUnit compileUnit(String fileName) throws IOException {
        ASTCollector requestor = new ASTCollector();
        parser.createASTs(new String[]{fileName}, new String[]{encoding}, new String[]{"" + (++uniqueCounter)},
                requestor, monitor);
        return requestor.getCompiledUnits().values().iterator().next();
    }

    public CompilationUnit compileUnit(String fileName, char[] contents) {
        parser.setSource(contents);
        ASTNode node = parser.createAST(monitor);
        CompilationUnit cu = (CompilationUnit) node.getRoot();
        annotateWithJml(fileName, cu);
        return cu;
    }

    public Collection<CompilationUnit> compileFiles(String... paths) {
        List<Path> col = JmlCore.getFiles(paths);
        List<String> javaFiles =
                col.stream().filter(it -> it.toString().endsWith(".java"))
                        .map(it -> it.toAbsolutePath().toString())
                        .collect(Collectors.toList());

        List<String> jmlFiles =
                col.stream().filter(it -> it.toString().endsWith(".jml"))
                        .map(it -> it.toAbsolutePath().toString())
                        .collect(Collectors.toList());

        String bindingKey = "" + (++uniqueCounter);

        @NotNull Collection<CompilationUnit> cusJava = Collections.emptyList();
        @NotNull Collection<CompilationUnit> cusJml = Collections.emptyList();

        if (javaFiles.size() > 0) {
            setIgnoreMethodBodies(false);
            cusJava = compile0(bindingKey, javaFiles, encoding);
        }

        if (jmlFiles.size() > 0) {
            setIgnoreMethodBodies(true);
            cusJml = compile0(bindingKey, jmlFiles, encoding);
        }

        setIgnoreMethodBodies(false);
        ArrayList<CompilationUnit> cus = new ArrayList<>(cusJml.size() + cusJava.size());
        cus.addAll(cusJava);
        cus.addAll(cusJml);
        return cus;
    }

    @NotNull
    private Collection<CompilationUnit> compile0(String bindingKey, List<String> files, String encoding) {
        String[] bindingKeys = new String[files.size()];
        String[] sourceFiles = new String[files.size()];
        String[] encodings = new String[files.size()];
        files.toArray(sourceFiles);
        Arrays.fill(bindingKeys, bindingKey);
        Arrays.fill(encodings, "utf-8");

        ASTCollector collector = new ASTCollector();
        parser.createASTs(sourceFiles, encodings, bindingKeys, collector, monitor);
        Collection<CompilationUnit> cus = collector.getCompiledUnits().values();
        collector.getCompiledUnits().forEach((this::annotateWithJml));
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

    public void dumpJson(Writer out, Stream<? extends ASTNode> nodes) {
        List<Object> seq = nodes.map(cu -> {
            AstSerializer v = new AstSerializer();
            cu.accept(v);
            return v.root;
        }).collect(Collectors.toList());

        Gson gson = new GsonBuilder().setPrettyPrinting().disableInnerClassSerialization()
                .serializeSpecialFloatingPointValues()
                .excludeFieldsWithModifiers(Modifier.PRIVATE)
                .create();
        gson.toJson(seq, out);
    }

    public void dumpJson(Writer out) {
        dumpJson(out, compiledUnits.values().stream());
    }

    public IProgressMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    public boolean isJmlAttachingEnabled() {
        return jmlAttachingEnabled;
    }

    public void setJmlAttachingEnabled(boolean jmlAttachingEnabled) {
        this.jmlAttachingEnabled = jmlAttachingEnabled;
    }

    public boolean isJmlEnabled() {
        return jmlEnabled;
    }

    public void setJmlEnabled(boolean jmlEnabled) {
        this.jmlEnabled = jmlEnabled;
    }

    public boolean isJmlAnnotationProcessingEnabled() {
        return jmlAnnotationProcessingEnabled;
    }

    public void setJmlAnnotationProcessingEnabled(boolean jmlAnnotationProcessingEnabled) {
        this.jmlAnnotationProcessingEnabled = jmlAnnotationProcessingEnabled;
    }

    public boolean isJmlAstCreationEnabled() {
        return jmlAstCreationEnabled;
    }

    public void setJmlAstCreationEnabled(boolean jmlAstCreationEnabled) {
        this.jmlAstCreationEnabled = jmlAstCreationEnabled;
    }

    public boolean isJmlReAnnotationEnabled() {
        return jmlReAnnotationEnabled;
    }

    public void setJmlReAnnotationEnabled(boolean jmlReAnnotationEnabled) {
        this.jmlReAnnotationEnabled = jmlReAnnotationEnabled;
    }

    public boolean isJmlTypeInferenceEnabled() {
        return jmlTypeInferenceEnabled;
    }

    public void setJmlTypeInferenceEnabled(boolean jmlTypeInferenceEnabled) {
        this.jmlTypeInferenceEnabled = jmlTypeInferenceEnabled;
    }

    public Lookup getLookup() {
        return lookup;
    }

    public void setEmptyEnvironment() {
        setEnvironment(new String[0], new String[0], "utf-8", true);
    }
}