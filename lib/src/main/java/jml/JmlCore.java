package jml;

import jml.impl.JmlAttacher;
import jml.impl.JmlCommentRepository;
import jml.impl.SimpleJmlDetection;
import jml.services.IJmlAttacher;
import jml.services.IJmlCommentRepositoryFactory;
import jml.services.IJmlDetection;
import lombok.SneakyThrows;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.internal.compiler.batch.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (1/30/20)
 */
public class JmlCore {
    static boolean initialised = false;
    static Lookup defaultServices = new Lookup();

    static {
        defaultServices.register(new SimpleJmlDetection(), IJmlDetection.class);
        defaultServices.register(new JmlAttacher(), IJmlAttacher.class);
        defaultServices.register(JmlCommentRepository::new, IJmlCommentRepositoryFactory.class);
    }

    public static void init() {
        if (!initialised) {
            initialised = true;
        }
    }

    public static JmlProject createProject(String version) {
        return new JmlProject(createParser(version));
    }

    public static ASTParser createParser(String version) {
        ASTParser parser = ASTParser.newParser(AST.JLS13);
        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(version, options);
        parser.setCompilerOptions(options);
        parser.setResolveBindings(true);
        parser.setEnvironment(null, null, null, true);
        return parser;
    }

    public static JmlProject createProject() {
        return createProject(JavaCore.VERSION_1_8);
    }

    public static String getVersion() {
        InputStream in = JmlCore.class.getResourceAsStream("/jml-extract.version");
        if (in == null) return "n/a";
        try {
            return JmlCore.readString(in);
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }


    static String readString(String fileName) throws IOException {
        byte[] b = Files.readAllBytes(Paths.get(fileName));
        return new String(b, StandardCharsets.UTF_8);
    }

    static String readString(InputStream in) {
        StringWriter writer = new StringWriter();
        try (final InputStreamReader r = new InputStreamReader(in)) {
            char b[] = new char[4 * 1024];
            int len = -1;
            while ((len = r.read(b)) > 0) {
                writer.write(b, 0, len);
            }
        } catch (IOException ignored) {
        }
        return writer.toString();
    }

    public static String getEclipseVersion() {
        Main m = new Main(new PrintWriter(System.out),
                new PrintWriter(System.out), false,
                new HashMap<>(), null);
        final String version = m.bind("misc.version", //$NON-NLS-1$
                new String[]{
                        m.bind("compiler.name"), //$NON-NLS-1$
                        m.bind("compiler.version"), //$NON-NLS-1$
                        m.bind("compiler.copyright") //$NON-NLS-1$
                }
        );
        return version;
    }

    static List<Path> getFiles(String[] paths) {
        return Arrays.stream(paths).flatMap(JmlCore::getFiles).collect(Collectors.toList());
    }

    @SneakyThrows
    static Stream<Path> getFiles(String path) {
        Path p = Paths.get(path);
        if (Files.isDirectory(p))
            return Files.walk(p).filter(it -> !Files.isDirectory(it));
        else
            return Stream.of(p);
    }
}
