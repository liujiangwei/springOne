package com.example;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ModuleApplication {

//    static final VoidVisitor<List<String>> importDeclaration = new VoidVisitorAdapter<List<String>>() {
//        @Override
//        public void visit(ImportDeclaration n, List<String> arg) {
//
//        }
//    };

    static final VoidVisitor<List<String>> methodCallExpr = new VoidVisitorAdapter<List<String>>() {
        @Override
        public void visit(MethodCallExpr methodCallExpr, List<String> arg) {
            String packageName = "";
            for (Node node : methodCallExpr.getChildNodes()){
                if(node instanceof MethodCallExpr){
                    ModuleApplication.methodCallExpr.visit((MethodCallExpr) node, arg);
                }
            }

            // 如果是内部方法
            methodCallExpr.getScope().ifPresent(expression -> {
                // 方法调用后调用的方法
                if(expression instanceof MethodCallExpr){
                    parseMethodCallExpr((MethodCallExpr)expression, methodCallExpr.getNameAsString());
                }

                // 变量调用的方法
                if(expression instanceof NameExpr){
                    String scopeName = ((NameExpr) expression).getName().toString();

                }
            });
        }
    };


    static void  parseNameExpr(Node parentNode, NameExpr nameExpr, String functionName){
        if(parentNode instanceof CompilationUnit){
            NodeList<ImportDeclaration> nodeList= ((CompilationUnit) parentNode).getImports();

            String name = nodeList.stream()
                    .map(importDeclaration -> importDeclaration.getNameAsString())
                    .filter(n -> n.endsWith("."+nameExpr.getNameAsString()))
                    .findFirst()
                    .orElse(null);

            if(StringUtils.isEmpty(name)){
                return;
            }
        }
    }


    public static class CodeObj{
        String fileName;
        String functionName;
        String code;
    }



    static final VoidVisitor<String> methodDeclaration = new VoidVisitorAdapter<>() {
        @Override
        public void visit(MethodDeclaration n, String functionName) {
            if (StringUtils.isEmpty(functionName) || n.getNameAsString().equals(functionName)) {
                log(n);
            }

            methodCallExpr.visit(n, null);
        }
    };

    static String getPackageName(Node node){
        Node parent = node.getParentNode().orElse(null);
        if(Objects.nonNull(parent)){
            return getPackageName(parent);
        }

        if(node instanceof CompilationUnit){
            return ((CompilationUnit) node).getPackageDeclaration().map(PackageDeclaration::getNameAsString).orElse(null);
        }

        return null;
    }

    static void log(MethodDeclaration methodDeclaration){
        String packageName = getPackageName(methodDeclaration);
        String functionName = methodDeclaration.getNameAsString();
        String code =  methodDeclaration.getBody().map(Node::toString).orElse(null);

        if(StringUtils.isNotEmpty(code) && code.contains("tTagClass")){
            System.out.printf("%s, %s\n", packageName, functionName);
        }
    }


    final static String rootPath = "/Users/ljw/work/us-search-poi-service/us-search-poi/src/main/java/";

    public static void main(String[] args) {
        //String filename = "com/amap/us/searchpoi/module/poidetail/struct/baseinfo/StructPoiDetailBaseInfoModule.java";
        String filename = rootPath + "com/amap/us/searchpoi/module/poilist/card_list/render/struct" +"/ComprehensivePoiItemRender.java";
        //String testFile = "/Users/ljw/work/springOne/web-api/src/main/java/com/example/Test.java";
        //String filename = "/Users/ljw/work/us-search-poi-service/us-search-poi/src/main/java/com/amap/us/searchpoi" +"/rest/poilist/util/CouponUtil.java";
        parseFile(filename, null);
    }

    public static void parseFile(String filename, String funcName){
        File file = new File(filename);

        JavaParser javaParser = new JavaParser();
        try {
            ParseResult<CompilationUnit> parseResult = javaParser.parse(file);
            parseResult.getResult().ifPresent(compilationUnit -> {
                methodDeclaration.visit(compilationUnit, funcName);

                //methodCallExpr.visit(compilationUnit, null);
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void parsePackage(String packageName, String funcName){
        parseFile(packageName, funcName);
    }


    // returnMethodCallExprName 返回值的调用方法
    public static void parseMethodCallExpr(MethodCallExpr methodCallExpr, String returnMethodCallExprName){

    }
}
