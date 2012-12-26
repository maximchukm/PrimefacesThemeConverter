package com.maximchuk.ptc.entity;

/**
 * @author Maxim L. Maximchuk
 *         Date: 26.12.12
 */
public class FileEntity {

    private String filename;
    private byte[] data;

    public FileEntity(String filename, byte[] data) {
        this.filename = filename;
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
