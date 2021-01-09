package me.dreamhopping.pml.launch;

import me.dreamhopping.pml.mods.launch.loader.TransformingClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PMLClientMain {
    public static void main(String... args) throws Throwable {
        ClassLoader classLoader = new TransformingClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);

        Class<?> clazz = classLoader.loadClass("me.dreamhopping.pml.launch.PMLEntryPoint");
        Method method = clazz.getMethod("start", String[].class, boolean.class);

        try {
            method.invoke(null, args, false);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
