package dev.cbyrne.pufferfishmodloader.launch;

import dev.cbyrne.pufferfishmodloader.mods.launch.loader.TransformingClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PMLClientMain {
    public static void main(String... args) throws Throwable {
        ClassLoader classLoader = new TransformingClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        Class<?> clazz = classLoader.loadClass("dev.cbyrne.pufferfishmodloader.launch.PMLEntryPoint");
        Method method = clazz.getMethod("start", String[].class, boolean.class);
        try {
            method.invoke(null, args, false);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
