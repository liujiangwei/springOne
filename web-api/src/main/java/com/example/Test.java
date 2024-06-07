package com.example;

public class Test {
    public String a(){
        return b() + c() + d(c());
    }

    public String b(){
        return "b";
    }

    public String c(){
        return "c" + b();
    }

    public String d(String str){
        return str;
    }
}
