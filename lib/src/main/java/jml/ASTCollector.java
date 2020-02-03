package jml;

import lombok.Getter;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.IBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Weigl
 * @version 1 (2/2/20)
 */
class ASTCollector extends FileASTRequestor {
    @Getter
    private final Map<String, CompilationUnit> compiledUnits = new HashMap<>();

    @Getter
    private final Map<String, List<IBinding>> bindings = new HashMap<>();

    @Override
    public void acceptAST(String sourceFilePath, CompilationUnit ast) {
        compiledUnits.put(sourceFilePath, ast);
    }

    @Override
    public void acceptBinding(String bindingKey, IBinding binding) {
        System.out.format("%s : %s\n", bindingKey, binding);
    }
}
