package jml.services;

import jml.JmlCommentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * @author Alexander Weigl
 * @version 1 (1/31/20)
 */
public interface IJmlDetection {
    /**
     * Checks whether a given String is a JML comment. This check should be simple and fast, and does not need to decide
     * whether the comment is valid.
     * Refer to the manual: http://www.eecs.ucf.edu/~leavens/JML/jmlrefman/jmlrefman_4.html#SEC25
     *
     * @param comment a non-null string
     * @return
     */
    boolean isJmlComment(@NotNull String comment);

    /**
     * Extracts the comment annotation given in a JML comment.
     * <p>
     * Annotations are the identifier (prefix with "+" or "-") between the comment start and the "@". They are used
     * for restriction certain JML comments to certain features or tools.
     *
     * @param comment non-null string
     * @return null if the given comment is not a JML comment, otherwise the set contains the identifier incl.
     * their prefix. Empty set signals a valid JML comment, without any annotation.
     */
    @Nullable Set<String> getAnnotationKeys(@NotNull String comment);

    /**
     * Determines the type of a comment. Needed for a sensful attaching to {@link org.eclipse.jdt.core.dom.ASTNode}s.
     *
     * @param comment
     * @return
     */
    @Nullable JmlCommentType getType(String comment);
}
