package util;

import model.annotation.MaxLength;
import model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/** Practical 7: reads @Positive and @MaxLength by reflection and returns the error messages. */
public final class AnnotationValidator {
    private AnnotationValidator() {
    }

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();
        // walk the class hierarchy so fields declared in Account are checked for subclasses too
        for (Class<?> c = obj.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (field.isAnnotationPresent(Positive.class) && value instanceof Number n
                            && n.longValue() <= 0) {
                        errors.add(field.getAnnotation(Positive.class).message());
                    }
                    if (field.isAnnotationPresent(MaxLength.class) && value instanceof String s) {
                        int max = field.getAnnotation(MaxLength.class).value();
                        if (s.length() > max) {
                            errors.add("length must be at most " + max);
                        }
                    }
                } catch (IllegalAccessException e) {
                    errors.add("could not read field " + field.getName());
                }
            }
        }
        return errors.toArray(new String[0]);
    }
}
