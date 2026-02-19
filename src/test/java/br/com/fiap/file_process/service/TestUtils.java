package br.com.fiap.file_process.service;

import java.lang.reflect.Method;

public class TestUtils {

    public static void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(Object target, String methodName, Object... args) {
        try {

            Class<?>[] paramTypes = new Class<?>[args.length];

            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }

            Method method = target.getClass().getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);

            return method.invoke(target, args);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
