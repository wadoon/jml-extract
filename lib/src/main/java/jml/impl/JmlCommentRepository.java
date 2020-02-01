package jml.impl;

import jml.Lookup;
import jml.services.IJmlCommentRepository;
import jml.services.IJmlAstFactory;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.HashMap;

/**
 * @author Alexander Weigl
 * @version 1 (1/30/20)
 */
public class JmlCommentRepository implements IJmlCommentRepository {
    private final Lookup lookup;
    private IJmlAstFactory jmlFactory;
    private HashMap<ASTNode, Comment> assignComments = new HashMap<>();
    private HashMap<ASTNode, Object> parsedComments = new HashMap<>();

    public JmlCommentRepository(Lookup lookup) {
        this.lookup = lookup;
    }

    void register(CompilationUnit node) {
        node.getCommentList().forEach(it -> System.out.println(it));
    }

    public String get(ASTNode node) {
        return null;
    }

    public String getParsed(ASTNode node) {
        return null;
    }
}
