package org.springcat.carrycat.core.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

/**
 * @Description CarryCatUtil
 * @Author springCat
 * @Date 2020/10/12 11:21
 */
@UtilityClass
public class CarryCatUtil {

    public <T> T newInstance(String className){
        return ReflectUtil.newInstance(className);
    }

    public <T> T newInstance(String className, Object... params){
        Class<T> cls = ClassUtil.loadClass(className);
        return ReflectUtil.newInstance(cls, params);
    }

    public String getName(Class<?> cls){
        String className = cls.getSimpleName();
        className = StrUtil.removeSuffix(className, "Conf");
        className = StrUtil.lowerFirst(className);
        return className;
    }

}
