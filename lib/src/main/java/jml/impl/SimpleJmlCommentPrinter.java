package jml.impl;

import jml.JmlComment;
import jml.services.JmlCommentPrinter;

/**
 * @author Alexander Weigl
 * @version 1 (4/5/20)
 */
public class SimpleJmlCommentPrinter implements JmlCommentPrinter {
    @Override
    public void print(StringBuffer buf, JmlComment comment, String indent, boolean debug) {
        if (comment != null) {
            printWithNewIndent(comment.getContent(), buf, indent);
            if (debug) {
                buf.append("\n").append(indent);
                buf.append("/*").append(comment.getAnnotations()).append("*/");
                buf.append("\n").append(indent);
                buf.append("/*").append(comment.getContext().getText()).append("*/");
                buf.append("\n").append(indent);
                buf.append("/*").append(comment.getParserErrors()).append("*/");
            }
        }
    }

    private void printWithNewIndent(String content, StringBuffer buf, String indent) {
        String nc = content.replaceAll("\n[\t ]*", indent+"  @ ");
        buf.append(indent).append(nc);
    }
}
