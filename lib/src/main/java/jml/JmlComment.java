package jml;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.ITypeBinding;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (2/1/20)
 */
public class JmlComment {
    //region constants
    public static final int KIND_UNKNOWN = 0;
    public static final int KIND_STATEMENT = 1;
    public static final int KIND_CONTRACT = 2;
    public static final int KIND_FIELD = 3;
    public static final int KIND_METHOD = 4;
    public static final int KIND_MODIFIER = 5;
    public static final int KIND_INVARIANT = 6;

    public static final int TYPE_UNKNOWN = (KIND_UNKNOWN << 8) | 0;
    public static final int TYPE_GHOST_FIELD = (KIND_FIELD << 8) | 1;
    public static final int TYPE_MODEL_FIELD = (KIND_FIELD << 8) | 2;
    public static final int TYPE_MODEL_METHOD = (KIND_METHOD << 8) | 3;
    public static final int TYPE_CLASS_INVARIANT = (KIND_INVARIANT << 8) | 4;
    public static final int TYPE_LOOP_INVARIANT = (KIND_INVARIANT << 8) | 5;
    public static final int TYPE_BLOCK_CONTRACT = (KIND_CONTRACT << 8) | 6;
    public static final int TYPE_METHOD_CONTRACT = (KIND_CONTRACT << 8) | 7;
    public static final int TYPE_MODIFIER = (KIND_MODIFIER << 8) | 8;
    public static final int TYPE_GHOST_SET = (KIND_STATEMENT << 8) | 9;
    public static final int TYPE_ASSUME = (KIND_STATEMENT << 8) | 10;
    public static final int TYPE_ASSERT = (KIND_STATEMENT << 8) | 11;

    public static final int AT_UNKNOWN = 0;
    /**
     *
     */
    public static final int AT_NEXT_DECLARATION = 1;
    /**
     *
     */
    public static final int AT_NEXT_STATEMENT = 2;

    /**
     *
     */
    public static final int AT_NEXT_METHOD = 3;
    /**
     *
     */
    public static final int AT_NEXT_FIELD = 4;
    /**
     *
     */
    public static final int AT_NEXT_LOOP = 5;
    public static final int AT_NEXT_BLOCK = 6;
    public static final int AT_CONTAINING_TYPE = 7;
    //


    private static final String JML_TYPE = "JML_TYPE";
    private static final String JML_CONTENT = "JML_CONTENT";
    private static final String JML_ANNOTATIONS = "JML_ANNOTATION";
    private static final String JML_ENABLED = "JML_ENABLED";
    private static final String JML_EFFECTS_ON = "JML_EFFECTS_ON";
    private static final String JML_AST = "JML_AST";
    private static final String JML_AST_PARSING_ERRORS = "JML_AST_PARSING_ERRORS";
    private static final String JML_AST_TYPE_ERRORS = "JML_AST_TYPE_ERRORS";
    private static final String JML_PARSER_ERRORS = "JML_PARSER_ERRORS";
    private static final String JML_EXPR_TYPES = "JML_EXPR_TYPES";
    private static final String JML_ATTACHING_TYPE = "JML_ATTACHING_TYPE";

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
        Boolean b = (Boolean) c.getProperty(JML_ENABLED);
        return b != null && b;
    }

    public String getContent() {
        return (String) c.getProperty(JML_CONTENT);
    }

    public void setContent(String str) {
        c.setProperty(JML_CONTENT, str);
    }

    public int getType() {
        return (int) c.getProperty(JML_TYPE);
    }

    public void setType(int type) {
        c.setProperty(JML_TYPE, type);
    }

    public int getAttachingType() {
        return (int) c.getProperty(JML_ATTACHING_TYPE);
    }

    public void setAttachingType(int type) {
        c.setProperty(JML_ATTACHING_TYPE, type);
    }

    public JmlAnnotations getAnnotations() {
        return (JmlAnnotations) c.getProperty(JML_ANNOTATIONS);
    }

    public void setAnnotations(JmlAnnotations annotationKeys) {
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

    public Map<ParserRuleContext, org.eclipse.jdt.core.dom.Type> getExprTypes() {
        return (Map<ParserRuleContext, org.eclipse.jdt.core.dom.Type>) wrapped().getProperty(JML_EXPR_TYPES);
    }

    public void setExprTypes(Map<ParserRuleContext, ITypeBinding> given) {
        wrapped().setProperty(JML_EXPR_TYPES, given);
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
