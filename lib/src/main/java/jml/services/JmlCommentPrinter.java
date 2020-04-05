package jml.services;

import jml.JmlComment;

/**
 * @author Alexander Weigl
 * @version 1 (4/5/20)
 */
public interface JmlCommentPrinter {
    void print(StringBuffer buf, JmlComment comment, String indent, boolean debug);
}
