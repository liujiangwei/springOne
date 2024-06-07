package com.example;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.LinkedList;
import java.util.List;

public class MethodCallChainVisitor extends VoidVisitorAdapter<List<MethodCallExpr>> {

    @Override
    public void visit(MethodCallExpr n, List<MethodCallExpr> arg) {
        arg.add(n);
        super.visit(n, arg);
    }

    public static void main(String[] args) {
        String code = "class X {" +
                "  void foo() {" +
                "    bar();" +
                "  }" +
                "  void bar() {" +
                "    baz();" +
                "  }" +
                "  void baz() {}" +
                "}";

        JavaParser javaParser = new JavaParser();
        CompilationUnit compilationUnit = javaParser.parse(code).getResult().orElse(null);
        MethodCallChainVisitor methodCallChainVisitor = new MethodCallChainVisitor();
        LinkedList<MethodCallExpr> methodCallExprs = new LinkedList<>();
        methodCallChainVisitor.visit(compilationUnit, methodCallExprs);

        methodCallExprs.forEach(methodCallExpr -> {
            System.out.println(methodCallExpr.getName());
        });
    }
}