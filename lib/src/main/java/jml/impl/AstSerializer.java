package jml.impl;

import jml.ASTProperties;
import lombok.var;
import org.eclipse.jdt.core.dom.*;

import java.util.*;

/**
 * @author Alexander Weigl
 * @version 1 (1/31/20)
 */
@SuppressWarnings("unchecked")
public class AstSerializer extends ASTVisitor {
    public Map<String, Object> root;
    private Stack<Map<String, Object>> stack = new Stack<>();

    public AstSerializer() {
        super(false);
    }

    @Override
    public void preVisit(ASTNode node) {
        var map = new LinkedHashMap<String, Object>();
        map.put("class", node.getClass().getName());
        map.put("start", node.getStartPosition());
        map.put("length", node.getLength());
        map.put("nodeType", node.getNodeType());
        map.put("flags", node.getFlags());
        copyJml(map, node);

        var loc = node.getLocationInParent();
        if (loc != null && !stack.isEmpty()) {
            var cur = stack.peek();
            if (loc.isChildListProperty()) {
                List<Object> seq;
                if (cur.containsKey(loc.getId())) {
                    seq = (List<Object>) cur.get(loc.getId());
                } else {
                    seq = new LinkedList<>();
                    cur.put(loc.getId(), seq);
                }
                seq.add(map);
            } else {
                cur.put(loc.getId(), map);
            }
        }
        stack.push(map);
    }

    private void copyJml(HashMap<String, Object> target, ASTNode node) {
        var seq = ASTProperties.getReferencedJmlComments(node, false);
        if (seq != null) {
            var jmlComments = new ArrayList<>(seq.size());
            seq.forEach(c -> {
                final JmlSerializer jmlSerializer = new JmlSerializer(c.getStartPosition());
                var jc = new HashMap<String, Object>(6);
                jc.put("startPosition", c.getStartPosition());
                jc.put("length", c.getLength());
                jc.put("type", c.getType());
                jc.put("annotations", c.getAnnotations());
                jc.put("content", c.getContent());
                if (c.getContext() != null) {
                    jc.put("ast", c.getContext().accept(jmlSerializer));
                }
                jc.put("parsing_errors", c.getParserErrors());
                jmlComments.add(jc);
            });
            target.put("jmlComments", jmlComments);
        }
    }

    @Override
    public boolean visit(AnnotationTypeDeclaration node) {
        return true;
    }

    @Override
    public boolean visit(AnnotationTypeMemberDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(AnonymousClassDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayAccess node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayCreation node) {
        visitAsExpression(node);

        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayInitializer node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(AssertStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(Assignment node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(Block node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(BlockComment node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(BooleanLiteral node) {
        stack.peek().put("value", node.booleanValue());
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(BreakStatement node) {
        stack.peek().put("label", node.getLabel());
        return super.visit(node);
    }

    @Override
    public boolean visit(CastExpression node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(CatchClause node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(CharacterLiteral node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(CompilationUnit node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ConditionalExpression node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ConstructorInvocation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ContinueStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(CreationReference node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(Dimension node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(DoStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(EmptyStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(EnhancedForStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(EnumConstantDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(EnumDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ExportsDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ExpressionMethodReference node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ExpressionStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(FieldAccess node) {
        visitAsExpression(node);

        return super.visit(node);
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ForStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(IfStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ImportDeclaration node) {
        stack.peek().put("static", node.isStatic());
        stack.peek().put("ondemand", node.isOnDemand());
        return super.visit(node);
    }

    @Override
    public boolean visit(InfixExpression node) {
        stack.peek().put("operator", node.getOperator().toString());
        return super.visit(node);
    }

    @Override
    public boolean visit(Initializer node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(InstanceofExpression node) {
        visitAsExpression(node);

        return super.visit(node);
    }

    @Override
    public boolean visit(IntersectionType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(Javadoc node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(LabeledStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(LambdaExpression node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(LineComment node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MarkerAnnotation node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(MemberRef node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MemberValuePair node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodRef node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodRefParameter node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodInvocation node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(Modifier node) {
        put("value", node.getKeyword().toFlagValue());
        put("svalue", node.getKeyword().toString());
        return super.visit(node);
    }

    @Override
    public boolean visit(ModuleDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ModuleModifier node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(NameQualifiedType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(NormalAnnotation node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(NullLiteral node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(NumberLiteral node) {
        put("value", node.getToken());
        visitAsExpression(node);
        return super.visit(node);
    }

    private void visitAsExpression(Expression node) {
        put("boxing", node.resolveBoxing());
        put("unboxing", node.resolveUnboxing());
        if (node.resolveConstantExpressionValue() != null) {
            put("constantExprValue", node.resolveConstantExpressionValue());
        }
        if (node.resolveTypeBinding() != null) {
            put("type", node.resolveTypeBinding().getBinaryName());
        }
    }

    private void put(String value, Object obj) {
        stack.peek().put(value, obj);
    }

    @Override
    public boolean visit(OpensDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(PackageDeclaration node) {
        put("name", node.getName());
        put("javadoc", node.getJavadoc());
        return super.visit(node);
    }

    @Override
    public boolean visit(ParameterizedType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ParenthesizedExpression node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(PostfixExpression node) {
        stack.peek().put("operator", node.getOperator().toString());
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(PrefixExpression node) {
        stack.peek().put("operator", node.getOperator().toString());
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ProvidesDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(PrimitiveType node) {
        put("primitiveType", node.getPrimitiveTypeCode().toString());
        return super.visit(node);
    }

    @Override
    public boolean visit(QualifiedName node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(QualifiedType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(RequiresDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ReturnStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SimpleName node) {
        put("value", node.getIdentifier());
        visitAsExpression(node);
        return true;
    }

    @Override
    public boolean visit(SimpleType node) {
        visitTypeBinding(node.resolveBinding());
        //stack.peek().put("value", node.getName().getFullyQualifiedName());
        stack.peek().put("isVar", node.isVar());
        return true;
    }

    private void visitTypeBinding(ITypeBinding resolveBinding) {
        if (resolveBinding == null) return;
        put("fullyQualifiedName", resolveBinding.getQualifiedName());
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(SingleVariableDeclaration node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(StringLiteral node) {
        visitAsExpression(node);
        stack.peek().put("escapedValue", node.getEscapedValue());
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperConstructorInvocation node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperFieldAccess node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperMethodInvocation node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(SuperMethodReference node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchCase node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchExpression node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(SynchronizedStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TagElement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TextBlock node) {
        visitAsExpression(node);

        return super.visit(node);
    }

    @Override
    public boolean visit(TextElement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(ThisExpression node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(ThrowStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TryStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        stack.peek().put("isInterface", node.isInterface());
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeDeclarationStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeLiteral node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeMethodReference node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeParameter node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(UnionType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(UsesDirective node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationExpression node) {
        visitAsExpression(node);
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(VariableDeclarationFragment node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(WhileStatement node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(WildcardType node) {
        return super.visit(node);
    }

    @Override
    public boolean visit(YieldStatement node) {
        stack.peek().put("implicit", node.isImplicit());
        return super.visit(node);
    }

    @Override
    public void postVisit(ASTNode node) {
        var cur = stack.pop();
        if (stack.isEmpty())
            root = cur;
    }
}
