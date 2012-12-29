package com.maximchuk.ptc.entity;

/**
 * @author Maxim L. Maximchuk
 */
public class CssPropertyEntity {

    private CssPropertyEnum type;
    private String value;

    public CssPropertyEntity(CssPropertyEnum type, String value) {
        this.type = type;
        this.value = value;
    }

    public CssPropertyEnum getType() {
        return type;
    }

    public void setType(CssPropertyEnum type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
