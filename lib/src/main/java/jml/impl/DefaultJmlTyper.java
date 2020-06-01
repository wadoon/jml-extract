package jml.impl;

import jml.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.eclipse.jdt.core.dom.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        Map<String, ITypeBinding> types = findTypes(typedUnit, buf.variableNames);
        Map<ParserRuleContext, ITypeBinding> ctxTypes = new HashMap<>();
        buf.variableNames.forEach((k, v) ->
                ctxTypes.put(k, types.get(v)));

        comments.forEach(it -> it.setExprTypes(ctxTypes));

        ctxTypes.forEach((ctx, t) -> {
            System.out.printf("%20s ==> %s\n",
                    ctx.getText(), t == null ? null : t.getQualifiedName());
        });

    }

    private Map<String, ITypeBinding> findTypes(CompilationUnit typedUnit,
                                                HashMap<ParserRuleContext, String> variableNames) {
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
                        addInScope(comment.getContext());
                    }


                    if (isMetaComment(comment.getType(), JmlComment.KIND_INVARIANT)) {
                        buffer.append("public boolean __inv_").append(counter.getAndIncrement()).append("_()");
                        addNewBlock(comment.getContext());
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
                        addNewBlock(comment.getContext());
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
                        addNewBlock(comment.getContext());
                    }
                }
            }
        }

        private final JmlToJavaTypesPrinter printer = new JmlToJavaTypesPrinter();

        private void addInScope(ParserRuleContext context) {
            buffer.append("// ").append(context.getText());
            if (additionalDeclarations != null) {
                buffer.append('\n').append(additionalDeclarations);
                additionalDeclarations = "";
            }
            context.accept(printer);
        }

        private void addNewBlock(ParserRuleContext context) {
            printIndent();
            buffer.append("{");
            indent++;
            addInScope(context);
            indent--;
            buffer.append('\n');
            printIndent();
            buffer.append("}\n");
        }

        private final StringBuffer buf = buffer;
        private final HashMap<ParserRuleContext, String> variableNames = new HashMap<>();
        private final AtomicInteger genSymCounter = counter;

        private class JmlToJavaTypesPrinter extends KeyJmlParserBaseVisitor<String> {
            private String gensym(ParserRuleContext ctx) {
                if (variableNames.containsKey(ctx)) {
                    return variableNames.get(ctx);
                }
                String name = "__gensym_" + (genSymCounter.getAndIncrement()) + "_";
                variableNames.put(ctx, name);
                return name;
            }

            private String assign(ParserRuleContext ctx, String expr) {
                String name = gensym(ctx);
                buf.append('\n');
                printIndent();
                buf.append("final var ").append(name).append(" = ").append(expr).append(";");
                return name;
            }

            @SuppressWarnings("unchecked")
            private <T> T accept(ParserRuleContext ctx) {
                return (T) ctx.accept(this);
            }

            @SuppressWarnings("unchecked")
            private <T> List<T> mapOf(List<? extends ParserRuleContext> exprs) {
                return exprs.stream().map(it -> (T) accept(it)).collect(Collectors.toList());
            }

            @Override
            public String visitErrorNode(ErrorNode node) {
                return "/*error*/";
            }

            @Override
            protected String defaultResult() {
                return "/*default*/";
            }

            @Override
            public String visitJmlAny(KeyJmlParser.JmlAnyContext ctx) {
                super.visitJmlAny(ctx);
                return "";
            }

            @Override
            public String visitJmlContract(KeyJmlParser.JmlContractContext ctx) {
                super.visitJmlContract(ctx);
                return "";
            }

            @Override
            public String visitJmlClassElem(KeyJmlParser.JmlClassElemContext ctx) {
                super.visitJmlClassElem(ctx);
                return "";
            }

            @Override
            public String visitJmlModifier(KeyJmlParser.JmlModifierContext ctx) {
                super.visitJmlModifier(ctx);
                return "";
            }

            @Override
            public String visitJmlBlockCntr(KeyJmlParser.JmlBlockCntrContext ctx) {
                return super.visitJmlBlockCntr(ctx);
            }

            @Override
            public String visitQuantifiedExpr(KeyJmlParser.QuantifiedExprContext ctx) {
                return super.visitQuantifiedExpr(ctx);
            }

            @Override
            public String visitLabeledExpr(KeyJmlParser.LabeledExprContext ctx) {
                return accept(ctx.expr());
            }

            @Override
            public String visitExprComprehension(KeyJmlParser.ExprComprehensionContext ctx) {
                return super.visitExprComprehension(ctx);
            }

            @Override
            public String visitExprShifts(KeyJmlParser.ExprShiftsContext ctx) {
                String op = "/*n/a*/";
                if (ctx.GT().size() == 2) {
                    op = ">>";
                }
                if (ctx.GT().size() == 3) {
                    op = ">>>";
                }
                if (ctx.LT().size() == 2) {
                    op = "<<";
                }
                return assignBinary(ctx, ctx.expr(0), op, ctx.expr(1));
            }

            private String assignBinary(ParserRuleContext ctx,
                                        ParserRuleContext left, String operator, ParserRuleContext right) {
                return assign(ctx, String.format("%s %s %s", accept(left), operator, accept(right)));
            }

            @Override
            public String visitExprFunction(KeyJmlParser.ExprFunctionContext ctx) {
                List<String> sub = mapOf(ctx.exprs().expr());
                String args = String.join(", ", sub);
                return assign(ctx, ctx.expr() + "(" + args + ")");
            }


            @Override
            public String visitExprDivisions(KeyJmlParser.ExprDivisionsContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "/", ctx.expr(1));
            }

            @Override
            public String visitExprAntivalence(KeyJmlParser.ExprAntivalenceContext ctx) {
                return assign(ctx,
                        enforceBool(ctx.expr(0)) + "==" +
                                enforceBool(ctx.expr(1)));
            }

            private String enforceBool(KeyJmlParser.ExprContext expr) {
                return "Enforce.bool(" + accept(expr) + ")";
            }

            @Override
            public String visitExprLineOperators(KeyJmlParser.ExprLineOperatorsContext ctx) {
                return assignBinary(ctx, ctx.expr(0), ctx.op.getText(), ctx.expr(1));
            }

            @Override
            public String visitExprBinaryNegate(KeyJmlParser.ExprBinaryNegateContext ctx) {
                return assign(ctx, "~" + accept(ctx.expr()));
            }

            @Override
            public String visitExprImplicationLeft(KeyJmlParser.ExprImplicationLeftContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "&&", ctx.expr(1));
            }

            @Override
            public String visitExprBinaryOr(KeyJmlParser.ExprBinaryOrContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "||", ctx.expr(1));
            }

            @Override
            public String visitIdentifier(KeyJmlParser.IdentifierContext ctx) {
                return assign(ctx, ctx.getText());
            }

            @Override
            public String visitThis_(KeyJmlParser.This_Context ctx) {
                return assign(ctx, ctx.getText());
            }

            @Override
            public String visitSuper_(KeyJmlParser.Super_Context ctx) {
                return super.visitSuper_(ctx);
            }

            @Override
            public String visitExprMultiplication(KeyJmlParser.ExprMultiplicationContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "*", ctx.expr(1));
            }

            @Override
            public String visitExprArryLocSet(KeyJmlParser.ExprArryLocSetContext ctx) {
                return super.visitExprArryLocSet(ctx);
            }

            @Override
            public String visitAccess(KeyJmlParser.AccessContext ctx) {
                return super.visitAccess(ctx);
            }

            @Override
            public String visitExprSubSequence(KeyJmlParser.ExprSubSequenceContext ctx) {
                return super.visitExprSubSequence(ctx);
            }

            @Override
            public String visitExprEqualities(KeyJmlParser.ExprEqualitiesContext ctx) {
                return assign(ctx,
                        enforceBool(ctx.expr(0)) + "==" +
                                enforceBool(ctx.expr(1)));
            }

            @Override
            public String visitExprBinaryXor(KeyJmlParser.ExprBinaryXorContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "^", ctx.expr(1));
            }

            @Override
            public String visitExprImplicationRight(KeyJmlParser.ExprImplicationRightContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "||", ctx.expr(1));
            }

            @Override
            public String visitExprBinaryAnd(KeyJmlParser.ExprBinaryAndContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "&&", ctx.expr(1));

            }

            @Override
            public String visitExprLogicalOr(KeyJmlParser.ExprLogicalOrContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "||", ctx.expr(1));
            }

            @Override
            public String visitExprLogicalNegate(KeyJmlParser.ExprLogicalNegateContext ctx) {
                return assign(ctx, "!" + ctx.expr());
            }

            @Override
            public String visitExprArrayAccess(KeyJmlParser.ExprArrayAccessContext ctx) {
                return super.visitExprArrayAccess(ctx);
            }

            @Override
            public String visitExprUnaryMinus(KeyJmlParser.ExprUnaryMinusContext ctx) {
                return assign(ctx, "-" + ctx.expr());
            }

            @Override
            public String visitExprNew(KeyJmlParser.ExprNewContext ctx) {
                List<String> sub = mapOf(ctx.exprs().expr());
                String args = String.join(", ", sub);
                String name = ctx.id().stream()
                        .map(RuleContext::getText)
                        .collect(Collectors.joining("."));
                return assign(ctx, "new " + name + "(" + args + ")");
            }

            @Override
            public String visitExprSuper(KeyJmlParser.ExprSuperContext ctx) {
                return assign(ctx, ctx.getText());
            }

            @Override
            public String visitExprCast(KeyJmlParser.ExprCastContext ctx) {
                return assign(ctx, "(" + ctx.typeType().getText() + ") " + accept(ctx.expr()));
            }

            @Override
            public String visitLocAll(KeyJmlParser.LocAllContext ctx) {
                return super.visitLocAll(ctx);
            }

            @Override
            public String visitExprLogicalAnd(KeyJmlParser.ExprLogicalAndContext ctx) {
                return assignBinary(ctx, ctx.expr(0), "||", ctx.expr(1));
            }

            @Override
            public String visitExprRelational(KeyJmlParser.ExprRelationalContext ctx) {
                return assignBinary(ctx, ctx.expr(0), ctx.op.getText(), ctx.expr(1));
            }

            @Override
            public String visitExprParens(KeyJmlParser.ExprParensContext ctx) {
                return assign(ctx, accept(ctx.expr()));
            }

            @Override
            public String visitExprEquivalence(KeyJmlParser.ExprEquivalenceContext ctx) {
                return assign(ctx,
                        enforceBool(ctx.expr(0)) + "==" +
                                enforceBool(ctx.expr(1)));
            }

            @Override
            public String visitExprTernary(KeyJmlParser.ExprTernaryContext ctx) {
                return assign(ctx, accept(ctx.expr(0)) + "?" + accept(ctx.expr(1)) + ":" + accept(ctx.expr(2)));
            }

            @Override
            public String visitLiteral(KeyJmlParser.LiteralContext ctx) {
                return assign(ctx, ctx.getText());
            }

            @Override
            public String visitClassRef(KeyJmlParser.ClassRefContext ctx) {
                return super.visitClassRef(ctx);
            }

            @Override
            public String visitConstructorCall(KeyJmlParser.ConstructorCallContext ctx) {
                return super.visitConstructorCall(ctx);
            }

            @Override
            public String visitMethodReference(KeyJmlParser.MethodReferenceContext ctx) {
                return super.visitMethodReference(ctx);
            }

            @Override
            public String visitJmlTypeType(KeyJmlParser.JmlTypeTypeContext ctx) {
                return super.visitJmlTypeType(ctx);
            }
        }
    }
}
