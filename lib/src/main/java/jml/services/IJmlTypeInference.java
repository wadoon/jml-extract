package jml.services;

import jml.JmlComment;
import jml.JmlProject;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 * @author Alexander Weigl
 * @version 1 (2/3/20)
 */
public interface IJmlTypeInference {
    /**
     * TODO
     * @param jmlProject
     * @param comment
     */
    void inferTypes(@NotNull JmlProject jmlProject,
                    @NotNull JmlComment comment);
}
