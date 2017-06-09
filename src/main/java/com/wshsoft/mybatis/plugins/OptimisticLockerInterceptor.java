package com.wshsoft.mybatis.plugins;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.wshsoft.mybatis.annotations.Version;
import com.wshsoft.mybatis.entity.TableFieldInfo;
import com.wshsoft.mybatis.entity.TableInfo;
import com.wshsoft.mybatis.mapper.Wrapper;
import com.wshsoft.mybatis.toolkit.ReflectionKit;
import com.wshsoft.mybatis.toolkit.TableInfoHelper;

/**
 * <p>
 * MyBatis乐观锁插件
 * </p>
 * <p>
 * 
 * <pre>
 * 之前：update user set name = ?, password = ? where id = ?
 * 之后：update user set name = ?, password = ?, version = version+1 where id = ? and version = ?
 * 对象上的version字段上添加{@link Version}注解
 * sql可以不需要写version字段,只要对象version有值就会更新
 * 支持,int Integer, long Long, Date,Timestamp
 * 其他类型可以自定义实现,注入versionHandlers,多个以逗号分隔
 * </pre>
 * 
 * @author Carry xie
 * @since 2017-04-08
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class OptimisticLockerInterceptor implements Interceptor {

    private final Map<Class<?>, EntityField> versionFieldCache = new HashMap<>();
    private final Map<Class<?>, List<EntityField>> entityFieldsCache = new HashMap<>();

    private static final String MP_OPTLOCK_VERSION_ORIGINAL = "MP_OPTLOCK_VERSION_ORIGINAL";
    private static final String MP_OPTLOCK_VERSION_COLUMN = "MP_OPTLOCK_VERSION_COLUMN";
    private static final String NAME_ENTITY = "et";
    private static final String NAME_ENTITY_WRAPPER = "ew";
    private static final String PARAM_UPDATE_METHOD_NAME = "update";

    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if(SqlCommandType.UPDATE.compareTo(ms.getSqlCommandType())!=0){
            return invocation.proceed();
        }
        Object param = args[1];
        if(param instanceof MapperMethod.ParamMap){
            MapperMethod.ParamMap map = (MapperMethod.ParamMap) param;
            Wrapper ew = null;
            if(map.containsKey(NAME_ENTITY_WRAPPER)){//mapper.update(updEntity, EntityWrapper<>(whereEntity);
                ew = (Wrapper) map.get(NAME_ENTITY_WRAPPER);
            }//else updateById(entity) -->> change updateById(entity) to updateById(@Param("et") entity)
            Object et = map.get(NAME_ENTITY);
            if(ew!=null){
                Object entity = ew.getEntity();
                if(entity!=null){
                    EntityField ef = getVersionField(entity.getClass());
                    Field versionField = ef==null?null:ef.getField();
                    if (versionField != null) {
                        Object originalVersionVal = versionField.get(entity);
                        if(originalVersionVal!=null){
                            versionField.set(et, getUpdatedVersionVal(originalVersionVal));
                        }
                    }
                }
            }else{
                String methodId = ms.getId();
                String updateMethodName = methodId.substring(ms.getId().lastIndexOf(".")+1);
                if(PARAM_UPDATE_METHOD_NAME.equals(updateMethodName)){//update(entity, null) -->> update all. ignore version
                    return invocation.proceed();
                }
                EntityField entityField = getVersionField(et.getClass());
                Field versionField = entityField==null?null:entityField.getField();
                Object originalVersionVal;
                if(versionField!=null && (originalVersionVal=versionField.get(et))!=null) {
                    TableInfo tableInfo = TableInfoHelper.getTableInfo(et.getClass());
                    Map<String,Object> entityMap = new HashMap<>();
                    List<EntityField> fields = getEntityFields(et.getClass());
                    for(EntityField ef : fields){
                        Field fd = ef.getField();
                        if(fd.isAccessible()) {
                            entityMap.put(fd.getName(), fd.get(et));
                            if (ef.isVersion()) {
                                versionField = fd;
                            }
                        }
                    }
                    String versionPropertyName = versionField.getName();
                    List<TableFieldInfo> fieldList = tableInfo.getFieldList();
                    String versionColumnName = entityField.getColumnName();
                    if(versionColumnName==null) {
                        for (TableFieldInfo tf : fieldList) {
                            if (versionPropertyName.equals(tf.getProperty())) {
                                versionColumnName = tf.getColumn();
                            }
                        }
                    }
                    if (versionColumnName != null) {
                        entityField.setColumnName(versionColumnName);
                        entityMap.put(versionField.getName(), getUpdatedVersionVal(originalVersionVal));
                        entityMap.put(MP_OPTLOCK_VERSION_ORIGINAL, originalVersionVal);
                        entityMap.put(MP_OPTLOCK_VERSION_COLUMN, versionColumnName);
                        map.put(NAME_ENTITY, entityMap);
                    }
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * This method provides the control for version value.<BR>
     * Returned value type must be the same as original one.
     *
     * @param originalVersionVal
     * @return updated version val
     */
    protected Object getUpdatedVersionVal(Object originalVersionVal){
        Class<?> versionValClass = originalVersionVal.getClass();
        if(long.class.equals(versionValClass)){
            return ((long)originalVersionVal)+1;
        }else if(Long.class.equals(versionValClass)){
            return ((Long)originalVersionVal)+1;
        }else if(int.class.equals(versionValClass)){
            return ((int)originalVersionVal)+1;
        }else if(Integer.class.equals(versionValClass)){
            return ((Integer)originalVersionVal)+1;
        }else if(Date.class.equals(versionValClass)){
            return new Date();
        }else if(Timestamp.class.equals(versionValClass)){
            return new Timestamp(System.currentTimeMillis());
        }else{
            return originalVersionVal;//not supported type, return original val.
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private EntityField getVersionField(Class<?> parameterClass) {
        synchronized (parameterClass.getName()) {
            if (versionFieldCache.containsKey(parameterClass)) {
                return versionFieldCache.get(parameterClass);
            }else{
                EntityField field = getVersionFieldRegular(parameterClass);
                versionFieldCache.put(parameterClass, field);
                return field;
            }
        }
    }

    private EntityField getVersionFieldRegular(Class<?> parameterClass){
        if (parameterClass != Object.class) {
            for (Field field : parameterClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Version.class)) {
                    field.setAccessible(true);
                    return new EntityField(field, true);
                }
            }
            return getVersionFieldRegular(parameterClass.getSuperclass());
        }
        return null;
    }

    private List<EntityField> getEntityFields(Class<?> parameterClass){
        if(entityFieldsCache.containsKey(parameterClass)){
            return entityFieldsCache.get(parameterClass);
        }else{
            List<EntityField> fields = getFieldsFromClazz(parameterClass, null);
            entityFieldsCache.put(parameterClass, fields);
            return fields;
        }
    }

    private List<EntityField> getFieldsFromClazz(Class<?> parameterClass, List<EntityField> fieldList){
        if(fieldList==null){
            fieldList = new ArrayList<>();
        }
        List<Field> fields = ReflectionKit.getFieldList(parameterClass);
        for(Field field:fields){
            field.setAccessible(true);
            if (field.isAnnotationPresent(Version.class)) {
                fieldList.add(new EntityField(field, true));
            }else{
                fieldList.add(new EntityField(field, false));
            }
        }
        return fieldList;
    }

}
class EntityField{

    private Field field;
    private boolean version;
    private String columnName;

    public EntityField(Field field, boolean version) {
        this.field = field;
        this.version = version;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}