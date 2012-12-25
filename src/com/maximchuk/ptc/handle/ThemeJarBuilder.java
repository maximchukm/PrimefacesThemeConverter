package com.maximchuk.ptc.handle;

import java.io.InputStream;
import java.util.List;

/**
 * @author Maxim L. Maximchuk
 *         Date: 25.12.12
 */
public class ThemeJarBuilder {

    private InputStream cssInputStream;
    private List<InputStream> imagesInputStreamList;

    public ThemeJarBuilder(InputStream cssInputStream, List<InputStream> imagesInputStreamList) {
        this.cssInputStream = cssInputStream;
        this.imagesInputStreamList = imagesInputStreamList;
    }

    public void build() {

    }

}
