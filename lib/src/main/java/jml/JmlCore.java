package jml;

import jml.impl.JmlAttacher;
import jml.impl.JmlCommentRepository;
import jml.impl.SimpleJmlDetection;
import jml.services.IJmlAttacher;
import jml.services.IJmlCommentRepositoryFactory;
import jml.services.IJmlDetection;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import java.util.Map;

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
            Map<String, String> options = JavaCore.getOptions();
            JavaCore.setComplianceOptions(JavaCore.VERSION_13, options);
        }
    }

    public static JmlProject createProject() {
        init();
        ASTParser parser = ASTParser.newParser(AST.JLS13);
        parser.setCompilerOptions(JavaCore.getOptions());
        parser.setResolveBindings(true);
        return new JmlProject(parser);
    }
}
