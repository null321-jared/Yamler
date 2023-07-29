package net.cubespace.Yamler.Config.Converter;

import net.cubespace.Yamler.Config.*;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class List implements Converter {
    private final InternalConverter internalConverter;

    public List(InternalConverter internalConverter) {
        this.internalConverter = internalConverter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        java.util.List<Object> values = (java.util.List<Object>) obj;
        java.util.List<Object> newList = new ArrayList<>();

        for (Object val : values) {
            Converter converter = internalConverter.getConverter(val.getClass());

            if (converter != null)
                newList.add(converter.toConfig(val.getClass(), val, null));
            else
                newList.add(val);
        }

        return newList;
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
        java.util.List<Object> newList = new ArrayList<>();
        try {
            newList = ((java.util.List<Object>) type.newInstance());
        } catch (Exception e) {
        }

        java.util.List<Object> values = (java.util.List<Object>) section;

        if (genericType != null && genericType.getActualTypeArguments()[0] instanceof Class) {
            Converter converter = internalConverter.getConverter((Class<?>) genericType.getActualTypeArguments()[0]);

            if (converter != null) {
                for (Object value : values) {
                    newList.add(converter.fromConfig((Class<?>) genericType.getActualTypeArguments()[0], value, null));
                }
            } else {
                newList = values;
            }
        } else {
            newList = values;
        }

        return newList;
    }

    @Override
    public boolean supports(Class<?> type) {
        return java.util.List.class.isAssignableFrom(type);
    }
}
