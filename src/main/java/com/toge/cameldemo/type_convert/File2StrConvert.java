package com.toge.cameldemo.type_convert;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;

public class File2StrConvert extends TypeConverterSupport {

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        return (T) (value + "123");
    }
}