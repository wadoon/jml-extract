package jml.impl;

import jml.KeyJmlParser;
import jml.KeyJmlParserBaseVisitor;
import lombok.var;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Alexander Weigl
 * @version 1 (2/21/20)
 */
public class JmlSerializer extends KeyJmlParserBaseVisitor<Object> {
    private final int offset;

    public JmlSerializer(int offset) {
        this.offset = offset;
    }

    @Override
    public Object visit(ParseTree tree) {
        return createMap((ParserRuleContext) tree);
    }

    @Override
    public Object visitJmlAny(KeyJmlParser.JmlAnyContext ctx) {
        return oneOf(ctx.jmlAnnotation(), ctx.jmlBlockCntr(), ctx.jmlClassElem(),
                ctx.jmlContract(), ctx.jmlModifier());
    }

    private Object oneOf(ParseTree... ctxs) {
        for (ParseTree ctx : ctxs) {
            if (ctx != null)
                return ctx.accept(this);
        }
        return null;
    }

    private Map<String, Object> createMap(ParserRuleContext ctx) {
        Map<String, Object> m = new HashMap<>(8);
        m.put("startPosition", ctx.start.getStartIndex() + offset);
        m.put("stopPosition", ctx.stop.getStopIndex() + offset);
        m.put("length", ctx.getText().length());
        m.put("type", ctx.getClass().getName());
        return m;
    }

    @Override
    public Object visitJmlContract(KeyJmlParser.JmlContractContext ctx) {
        var map = createMap(ctx);
        map.put("contracts", ctx.methodContracts().accept(this));
        return map;
    }

    @Override
    public Object visitMethodContracts(KeyJmlParser.MethodContractsContext ctx) {
        return mapOf(ctx.methodContract());
    }

    @Override
    public Object visitClause(KeyJmlParser.ClauseContext ctx) {
        return oneOf(ctx.acc(), ctx.assign(), ctx.ensures(), ctx.requires(), ctx.signals(), ctx.signalsOnly(),
                ctx.diverges(), ctx.determs(), ctx.mby());
    }

    @Override
    public Object visitEnsures(KeyJmlParser.EnsuresContext ctx) {
        var map = createMap(ctx);
        map.put("clause_type", ctx.ENSURES().getText());
        map.put("heap", mapOf(ctx.heap()));
        map.put("expr", ctx.expr().accept(this));
        return map;
    }

    @Override
    public Object visitRequires(KeyJmlParser.RequiresContext ctx) {
        var map = createMap(ctx);
        map.put("clause_type", ctx.REQUIRES().getText());
        map.put("heap", mapOf(ctx.heap()));
        map.put("expr", ctx.expr().accept(this));
        return map;
    }

    @Override
    public Object visitMby(KeyJmlParser.MbyContext ctx) {
        var map = createMap(ctx);
        map.put("clause_type", ctx.MEASURED_BY().getText());
        map.put("exprs", ctx.exprs().accept(this));
        return map;
    }

    @Override
    public Object visitSignals(KeyJmlParser.SignalsContext ctx) {
        var map = createMap(ctx);
        map.put("clause_type", ctx.SIGNALS().getText());
        map.put("exception_type", ctx.typeType().accept(this));
        map.put("expr", ctx.expr().accept(this));
        return map;
    }

    @Override
    public Object visitSignalsOnly(KeyJmlParser.SignalsOnlyContext ctx) {
        var map = createMap(ctx);
        map.put("clause_type", ctx.SIGNALS_ONLY().getText());
        map.put("exception_types", mapOf(ctx.typeType()));
        return map;
    }

    @Override
    public Object visitDiverges(KeyJmlParser.DivergesContext ctx) {
        var map = createMap(ctx);
        map.put("clause_type", ctx.DIVERGES().getText());
        map.put("expr", ctx.expr().accept(this));
        return map;
    }

    @Override
    public Object visitLiteral(KeyJmlParser.LiteralContext ctx) {
        return oneOf(ctx.boolLiteral(), ctx.floatLiteral(), ctx.nullLiteral(),
                ctx.integerLiteral(), ctx.charLiteral(), ctx.stringLiteral());
    }

    @Override
    public Object visitDeterms(KeyJmlParser.DetermsContext ctx) {
        var map = createMap(ctx);
        if (ctx.DECLASSIFIES() != null)
            map.put("clause_type", ctx.DECLASSIFIES().getText());
        if (ctx.DETERMINES() != null)
            map.put("clause_type", ctx.DECLASSIFIES().getText());
        map.put("exprs", mapOf(ctx.exprs()));
        return map;
    }

    @Override
    public Object visitHeap(KeyJmlParser.HeapContext ctx) {
        var map = createMap(ctx);
        map.put("value", ctx.IDENTIFIER().getText());
        return map;
    }

    @Override
    public Object visitAssign(KeyJmlParser.AssignContext ctx) {
        var map = createMap(ctx);
        if (ctx.ASSIGNABLE() != null)
            map.put("clause_type", ctx.ASSIGNABLE().getText());
        if (ctx.MODIFIABLE() != null)
            map.put("clause_type", ctx.MODIFIABLE().getText());
        if (ctx.MODIFIES() != null)
            map.put("clause_type", ctx.MODIFIES().getText());
        map.put("heaps", mapOf(ctx.heap()));
        map.put("exprs", mapOf(ctx.expr()));
        return map;
    }

    @Override
    public Object visitAcc(KeyJmlParser.AccContext ctx) {
        var map = createMap(ctx);
        map.put("clause_type", ctx.ACCESSIBLE().getText());
        map.put("heaps", mapOf(ctx.heap()));
        map.put("exprs", mapOf(ctx.expr()));
        return map;
    }

    @Override
    public Object visitExprs(KeyJmlParser.ExprsContext ctx) {
        return mapOf(ctx.expr());
    }

    private <T extends ParserRuleContext> List<Object> mapOf(List<T> seq) {
        return seq.stream().map(it -> it.accept(this)).collect(Collectors.toList());
    }

    @Override
    public Object visitMethodContract(KeyJmlParser.MethodContractContext ctx) {
        var map = createMap(ctx);
        map.put("clauses", mapOf(ctx.clause()));
        return map;
    }

    @Override
    public Object visitJmlClassElem(KeyJmlParser.JmlClassElemContext ctx) {
        var map = createMap(ctx);

        return map;
    }

    @Override
    public Object visitJmlModifier(KeyJmlParser.JmlModifierContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitJmlBlockCntr(KeyJmlParser.JmlBlockCntrContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitJmlAnnotation(KeyJmlParser.JmlAnnotationContext ctx) {
        return mapOf(ctx.annot());
    }

    @Override
    public Object visitExpression(KeyJmlParser.ExpressionContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprSubSequence(KeyJmlParser.ExprSubSequenceContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprEqualities(KeyJmlParser.ExprEqualitiesContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprBinaryXor(KeyJmlParser.ExprBinaryXorContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprImplicationRight(KeyJmlParser.ExprImplicationRightContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprBinaryAnd(KeyJmlParser.ExprBinaryAndContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprLogicalOr(KeyJmlParser.ExprLogicalOrContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprLiteral(KeyJmlParser.ExprLiteralContext ctx) {
        return ctx.jmlPrimary().accept(this);
    }

    @Override
    public Object visitExprLogicalNegate(KeyJmlParser.ExprLogicalNegateContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprArrayAccess(KeyJmlParser.ExprArrayAccessContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprUnaryMinus(KeyJmlParser.ExprUnaryMinusContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprNew(KeyJmlParser.ExprNewContext ctx) {
        var map = createMap(ctx);
        map.put("ids", mapOf(ctx.id()));
        return map;
    }

    @Override
    public Object visitExprSuper(KeyJmlParser.ExprSuperContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprCast(KeyJmlParser.ExprCastContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitLocAll(KeyJmlParser.LocAllContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprRelational(KeyJmlParser.ExprRelationalContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprLogicalAnd(KeyJmlParser.ExprLogicalAndContext ctx) {
        var map = createMap(ctx);
        map.put("op", ctx.op.getText());
        map.put("exprs", mapOf(ctx.expr()));
        return map;
    }

    @Override
    public Object visitExprParens(KeyJmlParser.ExprParensContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprEquivalence(KeyJmlParser.ExprEquivalenceContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprTernary(KeyJmlParser.ExprTernaryContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitLabeledExpr(KeyJmlParser.LabeledExprContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprComprehension(KeyJmlParser.ExprComprehensionContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprShifts(KeyJmlParser.ExprShiftsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprFunction(KeyJmlParser.ExprFunctionContext ctx) {
        var map = createMap(ctx);
        map.put("args", accept(ctx.exprs()));
        map.put("name", accept(ctx.expr()));
        return map;
    }

    @SuppressWarnings("unchecked")
    private <T> T accept(ParserRuleContext ctx) {
        if (ctx == null) return null;
        return (T) ctx.accept(this);
    }

    @Override
    public Object visitExprDivisions(KeyJmlParser.ExprDivisionsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprAntivalence(KeyJmlParser.ExprAntivalenceContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprLineOperators(KeyJmlParser.ExprLineOperatorsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprBinaryNegate(KeyJmlParser.ExprBinaryNegateContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprImplicationLeft(KeyJmlParser.ExprImplicationLeftContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprBinaryOr(KeyJmlParser.ExprBinaryOrContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprThis(KeyJmlParser.ExprThisContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitQuantifiedExpr(KeyJmlParser.QuantifiedExprContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitMod(KeyJmlParser.ModContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAnnot(KeyJmlParser.AnnotContext ctx) {
        return oneOf(ctx.setStm(), ctx.assert_(), ctx.assume(),
                ctx.blockContracts(), ctx.UNREACHABLE(), ctx.fieldDecl(), ctx.loopInv());
    }

    @Override
    public Object visitLoopInv(KeyJmlParser.LoopInvContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitLoopInvclause(KeyJmlParser.LoopInvclauseContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitVariantclause(KeyJmlParser.VariantclauseContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitBlockContracts(KeyJmlParser.BlockContractsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitBlockContract(KeyJmlParser.BlockContractContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitBbehavior(KeyJmlParser.BbehaviorContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitBclause(KeyJmlParser.BclauseContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitBreaks(KeyJmlParser.BreaksContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitReturns_(KeyJmlParser.Returns_Context ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitSetStm(KeyJmlParser.SetStmContext ctx) {
        var map = createMap(ctx);
        map.put("value", accept(ctx.value));
        map.put("variable", accept(ctx.location));
        return map;
    }

    @Override
    public Object visitAssume(KeyJmlParser.AssumeContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAssert_(KeyJmlParser.Assert_Context ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprMultiplication(KeyJmlParser.ExprMultiplicationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExprArryLocSet(KeyJmlParser.ExprArryLocSetContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitJmlPrimary(KeyJmlParser.JmlPrimaryContext ctx) {
        return oneOf(ctx.expr(), ctx.this_(), ctx.super_(), ctx.literal(), ctx.identifier(),
                ctx.classRef(),
                //typeTypeOrVoid DOT CLASS
                //| nonWildcardTypeArguments (explicitGenericInvocationSuffix | THIS arguments)
                ctx.methodReference(), ctx.jmlTypeType());
    }

    @Override
    public Object visitThis_(KeyJmlParser.This_Context ctx) {
        return createMap(ctx);
    }

    @Override
    public Object visitSuper_(KeyJmlParser.Super_Context ctx) {
        return createMap(ctx);
    }

    @Override
    public Object visitJmlTypeType(KeyJmlParser.JmlTypeTypeContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitCompilationUnit(KeyJmlParser.CompilationUnitContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitPackageDeclaration(KeyJmlParser.PackageDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitImportDeclaration(KeyJmlParser.ImportDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeDeclaration(KeyJmlParser.TypeDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitModifier(KeyJmlParser.ModifierContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassOrInterfaceModifier(KeyJmlParser.ClassOrInterfaceModifierContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitVariableModifier(KeyJmlParser.VariableModifierContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassDeclaration(KeyJmlParser.ClassDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeParameters(KeyJmlParser.TypeParametersContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeParameter(KeyJmlParser.TypeParameterContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeBound(KeyJmlParser.TypeBoundContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitEnumDeclaration(KeyJmlParser.EnumDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitEnumConstants(KeyJmlParser.EnumConstantsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitEnumConstant(KeyJmlParser.EnumConstantContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitEnumBodyDeclarations(KeyJmlParser.EnumBodyDeclarationsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitInterfaceDeclaration(KeyJmlParser.InterfaceDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassBody(KeyJmlParser.ClassBodyContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitInterfaceBody(KeyJmlParser.InterfaceBodyContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassBodyDeclaration(KeyJmlParser.ClassBodyDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitMemberDeclaration(KeyJmlParser.MemberDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitMethodDeclaration(KeyJmlParser.MethodDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitMethodBody(KeyJmlParser.MethodBodyContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeTypeOrVoid(KeyJmlParser.TypeTypeOrVoidContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitGenericMethodDeclaration(KeyJmlParser.GenericMethodDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitGenericConstructorDeclaration(KeyJmlParser.GenericConstructorDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitConstructorDeclaration(KeyJmlParser.ConstructorDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitFieldDeclaration(KeyJmlParser.FieldDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitInterfaceBodyDeclaration(KeyJmlParser.InterfaceBodyDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitInterfaceMemberDeclaration(KeyJmlParser.InterfaceMemberDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitConstDeclaration(KeyJmlParser.ConstDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitConstantDeclarator(KeyJmlParser.ConstantDeclaratorContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitInterfaceMethodDeclaration(KeyJmlParser.InterfaceMethodDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitInterfaceMethodModifier(KeyJmlParser.InterfaceMethodModifierContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitGenericInterfaceMethodDeclaration(KeyJmlParser.GenericInterfaceMethodDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitVariableDeclarators(KeyJmlParser.VariableDeclaratorsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitVariableDeclarator(KeyJmlParser.VariableDeclaratorContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitVariableDeclaratorId(KeyJmlParser.VariableDeclaratorIdContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitVariableInitializer(KeyJmlParser.VariableInitializerContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitArrayInitializer(KeyJmlParser.ArrayInitializerContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassOrInterfaceType(KeyJmlParser.ClassOrInterfaceTypeContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeArgument(KeyJmlParser.TypeArgumentContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitQualifiedNameList(KeyJmlParser.QualifiedNameListContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitFormalParameters(KeyJmlParser.FormalParametersContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitFormalParameterList(KeyJmlParser.FormalParameterListContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitFormalParameter(KeyJmlParser.FormalParameterContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitLastFormalParameter(KeyJmlParser.LastFormalParameterContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitQualifiedName(KeyJmlParser.QualifiedNameContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitIntegerLiteral(KeyJmlParser.IntegerLiteralContext ctx) {
        var map = createMap(ctx);
        map.put("value", ctx.getText());
        return map;
    }

    @Override
    public Object visitAnnotation(KeyJmlParser.AnnotationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitElementValuePairs(KeyJmlParser.ElementValuePairsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitElementValuePair(KeyJmlParser.ElementValuePairContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitElementValue(KeyJmlParser.ElementValueContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitElementValueArrayInitializer(KeyJmlParser.ElementValueArrayInitializerContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAnnotationTypeDeclaration(KeyJmlParser.AnnotationTypeDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAnnotationTypeBody(KeyJmlParser.AnnotationTypeBodyContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAnnotationTypeElementDeclaration(KeyJmlParser.AnnotationTypeElementDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAnnotationTypeElementRest(KeyJmlParser.AnnotationTypeElementRestContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAnnotationMethodOrConstantRest(KeyJmlParser.AnnotationMethodOrConstantRestContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAnnotationMethodRest(KeyJmlParser.AnnotationMethodRestContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAnnotationConstantRest(KeyJmlParser.AnnotationConstantRestContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitDefaultValue(KeyJmlParser.DefaultValueContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitBlock(KeyJmlParser.BlockContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitBlockStatement(KeyJmlParser.BlockStatementContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitLocalVariableDeclaration(KeyJmlParser.LocalVariableDeclarationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitStatement(KeyJmlParser.StatementContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitCatchClause(KeyJmlParser.CatchClauseContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitCatchType(KeyJmlParser.CatchTypeContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitFinallyBlock(KeyJmlParser.FinallyBlockContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitResourceSpecification(KeyJmlParser.ResourceSpecificationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitResources(KeyJmlParser.ResourcesContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitResource(KeyJmlParser.ResourceContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitSwitchBlockStatementGroup(KeyJmlParser.SwitchBlockStatementGroupContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitSwitchLabel(KeyJmlParser.SwitchLabelContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitForControl(KeyJmlParser.ForControlContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitForInit(KeyJmlParser.ForInitContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitEnhancedForControl(KeyJmlParser.EnhancedForControlContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitParExpression(KeyJmlParser.ParExpressionContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExpressionList(KeyJmlParser.ExpressionListContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitLambdaExpression(KeyJmlParser.LambdaExpressionContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitLambdaParameters(KeyJmlParser.LambdaParametersContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitLambdaBody(KeyJmlParser.LambdaBodyContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitPrimary(KeyJmlParser.PrimaryContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitMethodReference(KeyJmlParser.MethodReferenceContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassType(KeyJmlParser.ClassTypeContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitCreator(KeyJmlParser.CreatorContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitCreatedName(KeyJmlParser.CreatedNameContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitInnerCreator(KeyJmlParser.InnerCreatorContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitArrayCreatorRest(KeyJmlParser.ArrayCreatorRestContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassCreatorRest(KeyJmlParser.ClassCreatorRestContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExplicitGenericInvocation(KeyJmlParser.ExplicitGenericInvocationContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeArgumentsOrDiamond(KeyJmlParser.TypeArgumentsOrDiamondContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitNonWildcardTypeArgumentsOrDiamond(KeyJmlParser.NonWildcardTypeArgumentsOrDiamondContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitNonWildcardTypeArguments(KeyJmlParser.NonWildcardTypeArgumentsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeList(KeyJmlParser.TypeListContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeType(KeyJmlParser.TypeTypeContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitPrimitiveType(KeyJmlParser.PrimitiveTypeContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitTypeArguments(KeyJmlParser.TypeArgumentsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitSuperSuffix(KeyJmlParser.SuperSuffixContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitExplicitGenericInvocationSuffix(KeyJmlParser.ExplicitGenericInvocationSuffixContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitArguments(KeyJmlParser.ArgumentsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassElem(KeyJmlParser.ClassElemContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassSpec(KeyJmlParser.ClassSpecContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitClassInv(KeyJmlParser.ClassInvContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitFieldDecl(KeyJmlParser.FieldDeclContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitRepresents(KeyJmlParser.RepresentsContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitAccessible(KeyJmlParser.AccessibleContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitModelMethod(KeyJmlParser.ModelMethodContext ctx) {
        var map = createMap(ctx);
        return map;
    }

    @Override
    public Object visitParams(KeyJmlParser.ParamsContext ctx) {
        var map = createMap(ctx);
        map.put("ids", mapOf(ctx.id()));
        map.put("type", mapOf(ctx.jmlTypeType()));
        return map;
    }

    @Override
    public Object visitVisibility(KeyJmlParser.VisibilityContext ctx) {
        return oneOf(ctx.PACKAGE(), ctx.PUBLIC(), ctx.PRIVATE());
    }

    @Override
    public Object visitAccess(KeyJmlParser.AccessContext ctx) {
        var map = createMap(ctx);
        map.put("expr", accept(ctx.expr()));
        map.put("field", accept(ctx.id()));
        return map;
    }

    @Override
    public Object visitIdentifier(KeyJmlParser.IdentifierContext ctx) {
        var map = createMap(ctx);
        map.put("value", ctx.IDENTIFIER().getText());
        return map;
    }

    @Override
    public Object visitClassRef(KeyJmlParser.ClassRefContext ctx) {
        var map = createMap(ctx);
        map.put("type", ctx.typeTypeOrVoid().accept(this));
        return map;
    }

    @Override
    public Object visitId(KeyJmlParser.IdContext ctx) {
        return oneOf(ctx.identifier(), ctx.super_(), ctx.this_());
    }

    @Override
    public Object visitFloatLiteral(KeyJmlParser.FloatLiteralContext ctx) {
        var map = createMap(ctx);
        map.put("value", ctx.FLOAT_LITERAL().getText());
        return map;
    }

    @Override
    public Object visitCharLiteral(KeyJmlParser.CharLiteralContext ctx) {
        var map = createMap(ctx);
        map.put("value", ctx.CHAR_LITERAL().getText());
        return map;
    }

    @Override
    public Object visitStringLiteral(KeyJmlParser.StringLiteralContext ctx) {
        var map = createMap(ctx);
        map.put("value", ctx.STRING_LITERAL().getText());
        return map;
    }

    @Override
    public Object visitBoolLiteral(KeyJmlParser.BoolLiteralContext ctx) {
        var map = createMap(ctx);
        map.put("value", ctx.BOOL_LITERAL().getText().equals("true"));
        return map;
    }

    @Override
    public Object visitNullLiteral(KeyJmlParser.NullLiteralContext ctx) {
        var map = createMap(ctx);
        map.put("value", ctx.NULL_LITERAL().getText());
        return map;
    }

    @Override
    public Object visitConstructorCall(KeyJmlParser.ConstructorCallContext ctx) {
        var map = createMap(ctx);
        map.put("args", ctx.arguments().accept(this));
        map.put("generic", ctx.explicitGenericInvocationSuffix().accept(this));
        map.put("wildCardTypeArguments", ctx.nonWildcardTypeArguments().accept(this));
        return map;
    }
}
