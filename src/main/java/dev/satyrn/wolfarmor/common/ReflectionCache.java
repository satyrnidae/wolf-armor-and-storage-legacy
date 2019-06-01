package dev.satyrn.wolfarmor.common;

import dev.satyrn.wolfarmor.WolfArmorMod;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Caches reflection calls
 */
public abstract class ReflectionCache {
    //region Fields

    private static Map<String, Field> CACHED_REFLECTION_FIELDS = Maps.newHashMap();
    private static Map<String, Method> CACHED_REFLECTION_METHODS = Maps.newHashMap();

    private static Exception lastError;

    //endregion Fields

    //region Accessors / Mutators

    /**
     * Finds and caches a field.
     *
     * @param clazz        The class
     * @param fieldName    The unobfuscated name of the field
     * @param fieldObfName the obfuscated name of the field
     * @return The field
     */
    @Nullable
    public static Field getField(@Nonnull Class clazz,
                                 @Nonnull String fieldName,
                                 @Nonnull String fieldObfName) {
        String key = String.format("%s.%s", clazz.getName(), fieldObfName);
        Field field = null;
        if(CACHED_REFLECTION_FIELDS.containsKey(key)) {
            field = CACHED_REFLECTION_FIELDS.get(key);
        }
        if(field == null) {
            try {
                field = ReflectionHelper.findField(clazz, fieldName, fieldObfName);
                CACHED_REFLECTION_FIELDS.put(key, field);
            } catch (Exception ex) {
                WolfArmorMod.getLogger().error(ex);
                setLastError(ex);
            }
        }

        return field;
    }

    /**
     * Finds and caches a method.
     *
     * @param clazz         The class
     * @param methodName    The deobf method name
     * @param methodObfName The obf method name
     * @param params        The method parameter types
     * @param <E>           The instance or class type
     * @return The method
     */
    @Nullable
    public static <E> Method getMethod(@Nonnull Class<? super E> clazz,
                                       @Nonnull String methodName,
                                       @Nullable String methodObfName,
                                       @Nullable Class<?>... params) {
        Method method = null;

        StringBuilder keyParams = new StringBuilder("(");

        if(params != null) {
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    keyParams.append(", ");
                }
                keyParams.append(params[i].getName());
            }
        }

        keyParams.append(")");

        String key = clazz.getName() + "." + methodObfName + keyParams;
        if (CACHED_REFLECTION_METHODS.containsKey(key)) {
            method = CACHED_REFLECTION_METHODS.get(key);
        }

        if (method == null) {
            try {
                method = ReflectionHelper.findMethod(clazz, methodName, methodObfName, params);
                CACHED_REFLECTION_METHODS.put(key, method);
            } catch (Exception ex) {
                WolfArmorMod.getLogger().error(ex);
                setLastError(ex);
            }
        }

        return method;
    }
    //endregion Accessors / Mutators

    @Nullable
    public static Exception getLastError() {
        return lastError;
    }

    private static void setLastError(@Nullable Exception lastError) {
        ReflectionCache.lastError = lastError;
    }
}
