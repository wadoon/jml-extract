package jml;

import jml.services.JmlCommentPrinter;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.core.dom.NaiveASTFlattener;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author Alexander Weigl
 * @version 1 (4/5/20)
 */
public class PrettyPrinter extends NaiveASTFlattener {
    @Getter
    @Setter
    private JmlCommentPrinter jmlPrinter;

    @Getter
    @Setter
    private boolean printDebug = false;

    private final PriorityQueue<JmlComment> comments;

    public PrettyPrinter(List<JmlComment> comments, JmlCommentPrinter jmlPrinter) {
        super();
        this.comments = new PriorityQueue<>(Comparator.comparingInt(JmlComment::getStartPosition));
        this.comments.addAll(comments);
        this.jmlPrinter = jmlPrinter;
    }

    protected String getCurrentIndent(int searchPos) {
        int lastNl = buffer.lastIndexOf("\n", searchPos);
        if (lastNl == -1) {
            return "";
        }

        if (lastNl == buffer.length()) {
            return getCurrentIndent(lastNl - 1);
        }

        int nonWs = 0;
        for (nonWs = lastNl; nonWs < buffer.length(); nonWs++) {
            if (!Character.isWhitespace(buffer.codePointAt(nonWs))) {
                break;
            }
        }

        if (lastNl == nonWs-1) {
            return getCurrentIndent(lastNl - 2);
        }

        return buffer.substring(lastNl, nonWs);
    }

    protected String getCurrentIndent() {
        /*int l = buffer.length();
        printIndent();
        String indent = buffer.substring(l);
        buffer.setLength(l);*/
        return getCurrentIndent(buffer.length());
    }

    @Override
    public void preVisit(ASTNode node) {
        String indent = getCurrentIndent();
        while (!comments.isEmpty()
                && node.getStartPosition() >= comments.peek().getStartPosition()) {
            jmlPrinter.print(buffer, comments.poll(), indent, printDebug);
            buffer.append("\n").append(indent);
        }
    }
}
