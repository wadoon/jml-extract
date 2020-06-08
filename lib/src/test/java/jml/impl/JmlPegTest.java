package jml.impl;

import jml.JmlAst;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Alexander Weigl
 * @version 1 (6/6/20)
 */
class JmlPegTest {
    private String[] data = new String[]{
            "(\\forall int i; i<2; true)",
            "(\\forall int i; i<2; (\\forall int i; i<2; true))",
            "1+1+1",
            "1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1",

    };


    private JmlPeg parser;

    @BeforeEach
    void init() {
        if (parser == null)
            parser = Parboiled.createParser(JmlPeg.class);
    }

    @TestFactory
    public Stream<DynamicTest> generate() {
        return Arrays.stream(data).map(
                it -> DynamicTest.dynamicTest(it, () -> testExpr1(it)));
    }

    public void testExpr1(String input) {
        Rule exprRule = parser.expr();
        final TracingParseRunner<JmlAst> runner = new TracingParseRunner<>(exprRule);
        ParsingResult<JmlAst> result = runner.run(input);
        System.out.println(ParseTreeUtils.printNodeTree(result));
        Assertions.assertFalse(result.hasErrors());
    }

}