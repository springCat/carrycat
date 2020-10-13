package org.springcat.carrycat.impl.transformer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @Description MappingLine
 * @Author springCat
 * @Date 2020/10/13 12:58
 */
@Data
public class MappingColumn {

    private final static Dict typeDict  = defaultTypeDict();

    private String name;

    private int index;

    private Type type;

    private Object defaultValue;

    // columnName=index,type,defaultValue
    public boolean init(String columnName,String line){
        this.name = columnName;
        String[] typeMappingLine = StrUtil.split(line, ",");

        this.index = Convert.toInt(ArrayUtil.get(typeMappingLine,0), -1);
        this.type = (Type) typeDict.get(ArrayUtil.get(typeMappingLine,1));
        this.defaultValue = Convert.convertQuietly(type, ArrayUtil.get(typeMappingLine, 2), null);

        boolean isIndexTypeValid = index > -1 && type != null;
        boolean isDefaultValueValid = defaultValue != null;
        return isIndexTypeValid || isDefaultValueValid;
    }

    public Object getValue(String[] rawData){
        return Convert.convertQuietly(type, ArrayUtil.get(rawData, index), defaultValue);
    }

    private static Dict defaultTypeDict(){
        Dict dict = new Dict();
        putType(dict,Integer.class);
        putType(dict,Long.class);
        putType(dict, Date.class);
        putType(dict,Float.class);
        putType(dict,Double.class);
        putType(dict,String.class);
        putType(dict,Boolean.class);
        //添加常用别名
        dict.put("int",Integer.class);
        return dict;
    }

    private static void putType(Dict dict, Type type){
        dict.put(type.getTypeName(),type);
        dict.put(StrUtil.lowerFirst(StrUtil.subAfter(type.getTypeName(),".",true)),type);
    }
}
