package service;

import model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Practical 13: generic repository. The key is read from the @Id field by reflection. */
public class Repository<T> {
    private final Map<String, T> items = new HashMap<>();

    public T save(T entity) {
        items.put(idOf(entity), entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    public T findById(String id) {
        return items.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public boolean delete(String id) {
        return items.remove(id) != null;
    }

    public int count() {
        return items.size();
    }

    /** Bounded generic method: works for Integer[], Double[], Long[] and any other Number array. */
    public static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            return 0;
        }
        double sum = 0;
        for (N v : values) {
            sum += v.doubleValue();
        }
        return sum / values.length;
    }

    private String idOf(T entity) {
        // walk up the hierarchy so an @Id declared in Account is found for SavingsAccount etc.
        for (Class<?> c = entity.getClass(); c != null; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        return String.valueOf(field.get(entity));
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Cannot read @Id field", e);
                    }
                }
            }
        }
        throw new IllegalArgumentException("No @Id field in " + entity.getClass().getSimpleName());
    }
}
