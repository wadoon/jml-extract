package jml.services;

import jml.JmlComment;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A service associates {@link JmlComment}s to their appropiate ASTNode
 *
 * @author Alexander Weigl
 * @version 1 (1/31/20)
 */
public interface IJmlAttacher {
    /**
     * This method should attach the {@code jmlComments}
     * to the appropriate {@link ASTNode} in {@code ast}.
     *
     * @param ast         the ast, in which the comments where found
     * @param jmlComments a list of jml comments
     * @see jml.ASTProperties#attachJmlComment(ASTNode, JmlComment)
     */
    void attach(@NotNull CompilationUnit ast,
                @NotNull Collection<JmlComment> jmlComments);
}
