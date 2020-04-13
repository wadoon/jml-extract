package jml;

import jml.impl.SimpleJmlDetection;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.opentest4j.AssertionFailedError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (4/10/20)
 */
public class TestJmlParser {
    @TestFactory
    public List<DynamicTest> createTestCases() throws IOException {
        InputStream in = getClass().getResourceAsStream("/jmlComments.txt");
        Assertions.assertNotNull(in, "Could not find 'jmlComments.txt' via classpath");
        List<DynamicTest> list = new LinkedList<>();

        StringBuilder comment = new StringBuilder();
        String tmp;
        int cnt = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            while ((tmp = br.readLine()) != null) {
                if (tmp.startsWith("/*@")) {
                    comment = new StringBuilder();
                }

                comment.append(tmp).append("\n");

                if (tmp.endsWith(("*/"))) {
                    String finalComment = comment.toString();
                    list.add(DynamicTest.dynamicTest("parse_" + (cnt), () -> test(finalComment)));
                    list.add(DynamicTest.dynamicTest("type_" + (cnt++), () -> testType(finalComment)));
                }
            }
        }
        return list;
    }

    SimpleJmlDetection jmlDetection = new SimpleJmlDetection();

    public void testType(String jmlComment) {
        Assertions.assertTrue(jmlDetection.isJmlComment(jmlComment));
        Assertions.assertNotNull(jmlDetection.getType(jmlComment));
    }


    public void test(String jmlComment) {
        try {
            KeyJmlLexer lexer = new KeyJmlLexer(CharStreams.fromString(jmlComment));
            KeyJmlParser parser = new KeyJmlParser(new CommonTokenStream(lexer));
            KeyJmlParser.JmlAnyContext context = parser.jmlAny();
            Assertions.assertEquals(0, parser.getNumberOfSyntaxErrors());
            Assertions.assertEquals(jmlComment.replaceAll("\\s", ""), context.getText());
        } catch (AssertionFailedError e) {
            System.out.println(jmlComment);
            debugLexer(jmlComment);
            throw e;
        }
    }

    private void debugLexer(String comment) {
        {
            KeyJmlLexer lexer = new KeyJmlLexer(CharStreams.fromString(comment));
            Token cur = null;
            do {
                cur = lexer.nextToken();
                System.out.printf("%20s;%3d [%10s] : %s\n", lexer.getVocabulary().getDisplayName(cur.getType()),
                        cur.getType(),
                        lexer.getModeNames()[lexer._mode],
                        cur.getText());
            } while (cur.getType() != -1);
        }
        {
            KeyJmlLexer lexer = new KeyJmlLexer(CharStreams.fromString(comment));
            KeyJmlParser parser = new KeyJmlParser(new CommonTokenStream(lexer));
            KeyJmlParser.JmlAnyContext context = parser.jmlAny();
        }
    }
}
