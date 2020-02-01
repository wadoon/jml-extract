package jml.services;

import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * @author Alexander Weigl
 * @version 1 (1/31/20)
 */
public interface IJmlAnnotationProcessor {
    void process(CompilationUnit ast);
}
