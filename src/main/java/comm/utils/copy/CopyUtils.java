package comm.utils.copy;

import com.google.common.primitives.Primitives;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The utility to deep copy the given object.
 */
public final class CopyUtils {
    private static final Logger log = LoggerFactory.getLogger(CopyUtils.class);

    private CopyUtils() {
    }

    /**
     * Deep-copy the given object.
     *
     * @param src the source object to be deep-copied.
     * @param <T> the type of the given object.
     * @return the deep-copied object.
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     */
    public static <T> T copy(T src) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        return doCopy(src, new IdentityHashMap<>());
    }

    /**
     * Deep-copy the given object.
     *
     * @param src     the source object to be deep-copied.
     * @param visited the copied object collection.
     * @param <T>     the type of the given object.
     * @return the deep-copied object.
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     */
    private static <T> T doCopy(T src, Map<Object, Object> visited) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        if (src == null) {
            return null;
        }
        if (src.getClass().isAssignableFrom(String.class)
            || src.getClass().isAssignableFrom(Integer.class)
            || src.getClass().isAssignableFrom(LocalDateTime.class)
            || src.getClass().isEnum()
            || src.getClass() == Class.class) {
            return src;
        }
        if (visited.containsKey(src)) {
            return (T) visited.get(src);
        }
        if (src.getClass().isArray()) {
            return copyArray(src, visited);
        }else if(src.getClass().getName().contains("java.util.HashSet")){
            return copySet(src, visited);
        }else if(src.getClass().getName().contains("java.util.HashMap")){
            return copyMap(src, visited);
        }
        return copyObject(src, visited);
    }

    /**
     * Gets the unsafe object to create a new instance.
     *
     * @return the {@link Unsafe} object.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    /**
     * Deep-copy the array object.
     *
     * @param src     the given object with array type.
     * @param visited the copied object collection.
     * @param <T>     the type of the element in the given array.
     * @return the deep-copied array object.
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InstantiationException
     */
    private static <T> T copyArray(T src, Map<Object, Object> visited) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        int length = Array.getLength(src);
        Object result =
            Array.newInstance(
                src.getClass().getComponentType(),
                Array.getLength(src));
        visited.put(src, result);
        for (int i = 0; i < length; i++) {
            Array.set(result, i, doCopy(Array.get(src, i), visited));
        }
        return (T) result;
    }

    private static <T> T copySet(T src, Map<Object, Object> visited) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        HashSet org =   (HashSet)(src);
        Collection<T> result = new HashSet<T>(org.size());
        visited.put(src, result);
        Iterator<T> itr = org.iterator();
        while(itr.hasNext()) {
            result.add(doCopy(itr.next(), visited));
        }
        return (T) result;
    }

    private static <T> T copyMap(T src, Map<Object, Object> visited) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        HashMap<T,T> org =  (HashMap)(src);
        Map<T,T> result = new HashMap();
        for (Map.Entry<T,T> entry : org.entrySet()) {
            result.put(entry.getKey(),doCopy(entry.getValue(),visited)) ;

        }
        return (T) result;
    }

    /**
     * Checks if the given field with the given modifier.
     *
     * @param field    the given field to check.
     * @param modifier the modifier of the given field.
     * @return {@code true} if the field has the given modifier, otherwise {@code false}.
     */
    private static boolean hasModifier(Field field, int modifier) {
        return (field.getModifiers() & modifier) != 0;
    }

    /**
     * Deep-copy the given object with a new instance.
     *
     * @param src     the given object to be deep-copied.
     * @param visited the copied object collection.
     * @param <T>     the type of the given object.
     * @return the deep-copied object instance.
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static <T> T copyObject(T src, Map<Object, Object> visited) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Object result = getUnsafe().allocateInstance(src.getClass());
        visited.put(src, result);
        Class<?> clazz = src.getClass();
        do {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // if the field is static or final we won't do anything with it
                if (hasModifier(field, Modifier.STATIC) || hasModifier(field, Modifier.FINAL)) {
                    continue;
                }
                field.setAccessible(true);
                // if it's primitive, we just set it's value to our new instance
                // otherwise we recursively copy it's value
                if (Primitives.unwrap(field.getType()).isPrimitive()) {
                    field.set(result, field.get(src));
                } else {
                    field.set(result, doCopy(field.get(src), visited));
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return (T) result;
    }

    /**
     * Deep-copy the given object.
     *
     * @param source the original object.
     * @return the deep-copied object instance.
     */
    public static <T> T deepCopy(T source) {
        try {
            return CopyUtils.copy(source);
        } catch (Exception e) {
            log.error("Failed to deep-copy the given object.", e);
            return null;
        }
    }
}
