package jml.impl;

import jml.JmlComment;
import jml.services.IJmlParser;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Weigl
 * @version 1 (2/3/20)
 */
public class JmlParser implements IJmlParser {

    /*public KeyJmlParser createParser(String content) {
        KeyJmlLexer lexer = createLexer(content);
        KeyJmlParser parser = new KeyJmlParser(new CommonTokenStream(lexer));
        return parser;
    }*/

/*    private KeyJmlLexer createLexer(String content) {
        KeyJmlLexer lexer = new KeyJmlLexer(CharStreams.fromString(content));
        return lexer;
    }
*/

    @Override
    public void create(@NotNull JmlComment comment) {
        /*KeyJmlParser p = createParser(comment.getContent());
        final KeyJmlParser.JmlAnyContext ctx = p.jmlAny();
        if(0 != p.getNumberOfSyntaxErrors()) {
            System.out.println(comment.getContent());
        }
        comment.setContext(ctx);
        */
    }
}
