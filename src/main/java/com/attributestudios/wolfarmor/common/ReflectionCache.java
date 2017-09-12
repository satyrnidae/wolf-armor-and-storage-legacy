package com.attributestudios.wolfarmor.common;

import com.attributestudios.wolfarmor.WolfArmorMod;
import com.attributestudios.wolfarmor.api.util.Future;
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
public final class ReflectionCache {
    //region Fields

    private static Map<String, Field> CACHED_REFLECTION_FIELDS = Maps.newHashMap();
    private static Map<String, Method> CACHED_REFLECTION_METHODS = Maps.newHashMap();

    private static Exception _lastError;

    //endregion Fields

    //region Constructors

    private ReflectionCache() {
    }

    //endregion Constructors

    //region Accessors / Mutators

    /**
     * Finds and caches a field.
     *
     * @param clazz      The class
     * @param fieldNames The instance
     * @return The field
     */
    @Nullable
    public static Field getField(@Nonnull Class clazz,
                                 @Nonnull String... fieldNames) {
        if (fieldNames.length > 0) {
            Field field = null;

            for (String s : fieldNames) {
                String key = clazz.getName() + "." + s;
                if (CACHED_REFLECTION_FIELDS.containsKey(key)) {
                    field = CACHED_REFLECTION_FIELDS.get(key);
                    break;
                }
            }

            if (field == null) {
                for (String s : fieldNames) {
                    String key = clazz.getName() + "." + s;
                    try {
                        field = ReflectionHelper.findField(clazz, fieldNames);
                        CACHED_REFLECTION_FIELDS.put(key, field);
                    } catch (ReflectionHelper.UnableToFindFieldException ex) {
                        WolfArmorMod.getLogger().error(ex);
                        _lastError = ex;
                    }
                }
            }

            return field;
        }
        throw new IllegalArgumentException("Must specify at least one field name.");
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
    @Future
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

        String key = clazz.getName() + "." + methodName + keyParams;
        if (CACHED_REFLECTION_METHODS.containsKey(key)) {
            method = CACHED_REFLECTION_METHODS.get(key);
        }

        if (method == null) {
            try {
                method = ReflectionHelper.findMethod(clazz, methodName, methodObfName, params);
                CACHED_REFLECTION_METHODS.put(key, method);
            } catch (ReflectionHelper.UnableToFindMethodException ex) {
                WolfArmorMod.getLogger().error(ex);
                _lastError = ex;
            }
        }

        return method;
    }
    //endregion Accessors / Mutators

    @Future
    @Nullable
    public static Exception getLastError() {
        return _lastError;
    }
}
