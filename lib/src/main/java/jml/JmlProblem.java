package jml;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Alexander Weigl
 * @version 1 (2/3/20)
 */
@Immutable
@Data
@AllArgsConstructor
public class JmlProblem {
    private char[] fileName;
    private int id;
    private int startPosition;
    private int endPosition;
    private int line;
    public int column;
    public int severity;
    private String[] arguments;
    private String message;
}
