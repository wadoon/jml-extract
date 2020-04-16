package jml.experimental;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

/**
 * @author Alexander Weigl
 * @version 1 (4/16/20)
 */
@BuildParseTree
public class JmlPegParser extends BaseParser<Object> {
    Rule method_specification() {
        return Sequence(
                ZeroOrMore(also_keyword()),
                spec_case(),
                ZeroOrMore(also_keyword(), spec_case());
        );
    }

    private Rule spec_case() {
        return FirstOf(lightweight_spec_case(), heavyweight_spec_case());
    }

    private Rule heavyweight_spec_case() {
        return null;
    }

    private Rule lightweight_spec_case() {
        return generic_spec_case();
    }

    private Rule generic_spec_case() { return  null;}

    private Rule spec_var_decls() {
        return null;
    }

    Rule also_keyword() {
        return this.FirstOf(ALSO, FOR_EXAMPLE, IMPLIES_THAT);
    }

    private final Rule ALSO = String("ALSO");
    private final Rule FOR_EXAMPLE = String("for_example");
    private final Rule IMPLIES_THAT = String("implies_that");
}
