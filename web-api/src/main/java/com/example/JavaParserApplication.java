package com.example;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.maven.surefire.shade.org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

public class JavaParserApplication {
    final static String rootPath = "/Users/ljw/work/us-search-poi-service/us-search-poi/src/main/java/";

    private static String nameToFileName(String name){
        String filename = String.format("%s%s.java", rootPath, StringUtils.replace(name, ".", "/"));

        Path filePath = Paths.get(filename); // 替换为你的文件路径

        if (Files.exists(filePath)) {
            return filename;
        } else {
            name = name.substring(0, StringUtils.lastIndexOf(name, "."));
            return nameToFileName(name);
        }
    }

    public static void main(String[] args) {
        String filepath = "com/amap/us/searchpoi/module/poidetail/struct/baseinfo/StructPoiDetailBaseInfoModule.java";

        try {
            HashSet<String> files = new HashSet<>();

            parseImportClassList(rootPath + filepath, files);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void parseImportClassList(String filename, HashSet<String> parsedFiles){
        try {
            if(parsedFiles.contains(filename)){
                return;
            }

            parsedFiles.add(filename);
            JavaParser javaParser = new JavaParser();
            File file = new File(filename);
            ParseResult<CompilationUnit> parseResult = javaParser.parse(file);
            final VoidVisitor<List<String>> importDeclaration = new VoidVisitorAdapter<List<String>>() {
                @Override
                public void visit(ImportDeclaration n, List<String> arg) {
                    String name = n.getNameAsString();
                    // search内 com/amap/us/searchpoi/
                    if(!name.startsWith("com.amap.us.searchpoi")){
                        return;
                    }

                    String dirname = String.format("%s%s", rootPath, StringUtils.replace(name, ".", "/"));
                    Path path = Paths.get(dirname);
                    if(Files.isDirectory(path)){
                        parseImportClassListDir(dirname, parsedFiles);
                        return;
                    }

                    String filename = nameToFileName(name);
                    System.out.println(filename);
                    parseImportClassList(filename, parsedFiles);
                }
            };

            parseResult.getResult().ifPresent(compilationUnit -> {
                importDeclaration.visit(compilationUnit, null);
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void parseImportClassListDir(String directory, HashSet<String> parsedFiles){
        Path path = Paths.get(directory); // 替换为你的目录路径

        try{
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            for (Path p : directoryStream) {
                parseImportClassList(p.toString(), parsedFiles);
            }
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
