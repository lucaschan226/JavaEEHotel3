package com.bcu.homeroom.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateValueProcessor implements JsonValueProcessor {
    //���ڸ�ʽ
    private String format = "yyyy-MM-dd HH:mm:ss";

    public JsonDateValueProcessor() {
    }

    public JsonDateValueProcessor(String format) {
        this.format = format;
    }

    public Object processArrayValue(Object o, JsonConfig jsonConfig) {
        return process(o);
    }

    public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
        return process(o);
    }

    private Object process(Object value) {
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }
}
