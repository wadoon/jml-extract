package jml;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Comment;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Alexander Weigl
 * @version 1 (2/1/20)
 */
public class ASTProperties {
    private static final String JML_TYPE = "afsafsdfsaf";
    private static final String JML_CONTENT = "dsfsdf";
    private static final String JML_ANNOTATIONS = "dsafsafss";
    private static final String JML_COMMENTS = "dsaokfjdsaf";
    private static final String JML_ENABLED = "sdfdsajfdsafs";

    public static JmlCommentType getJmlCommentType(Comment c) {
        return (JmlCommentType) c.getProperty(JML_TYPE);
    }

    public static String getContent(Comment c) {
        return (String) c.getProperty(JML_CONTENT);
    }


    public static void setIsJmlComment(Comment c, boolean b) {
        c.setProperty(JML_ENABLED, b);
    }

    public static void setContent(Comment c, String str) {
        c.setProperty(JML_CONTENT, str);
    }

    public static void setJmlCommentType(Comment c, JmlCommentType type) {
        c.setProperty(JML_TYPE, type);
    }

    public static void setAnnotations(Comment c, Set<String> annotationKeys) {
        c.setProperty(JML_ANNOTATIONS, annotationKeys);

    }

    public static List<Comment> getReferencedJmlComments(ASTNode node, boolean create) {
        List<Comment> comments = (List<Comment>) node.getProperty(JML_COMMENTS);
        if (comments == null && create) {
            comments = new LinkedList<>();
            node.setProperty(JML_COMMENTS, comments);
        }
        return comments;
    }


    public static void attachJmlComment(ASTNode empty, Comment comment) {
        getReferencedJmlComments(empty, true).add(comment);
    }

    public static Set<String> getAnnotations(Comment c) {
        return (Set<String>) c.getProperty(JML_ANNOTATIONS);
    }
}
