package com.maximchuk.ptc.entity;

/**
 * @author Maxim L. Maximchuk
 */
public enum CssPropertyEnum {
    INPUT_TEXT, SELECT_ONE_MENU;

    public String getPropertyKey() {
        String key;
        switch (this) {
            case INPUT_TEXT: key = "input.text"; break;
            case SELECT_ONE_MENU: key = "select.one.menu"; break;
            default: throw new IllegalArgumentException("Property key for '" + this.name() + "' does`t assigned");
        }
        return key;
    }

    public String getCssClass() {
        String cssClass;
        switch (this) {
            case INPUT_TEXT: cssClass = ".ui-inputfield, .ui-widget-content .ui-inputfield, .ui-widget-header .ui-inputfield"; break;
            case SELECT_ONE_MENU: cssClass = ".ui-selectonemenu, .ui-selectonemenu.ui-widget"; break;
            default: throw new IllegalArgumentException("Css class for '" + this.name() + "' does`t assigned");
        }
        return cssClass;
    }

}
