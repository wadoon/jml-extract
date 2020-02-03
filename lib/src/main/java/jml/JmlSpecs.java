package jml;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (2/2/20)
 * http://www.eecs.ucf.edu/~leavens/JML/jmlrefman/jmlrefman_6.html#SEC41
 */
public class JmlSpecs {
    private Map<ParserRuleContext, Object> types = new HashMap<>();
    private List<ParserRuleContext>
            classInvariant, axioms, models, loopInvariant, commands;
    private EnumSet<JmlModifier> modifiers = EnumSet.noneOf(JmlModifier.class);

    public enum JmlModifier {
        PUBLIC, PRIVATE, PROTECTED, SPEC_PUBLIC, SPEC_PROTECTED,
        ABSRACT, STATIC, INSTANCE,
        MODEL, GHOST, PURE, HELPER,
        FINAL, SYNCHRONIZED,
        TRANSIENT, VOLATILE, NATIVE, STRICTFP,
        MONITORED, UNINITIALIZED,
        spec_java_math, spec_safe_math, spec_bigint_math,
        code_java_math, code_safe_math, code_bigint_math,
        non_null, nullable, nullable_by_default,
        code, extract,
        peer, rep, readonly;

    }
}
