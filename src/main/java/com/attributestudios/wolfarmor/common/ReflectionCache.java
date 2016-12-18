package com.attributestudios.wolfarmor.common;

import com.attributestudios.wolfarmor.WolfArmorMod;
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

    private static Map<String, Field> CACHED_REFLECTION_FIELDS = Maps.newHashMap();
    private static Map<String, Method> CACHED_REFLECTION_METHODS = Maps.newHashMap();

    private ReflectionCache() {}

    /**
     * Finds and caches a field.
     * @param clazz The class
     * @param fieldNames The instance
     * @return The field
     */
    @Nullable
    public static Field getField(@Nonnull Class clazz,
                                 @Nonnull String ... fieldNames) {
        if(fieldNames.length > 0) {
            Field field = null;

            for(String s : fieldNames) {
                String key = clazz.getName() + "." + s;
                if(CACHED_REFLECTION_FIELDS.containsKey(key)) {
                    field = CACHED_REFLECTION_FIELDS.get(key);
                    break;
                }
            }

            if(field == null) {
                for(String s : fieldNames) {
                    String key = clazz.getName() + "." + s;
                    try {
                        field = ReflectionHelper.findField(clazz, fieldNames);
                        CACHED_REFLECTION_FIELDS.put(key, field);
                    } catch(ReflectionHelper.UnableToFindFieldException ex) {
                        WolfArmorMod.getLogger().error(ex);
                    }
                }
            }

            return field;
        }
        throw new IllegalArgumentException("Must specify at least one field name.");
    }

    /**
     * Finds and caches a method.
     * @param clazz The class
     * @param instance The instance
     * @param methodNames The possible method names
     * @param params The method parameter types
     * @param <E> The instance or class type
     * @return The method
     */
    @Nullable
    public static <E> Method getMethod(@Nonnull Class<? super E> clazz,
                                       @Nullable E instance,
                                       @Nonnull String[] methodNames,
                                       @Nonnull Class<?> ... params) {
        if(methodNames.length > 0) {
            Method method = null;

            String keyParams = "(";

            for(int i = 0; i < params.length; i++) {
                if(i > 0) {
                    keyParams += ", ";
                }
                keyParams += params[i].getName();
            }

            keyParams += ")";

            for(String s : methodNames) {
                String key = clazz.getName() + "." + s + keyParams;
                if(CACHED_REFLECTION_METHODS.containsKey(key)) {
                    method = CACHED_REFLECTION_METHODS.get(key);
                    break;
                }
            }

            if(method == null) {
                for(String s : methodNames) {
                    String key = clazz.getName() + "." + s + keyParams;
                    try {
                        method = ReflectionHelper.findMethod(clazz, instance, methodNames, params);
                        CACHED_REFLECTION_METHODS.put(key, method);
                    } catch (ReflectionHelper.UnableToFindMethodException ex) {
                        WolfArmorMod.getLogger().error(ex);
                    }
                }
            }
            return method;
        }
        throw new IllegalArgumentException("Must specify at least one method name.");
    }
}
