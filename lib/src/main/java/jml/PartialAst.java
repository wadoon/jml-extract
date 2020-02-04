package jml;

import lombok.Getter;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * @author Alexander Weigl
 * @version 1 (2/4/20)
 */
public class PartialAst<T extends ASTNode> {
    @Getter
    private final T result;

    @Getter
    private final CompilationUnit context;

    public PartialAst(T result, CompilationUnit context) {
        this.result = result;
        this.context = context;
    }
}
