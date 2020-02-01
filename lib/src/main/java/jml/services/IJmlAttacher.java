package jml.services;

import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexander Weigl
 * @version 1 (1/31/20)
 */
public interface IJmlAttacher {
    void attach(CompilationUnit ast, Collection<Comment> jmlComments);
}
