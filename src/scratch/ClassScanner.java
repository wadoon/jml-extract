package jml.annotation;

import com.sun.source.tree.*;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

/**
 * @author Alexander Weigl
 * @version 1 (1/24/20)
 */
public class ClassScanner extends TreePathScanner<Object, Trees> {
    private JMLTokenFinder jmlTokenFinder = new JMLTokenFinder();

    @Override
    public Object visitCompilationUnit(CompilationUnitTree node, Trees trees) {
        System.out.println(node);
        var sourceFile = node.getSourceFile();
        jmlTokenFinder.enable(sourceFile);
        scan(node.getTypeDecls(), trees);
        return super.visitCompilationUnit(node, trees);
    }

    @Override
    public Object visitPackage(PackageTree node, Trees trees) {
        return super.visitPackage(node, trees);
    }

    @Override
    public Object visitImport(ImportTree node, Trees trees) {
        return super.visitImport(node, trees);
    }

    @Override
    public Object visitClass(ClassTree node, Trees trees) {
        return super.visitClass(node, trees);
    }

    @Override
    public Object visitMethod(MethodTree node, Trees trees) {
        return super.visitMethod(node, trees);
    }

    @Override
    public Object visitVariable(VariableTree node, Trees trees) {
        return super.visitVariable(node, trees);
    }

    @Override
    public Object visitEmptyStatement(EmptyStatementTree node, Trees trees) {
        return super.visitEmptyStatement(node, trees);
    }

    @Override
    public Object visitBlock(BlockTree node, Trees trees) {
        return super.visitBlock(node, trees);
    }

    @Override
    public Object visitDoWhileLoop(DoWhileLoopTree node, Trees trees) {
        return super.visitDoWhileLoop(node, trees);
    }

    @Override
    public Object visitWhileLoop(WhileLoopTree node, Trees trees) {
        return super.visitWhileLoop(node, trees);
    }

    @Override
    public Object visitForLoop(ForLoopTree node, Trees trees) {
        return super.visitForLoop(node, trees);
    }

    @Override
    public Object visitEnhancedForLoop(EnhancedForLoopTree node, Trees trees) {
        return super.visitEnhancedForLoop(node, trees);
    }

    @Override
    public Object visitLabeledStatement(LabeledStatementTree node, Trees trees) {
        return super.visitLabeledStatement(node, trees);
    }

    @Override
    public Object visitSwitch(SwitchTree node, Trees trees) {
        return super.visitSwitch(node, trees);
    }

    @Override
    public Object visitCase(CaseTree node, Trees trees) {
        return super.visitCase(node, trees);
    }

    @Override
    public Object visitSynchronized(SynchronizedTree node, Trees trees) {
        return super.visitSynchronized(node, trees);
    }

    @Override
    public Object visitTry(TryTree node, Trees trees) {
        return super.visitTry(node, trees);
    }

    @Override
    public Object visitCatch(CatchTree node, Trees trees) {
        return super.visitCatch(node, trees);
    }

    @Override
    public Object visitConditionalExpression(ConditionalExpressionTree node, Trees trees) {
        return super.visitConditionalExpression(node, trees);
    }

    @Override
    public Object visitIf(IfTree node, Trees trees) {
        return super.visitIf(node, trees);
    }

    @Override
    public Object visitExpressionStatement(ExpressionStatementTree node, Trees trees) {
        return super.visitExpressionStatement(node, trees);
    }

    @Override
    public Object visitBreak(BreakTree node, Trees trees) {
        return super.visitBreak(node, trees);
    }

    @Override
    public Object visitContinue(ContinueTree node, Trees trees) {
        return super.visitContinue(node, trees);
    }

    @Override
    public Object visitReturn(ReturnTree node, Trees trees) {
        return super.visitReturn(node, trees);
    }

    @Override
    public Object visitThrow(ThrowTree node, Trees trees) {
        return super.visitThrow(node, trees);
    }

    @Override
    public Object visitAssert(AssertTree node, Trees trees) {
        return super.visitAssert(node, trees);
    }

    @Override
    public Object visitMethodInvocation(MethodInvocationTree node, Trees trees) {
        return super.visitMethodInvocation(node, trees);
    }

    @Override
    public Object visitNewClass(NewClassTree node, Trees trees) {
        return super.visitNewClass(node, trees);
    }

    @Override
    public Object visitNewArray(NewArrayTree node, Trees trees) {
        return super.visitNewArray(node, trees);
    }

    @Override
    public Object visitLambdaExpression(LambdaExpressionTree node, Trees trees) {
        return super.visitLambdaExpression(node, trees);
    }

    @Override
    public Object visitParenthesized(ParenthesizedTree node, Trees trees) {
        return super.visitParenthesized(node, trees);
    }

    @Override
    public Object visitAssignment(AssignmentTree node, Trees trees) {
        return super.visitAssignment(node, trees);
    }

    @Override
    public Object visitCompoundAssignment(CompoundAssignmentTree node, Trees trees) {
        return super.visitCompoundAssignment(node, trees);
    }

    @Override
    public Object visitUnary(UnaryTree node, Trees trees) {
        return super.visitUnary(node, trees);
    }

    @Override
    public Object visitBinary(BinaryTree node, Trees trees) {
        return super.visitBinary(node, trees);
    }

    @Override
    public Object visitTypeCast(TypeCastTree node, Trees trees) {
        return super.visitTypeCast(node, trees);
    }

    @Override
    public Object visitInstanceOf(InstanceOfTree node, Trees trees) {
        return super.visitInstanceOf(node, trees);
    }

    @Override
    public Object visitArrayAccess(ArrayAccessTree node, Trees trees) {
        return super.visitArrayAccess(node, trees);
    }

    @Override
    public Object visitMemberSelect(MemberSelectTree node, Trees trees) {
        return super.visitMemberSelect(node, trees);
    }

    @Override
    public Object visitMemberReference(MemberReferenceTree node, Trees trees) {
        return super.visitMemberReference(node, trees);
    }

    @Override
    public Object visitIdentifier(IdentifierTree node, Trees trees) {
        return super.visitIdentifier(node, trees);
    }

    @Override
    public Object visitLiteral(LiteralTree node, Trees trees) {
        return super.visitLiteral(node, trees);
    }

    @Override
    public Object visitPrimitiveType(PrimitiveTypeTree node, Trees trees) {
        return super.visitPrimitiveType(node, trees);
    }

    @Override
    public Object visitArrayType(ArrayTypeTree node, Trees trees) {
        return super.visitArrayType(node, trees);
    }

    @Override
    public Object visitParameterizedType(ParameterizedTypeTree node, Trees trees) {
        return super.visitParameterizedType(node, trees);
    }

    @Override
    public Object visitUnionType(UnionTypeTree node, Trees trees) {
        return super.visitUnionType(node, trees);
    }

    @Override
    public Object visitIntersectionType(IntersectionTypeTree node, Trees trees) {
        return super.visitIntersectionType(node, trees);
    }

    @Override
    public Object visitTypeParameter(TypeParameterTree node, Trees trees) {
        return super.visitTypeParameter(node, trees);
    }

    @Override
    public Object visitWildcard(WildcardTree node, Trees trees) {
        return super.visitWildcard(node, trees);
    }

    @Override
    public Object visitModifiers(ModifiersTree node, Trees trees) {
        return super.visitModifiers(node, trees);
    }

    @Override
    public Object visitAnnotation(AnnotationTree node, Trees trees) {
        return super.visitAnnotation(node, trees);
    }

    @Override
    public Object visitAnnotatedType(AnnotatedTypeTree node, Trees trees) {
        return super.visitAnnotatedType(node, trees);
    }

    @Override
    public Object visitModule(ModuleTree node, Trees trees) {
        return super.visitModule(node, trees);
    }

    @Override
    public Object visitExports(ExportsTree node, Trees trees) {
        return super.visitExports(node, trees);
    }

    @Override
    public Object visitOpens(OpensTree node, Trees trees) {
        return super.visitOpens(node, trees);
    }

    @Override
    public Object visitProvides(ProvidesTree node, Trees trees) {
        return super.visitProvides(node, trees);
    }

    @Override
    public Object visitRequires(RequiresTree node, Trees trees) {
        return super.visitRequires(node, trees);
    }

    @Override
    public Object visitUses(UsesTree node, Trees trees) {
        return super.visitUses(node, trees);
    }

    @Override
    public Object visitOther(Tree node, Trees trees) {
        return super.visitOther(node, trees);
    }

    @Override
    public Object visitErroneous(ErroneousTree node, Trees trees) {
        return super.visitErroneous(node, trees);
    }
}
