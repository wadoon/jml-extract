package jml.services;

import jml.JmlComment;
import jml.JmlSpecs;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Weigl
 * @version 1 (1/30/20)
 */
public interface IJmlParser {
    /**
     * sets {@link JmlComment#setContext}
     * Problems during parsing should be reported to {@link JmlComment#getParserErrors()}
     *
     * @param comment
     */
    void create(@NotNull JmlComment comment);

    default void update(@NotNull ParserRuleContext ctx, @NotNull JmlSpecs target) {
        //unused at the moment
    }
}
