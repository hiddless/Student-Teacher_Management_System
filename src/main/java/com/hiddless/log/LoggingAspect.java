package com.hiddless.log;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class LoggingAspect {
    public static void invokeAnnotatedMethods(Object obj, Object... args) {
        Class<?> clazz = obj.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(LogExecutionTime.class)) {
                long start = System.nanoTime();
                try {
                    method.setAccessible(true);

                    Parameter[] parameters = method.getParameters();

                    if (parameters.length == 0) {
                        method.invoke(obj);
                    }else {
                        if (args.length == parameters.length) {
                            method.invoke(obj, args);
                        }else {
                            System.out.println("Error " + method.getName() + "Wrong Parameter! Should be: " + parameters.length + ", Given: " + args.length);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("There is an error" + method.getName(), e);
                }
                long end = System.nanoTime();
                System.out.println("Correct " + method.getName() + " method " + (end - start) / 1_000_000 + "miliseconds.");
            }
        }
    }
}
