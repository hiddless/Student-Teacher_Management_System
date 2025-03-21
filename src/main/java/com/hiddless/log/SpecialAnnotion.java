package com.hiddless.log;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SpecialAnnotion {
}

/// Test Class
class Test {

    private String name;

    public Test() {

    }

    @SpecialAnnotion
    public static void process() {
        System.out.println("This method will be log.");
    }

    public static void main(String[] args) {
        Test.process();
    }
}
