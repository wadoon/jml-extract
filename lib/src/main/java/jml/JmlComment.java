package jml;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Comment;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (2/1/20)
 */
public class JmlComment {
    private static final String JML_TYPE = "JML_TYPE";
    private static final String JML_CONTENT = "JML_CONTENT";
    private static final String JML_ANNOTATIONS = "JML_ANNOTATION";
    private static final String JML_ENABLED = "JML_ENABLED";
    private static final String JML_EFFECTS_ON = "JML_EFFECTS_ON";
    private static final String JML_AST = "JML_AST";
    private static final String JML_AST_PARSING_ERRORS = "JML_AST_PARSING_ERRORS";
    private static final String JML_AST_TYPE_ERRORS = "JML_AST_TYPE_ERRORS";


    private static final String JML_PARSER_ERRORS = "JML_PARSER_ERRORS";

    private final Comment c;

    /**
     * @param wrapped
     * @see ASTProperties#wrap
     */
    JmlComment(Comment wrapped) {
        this.c = wrapped;
    }

    public void setEnabled(boolean b) {
        c.setProperty(JML_ENABLED, b);
    }

    public boolean isEnabled() {
        return (boolean) c.getProperty(JML_ENABLED);
    }

    public String getContent() {
        return (String) c.getProperty(JML_CONTENT);
    }

    public void setContent(String str) {
        c.setProperty(JML_CONTENT, str);
    }

    public Type getType() {
        return (Type) c.getProperty(JML_TYPE);
    }

    public void setType(Type type) {
        c.setProperty(JML_TYPE, type);
    }

    public JmlAnnotation getAnnotations() {
        return (JmlAnnotation) c.getProperty(JML_ANNOTATIONS);
    }

    public void setAnnotations(JmlAnnotation annotationKeys) {
        c.setProperty(JML_ANNOTATIONS, annotationKeys);
    }

    public int getStartPosition() {
        return wrapped().getStartPosition();
    }

    public int getLength() {
        return wrapped().getLength();
    }

    public Comment wrapped() {
        return c;
    }

    public enum Type {
        UNKNOWN,
        GHOST_FIELD,
        MODEL,
        CLASS_INVARIANT,
        LOOP_INVARIANT,
        BLOCK_CONTRACT,
        METHOD_CONTRACT,
        MODIFIER,
        GHOST_SET,
        ASSUME,
        ASSERT;
    }

    public List<JmlProblem> getTypeErrors() {
        return (List<JmlProblem>) c.getProperty(JML_AST_TYPE_ERRORS);
    }

    public void setTypeErrors(List<JmlProblem> seq) {
        c.setProperty(JML_AST_TYPE_ERRORS, seq);
    }

    public ParserRuleContext getContext() {
        return (ParserRuleContext) c.getProperty(JML_AST);
    }

    public void setContext(ParserRuleContext ctx) {
        c.setProperty(JML_AST, ctx);
    }


    public ASTNode getEffectedNode() {
        return (ASTNode) c.getProperty(JML_EFFECTS_ON);
    }

    public void setEffectedNode(ASTNode node) {
        c.setProperty(JML_EFFECTS_ON, node);
    }

    public List<JmlProblem> getParserErrors() {
        return getParserErrors(false);
    }

    public List<JmlProblem> getParserErrors(boolean create) {
        if (create && c.getProperty(JML_PARSER_ERRORS) == null)
            setParserErrors(new LinkedList<>());
        return (List<JmlProblem>) c.getProperty(JML_PARSER_ERRORS);
    }


    public void setParserErrors(List<JmlProblem> problems) {
        c.setProperty(JML_PARSER_ERRORS, problems);
    }


}
