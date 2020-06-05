package jml.impl;

import jml.ASTProperties;
import jml.JmlComment;
import jml.JmlProject;
import org.eclipse.jdt.core.dom.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Alexander Weigl
 * @version 1 (4/12/20)
 */
public class DefaultJmlTyper {
    private final JmlProject project;

    public DefaultJmlTyper(JmlProject project) {
        this.project = project;
    }

    public void inferTypes(CompilationUnit unit) {
        List<JmlComment> comments = ASTProperties.getJmlComments(unit);
        Jml2JavaInserter buf = insertJml2JavaType(unit);
        System.out.println(buf.getResult());

        //TODO find the appropriate filename
        CompilationUnit typedUnit = project.compileUnit("../example/JMLTestT.java",
                buf.getResult().toCharArray());

        //TODO Handle error reported in typedUnit.getMessages()
        // and translate them back to the JmlComment
        for (Message message : typedUnit.getMessages()) {
            System.out.printf("%d:%d %s\n",
                    typedUnit.getLineNumber(message.getStartPosition()),
                    typedUnit.getColumnNumber(message.getStartPosition()),
                    message.getMessage());
        }
        //Map<String, ITypeBinding> types = findTypes(typedUnit, buf.variableNames);
        //Map<Object, ITypeBinding> ctxTypes = new HashMap<>();
        /*buf.variableNames.forEach((k, v) ->
                ctxTypes.put(k, types.get(v)));

        comments.forEach(it -> it.setExprTypes(ctxTypes));

        ctxTypes.forEach((ctx, t) -> {
            System.out.printf("%20s ==> %s\n",
                    ctx.getText(), t == null ? null : t.getQualifiedName());
        });
        */
    }

    private Map<String, ITypeBinding> findTypes(CompilationUnit typedUnit,
                                                HashMap<Object, String> variableNames) {
        AssignmentFinder visitor = new AssignmentFinder(new HashSet<>(variableNames.values()));
        typedUnit.accept(visitor);
        return visitor.types;
    }

    private Jml2JavaInserter insertJml2JavaType(CompilationUnit unit) {
        Jml2JavaInserter visitor = new Jml2JavaInserter();
        unit.accept(visitor);
        return visitor;
    }

    private final AtomicInteger counter = new AtomicInteger();

    private static class AssignmentFinder extends ASTVisitor {
        private final Set<String> names;
        final Map<String, ITypeBinding> types = new HashMap<>();

        public AssignmentFinder(Set<String> names) {
            this.names = names;
        }

        @Override
        public boolean visit(Block node) {
            for (Object it : node.statements()) {
                ((ASTNode) it).accept(this);
            }
            return true;
        }

        @Override
        public boolean visit(VariableDeclarationFragment node) {
            String lhs = node.getName().toString();
            if (names.contains(lhs)) {
                ITypeBinding type = node.getInitializer().resolveTypeBinding();
                types.put(lhs, type);
            }
            return super.visit(node);
        }
    }

    private class Jml2JavaInserter extends NaiveASTFlattener0 {
        private ASTNode contractNode;
        private String additionalDeclarations = "";


        @Override
        public boolean visit(Block node) {
            this.buffer.append("{\n");//$NON-NLS-1$
            this.indent++;
            if (contractNode != null)
                handleJmlContracts(contractNode);
            else
                handleJmlContracts(node); //block contracts
            contractNode = null;
            for (Iterator it = node.statements().iterator(); it.hasNext(); ) {
                Statement s = (Statement) it.next();
                s.accept(this);
            }
            this.indent--;
            printIndent();
            this.buffer.append("}\n");//$NON-NLS-1$
            return false;
        }

        @Override
        public boolean visit(MethodDeclaration node) {
            if (contractNode != null)
                addNewClassBlock(contractNode);
            contractNode = node;

            String retType = node.getReturnType2().resolveBinding().getQualifiedName();
            if (!retType.equals("void")) {
                String init = "0";
                if (!node.getReturnType2().isPrimitiveType()) {
                    init = "null";
                } else {
                    //TODO handle primitive cases and set a correct initial value
                }
                additionalDeclarations = retType + " __return = " + init + ";";
            }

            return super.visit(node);
        }

        @Override
        public boolean visit(FieldDeclaration node) {
            if (contractNode != null)
                addNewClassBlock(contractNode);
            return super.visit(node);
        }

        @Override
        public boolean visit(EnhancedForStatement node) {
            contractNode = node;
            return super.visit(node);
        }

        @Override
        public boolean visit(ForStatement node) {
            contractNode = node;
            return super.visit(node);
        }

        @Override
        public boolean visit(DoStatement node) {
            contractNode = node;
            return super.visit(node);
        }

        @Override
        public boolean visit(WhileStatement node) {
            contractNode = node;
            return super.visit(node);
        }


        @Override
        public boolean visit(CompilationUnit node) {
            buffer.append("class Enforce { public static boolean bool(boolean a) { return a; }}");
            return super.visit(node);
        }

        @Override
        public boolean visit(TypeDeclaration node) {
            if (contractNode != null)
                addNewClassBlock(contractNode);
            contractNode = node;
            return super.visit(node);
        }

        @Override
        public void preVisit(ASTNode node) {
            if (node instanceof Statement) {
                handleJmlStatements((Statement) node);
            }
            super.preVisit(node);
        }


        private void addNewClassBlock(ASTNode node) {
            if (contractNode == node) {
                contractNode = null;
            }
            List<JmlComment> comments = ASTProperties.getReferencedJmlComments(node, false);
            if (comments != null) {
                for (JmlComment comment : comments) {

                    if (isMetaComment(comment.getType(), JmlComment.KIND_FIELD)) {
                        //addInScope(comment.getContext());
                    }


                    if (isMetaComment(comment.getType(), JmlComment.KIND_INVARIANT)) {
                        buffer.append("public boolean __inv_").append(counter.getAndIncrement()).append("_()");
                        //addNewBlock(comment.getContext());
                    }
                }
            }
        }

        private boolean isMetaComment(int type, int kindInvariant) {
            return ((type >> 8) & kindInvariant) == 0;
        }

        private void handleJmlContracts(ASTNode node) {
            List<JmlComment> comments = ASTProperties.getReferencedJmlComments(node, false);
            if (comments != null) {
                for (JmlComment comment : comments) {
                    if (isMetaComment(comment.getType(), JmlComment.KIND_CONTRACT)) {
                        //addNewBlock(comment.getContext());
                    }
                }
            }
        }

        private void handleJmlStatements(Statement node) {
            List<JmlComment> comments = ASTProperties.getReferencedJmlComments(node, false);
            if (comments != null) {
                for (JmlComment comment : comments) {
                    if (comment.getType() == JmlComment.TYPE_ASSERT
                            || comment.getType() == JmlComment.TYPE_ASSUME
                            || comment.getType() == JmlComment.TYPE_GHOST_SET) {
                        //addNewBlock(comment.getContext());
                    }
                }
            }
        }

        //private final JmlToJavaTypesPrinter printer = new JmlToJavaTypesPrinter();

        /*private void addInScope(ParserRuleContext context) {
            buffer.append("// ").append(context.getText());
            if (additionalDeclarations != null) {
                buffer.append('\n').append(additionalDeclarations);
                additionalDeclarations = "";
            }
            context.accept(printer);
        }*/

        /*private void addNewBlock(ParserRuleContext context) {
            printIndent();
            buffer.append("{");
            indent++;
            addInScope(context);
            indent--;
            buffer.append('\n');
            printIndent();
            buffer.append("}\n");
        }
*/
        private final StringBuffer buf = buffer;
        //      private final HashMap<ParserRuleContext, String> variableNames = new HashMap<>();
        //    private final AtomicInteger genSymCounter = counter;

    }
}
