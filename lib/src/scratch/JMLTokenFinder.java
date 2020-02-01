package jml.annotation;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (1/24/20)
 */
public class JMLTokenFinder {
    private List<Token> tokens = new ArrayList<>();

    public void enable(JavaFileObject sourceFile) {
        try (var reader = sourceFile.openReader(true)) {
            JmlFinder lexer = new JmlFinder(CharStreams.fromReader(reader,
                    sourceFile.getName()));
            tokens.addAll(lexer.getAllTokens());
            for (Token t : tokens) {
                System.out.format("%5d %-20s : %s\n",
                        t.getLine(),
                        lexer.getVocabulary().getSymbolicName(t.getType()),
                        t.getText()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
