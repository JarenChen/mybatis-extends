package com.wshsoft.mybatis.toolkit;

import com.wshsoft.mybatis.entity.TableFieldInfo;
import com.wshsoft.mybatis.entity.TableInfo;
import com.wshsoft.mybatis.enums.FieldStrategy;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 反射工具类
 * </p>
 *
 * @author Carry xie
 * @Date 2016-09-22
 */
public class ReflectionKit {
    private static final Log logger = LogFactory.getLog(ReflectionKit.class);

    /**
     * <p>
     * 反射 method 方法名，例如 getId
     * </p>
     *
     * @param field
     * @param str
     *            属性字符串内容
     * @return
     */
    public static String getMethodCapitalize(Field field, final String str) {
        Class<?> fieldType = field.getType();
        String concatstr;
        if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
            concatstr = "is";
        } else {
            concatstr = "get";
        }
        return StringUtils.concatCapitalize(concatstr, str);
    }

    /**
     * 获取 public get方法的值
     *
     * @param cls
     * @param entity
     *            实体
     * @param str
     *            属性字符串内容
     * @return Object
     */
    public static Object getMethodValue(Class<?> cls, Object entity, String str) {
        Object obj = null;
        try {
            Map<String, Field> fieldMaps = getFieldMaps(cls);
            if (MapUtils.isEmpty(fieldMaps)){
                logger.warn(String.format("Warn: NoSuchField in %s.  Cause:", cls.getSimpleName()));
                return obj;
            }
            Method method = cls.getMethod(getMethodCapitalize(fieldMaps.get(str), str));
            obj = method.invoke(entity);
        } catch (NoSuchMethodException e) {
            logger.warn(String.format("Warn: No such method. in %s.  Cause:", cls.getSimpleName()) + e);
        } catch (IllegalAccessException e) {
            logger.warn(String.format("Warn: Cannot execute a private method. in %s.  Cause:", cls.getSimpleName()) + e);
        } catch (Exception e) {
            logger.warn("Warn: Unexpected exception on getMethodValue.  Cause:" + e);
        }
        return obj;
    }

    /**
     * 获取 public get方法的值
     *
     * @param entity
     *            实体
     * @param str
     *            属性字符串内容
     * @return Object
     */
    public static Object getMethodValue(Object entity, String str) {
        if (null == entity) {
            return null;
        }
        return getMethodValue(entity.getClass(), entity, str);
    }

    /**
     * 调用对象的get方法检查对象所有属性是否为null
     *
     * @param bean
     *            检查对象
     * @return boolean true对象所有属性不为null,false对象所有属性为null
     */
    public static boolean checkFieldValueNotNull(Object bean) {
        if (null == bean) {
            return false;
        }
        Class<?> cls = bean.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        if (null == tableInfo) {
            logger.warn("Warn: Could not find @TableId.");
            return false;
        }
        boolean result = false;
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        for (TableFieldInfo tableFieldInfo : fieldList) {
            FieldStrategy fieldStrategy = tableFieldInfo.getFieldStrategy();
            Object val = getMethodValue(cls, bean, tableFieldInfo.getProperty());
            if (FieldStrategy.NOT_EMPTY.equals(fieldStrategy)) {
                if (StringUtils.checkValNotNull(val)) {
                    result = true;
                    break;
                }
            } else {
                if (null != val) {
                    result = true;
                    break;
                }
            }

        }
        return result;
    }

    /**
     * 反射对象获取泛型
     *
     * @param clazz
     *            对象
     * @param index
     *            泛型所在位置
     * @return Class
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(String.format("Warn: %s's superclass not ParameterizedType", clazz.getSimpleName()));
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn(String.format("Warn: Index: %s, Size of %s's Parameterized Type: %s .", index, clazz.getSimpleName(),
                    params.length));
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(String.format("Warn: %s not set the actual class on superclass generic parameter", clazz.getSimpleName()));
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz
     *            反射类
     * @return
     */
    private static Map<String, Field> getFieldMaps(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }
        Map<String, Field> fieldMaps = new LinkedHashMap<String, Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
			/* 过滤静态属性 */
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
			/* 过滤 transient关键字修饰的属性 */
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            fieldMaps.put(field.getName(), field);
        }
		/* 处理父类字段 */
        Class<?> superClass = clazz.getSuperclass();
        if (superClass.equals(Object.class)) {
            return fieldMaps;
        }
        fieldMaps.putAll(getFieldMaps(superClass));
        return fieldMaps;
    }
}