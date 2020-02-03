package jml.services;

import jml.JmlComment;
import jml.JmlProject;

/**
 * @author Alexander Weigl
 * @version 1 (2/3/20)
 */
public interface IJmlTypeInference {
    void inferTypes(JmlProject jmlProject, JmlComment comment);
}
