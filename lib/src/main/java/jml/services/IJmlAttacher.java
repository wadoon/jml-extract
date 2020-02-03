package jml.services;

import jml.JmlComment;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Collection;

/**
 * @author Alexander Weigl
 * @version 1 (1/31/20)
 */
public interface IJmlAttacher {
    void attach(CompilationUnit ast, Collection<JmlComment> jmlComments);
}
