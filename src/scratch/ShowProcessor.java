package jml.annotation;

import com.sun.source.util.DocTrees;
import com.sun.source.util.Trees;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class ShowProcessor extends AbstractProcessor {
    private Trees trees;
    private PrintWriter out;
    private DocTrees treeUtils;

    public void init(ProcessingEnvironment pEnv) {
        out = new PrintWriter(System.out);
        trees = Trees.instance(pEnv);
        treeUtils = DocTrees.instance(pEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getRootElements().forEach((Element it) -> {
            var path = trees.getPath(it);
            new ClassScanner().scan(path.getParentPath(), this.trees);
        });
        return true;
    }
}
