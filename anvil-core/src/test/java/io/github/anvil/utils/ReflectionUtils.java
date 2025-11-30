package io.github.anvil.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ReflectionUtils {
    private ReflectionUtils() {
        // Prevent instantiation
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field '" + fieldName + "' not found in class " + clazz.getName(), e);
        }
    }

    public static Annotation getFieldAnnotation(Class<?> clazz, String fieldName, Class<? extends Annotation> annotationClass) {
        Field field = getField(clazz, fieldName);
        return field.getAnnotation(annotationClass);
    }
}
