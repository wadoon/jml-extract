package jml.impl;

import jml.JmlCommentType;
import jml.KeyJmlLexer;
import jml.services.IJmlDetection;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * @author Alexander Weigl
 * @version 1 (1/31/20)
 */
public class SimpleJmlDetection implements IJmlDetection {
    private static final Pattern JML_COMMENT_PATTERN
            = Pattern.compile("^\\s*(//|/[*])(([-+]\\w+)*)[@].*", Pattern.DOTALL);

    @Override
    public boolean isJmlComment(@NotNull String comment) {
        return getAnnotationKeys(comment) != null;
    }

    /**
     * @param comment
     * @return null if the given comment is non-jml, else a set with the annotation keys are returned.
     */
    @Override
    public Set<String> getAnnotationKeys(@NotNull String comment) {
        var m = JML_COMMENT_PATTERN.matcher(comment);
        if (m.matches()) {
            if (m.groupCount() >= 3 && !m.group(2).isBlank()) {
                Set<String> s = new TreeSet<>();
                var annotations = m.group(2);
                Pattern.compile("(?=[+-])").splitAsStream(annotations)
                        .filter(it -> !it.isBlank())
                        .forEach(s::add);
                return s;
            }
            return Collections.emptySet();
        }
        return null;
    }

    @Override
    public JmlCommentType getType(String comment) {
        KeyJmlLexer lexer = new KeyJmlLexer(CharStreams.fromString(comment));
        boolean onlyModifier = true;
        Token tok;
        do {
            tok = lexer.nextToken();
            switch (tok.getType()) {
                //Ignore case
                case KeyJmlLexer.WS:
                case KeyJmlLexer.WS_CONTRACT:
                case KeyJmlLexer.WS_CONTRACT_IGNORE:
                case KeyJmlLexer.JML_START:
                    break;

                //CONTRACT
                case KeyJmlLexer.BEHAVIOR:
                case KeyJmlLexer.NORMAL_BEHAVIOR:
                case KeyJmlLexer.EXCEPTIONAL_BEHAVIOR:
                case KeyJmlLexer.MODEL_BEHAVIOR:
                    return JmlCommentType.METHOD_CONTRACT;
                case KeyJmlLexer.BREAK_BEHAVIOR:
                case KeyJmlLexer.CONTINUE_BEHAVIOR:
                case KeyJmlLexer.RETURN_BEHAVIOR:
                    return JmlCommentType.BLOCK_CONTRACT;

                case KeyJmlLexer.ASSERT:
                    return JmlCommentType.ASSERT;

                case KeyJmlLexer.MODEL:
                case KeyJmlLexer.MODEL_METHOD_AXIOM:
                    return JmlCommentType.MODEL;

                case KeyJmlLexer.INVARIANT:
                case KeyJmlLexer.CONSTRAINT:
                case KeyJmlLexer.INITIALLY:
                case KeyJmlLexer.AXIOM:
                    return JmlCommentType.CLASS_INVARIANT;

                case KeyJmlLexer.GHOST:
                    return JmlCommentType.GHOST_FIELD;
                case KeyJmlLexer.LOOP_INVARIANT:
                    return JmlCommentType.LOOP_INVARIANT;

                case KeyJmlLexer.SET:
                    return JmlCommentType.GHOST_SET;

                case KeyJmlLexer.PACKAGE:
                case KeyJmlLexer.PUBLIC:
                case KeyJmlLexer.PRIVATE:
                case KeyJmlLexer.PROTECTED:
                case KeyJmlLexer.STATIC:
                case KeyJmlLexer.NON_NULL:
                case KeyJmlLexer.NULLABLE:
                case KeyJmlLexer.PURE:
                case KeyJmlLexer.STRICTLY_PURE:
                case KeyJmlLexer.ABSTRACT:
                case KeyJmlLexer.UNREACHABLE:
                case KeyJmlLexer.HELPER:
                case KeyJmlLexer.NULLABLE_BY_DEFAULT:
                case KeyJmlLexer.INSTANCE:
                case KeyJmlLexer.NO_STATE:
                case KeyJmlLexer.TWO_STATE:
                    break;
                default:
                    onlyModifier = false;
            }
        } while (tok.getType() != Token.EOF);

        if (onlyModifier)
            return JmlCommentType.MODIFIER;

        return null;
    }
}
