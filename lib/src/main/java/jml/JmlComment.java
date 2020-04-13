package jml;

import lombok.Getter;
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

    public Type getType() {
        return (Type) c.getProperty(JML_TYPE);
    }

    public void setType(Type type) {
        c.setProperty(JML_TYPE, type);
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


    public enum MetaType {
        UNKNOWN, STATEMENT, CONTRACT, FIELD, METHOD, MODIFIER, INVARIANT;
    }

    public enum AttacherType {
        UNKNOWN,
        NEXT_DECLARATION,
        NEXT_STATEMENT,
        NEXT_METHOD,
        NEXT_FIELD,
        NEXT_LOOP,
        NEXT_BLOCK,
        CONTAINING_TYPE;
    }


    public enum Type {
        UNKNOWN,
        GHOST_FIELD(AttacherType.NEXT_LOOP, MetaType.CONTRACT),
        MODEL_FIELD(AttacherType.CONTAINING_TYPE, MetaType.FIELD),
        MODEL_METHOD(AttacherType.CONTAINING_TYPE, MetaType.METHOD),
        CLASS_INVARIANT(AttacherType.CONTAINING_TYPE, MetaType.INVARIANT),
        LOOP_INVARIANT(AttacherType.NEXT_LOOP, MetaType.CONTRACT),
        BLOCK_CONTRACT(AttacherType.NEXT_BLOCK, MetaType.CONTRACT),
        METHOD_CONTRACT(AttacherType.NEXT_METHOD, MetaType.CONTRACT),
        MODIFIER(AttacherType.NEXT_DECLARATION, MetaType.MODIFIER),
        GHOST_SET(AttacherType.NEXT_STATEMENT, MetaType.STATEMENT),
        ASSUME(AttacherType.NEXT_STATEMENT, MetaType.STATEMENT),
        ASSERT(AttacherType.NEXT_STATEMENT, MetaType.STATEMENT);

        @Getter
        final MetaType metaType;
        @Getter
        final AttacherType attacherType;

        Type() {
            this(AttacherType.UNKNOWN, MetaType.UNKNOWN);
        }

        Type(AttacherType attacherType, MetaType metaType) {
            this.attacherType = attacherType;
            this.metaType = metaType;
        }
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
