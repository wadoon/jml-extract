package jml;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Comment;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (2/1/20)
 */
public class ASTProperties {
    private static final String JML_COMMENTS = "JML_COMMENTS";

    public static @NotNull JmlComment wrap(@NotNull Comment comment) {
        return new JmlComment(comment);
    }

    public static List<JmlComment> getReferencedJmlComments(ASTNode node, boolean create) {
        List<JmlComment> comments = (List<JmlComment>) node.getProperty(JML_COMMENTS);
        if (comments == null && create) {
            comments = new LinkedList<>();
            node.setProperty(JML_COMMENTS, comments);
        }
        return comments;
    }

    public static void attachJmlComment(ASTNode node, JmlComment comment) {
        getReferencedJmlComments(node, true).add(comment);
        comment.setEffectedNode(node);
    }
}
