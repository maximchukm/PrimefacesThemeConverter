package com.maximchuk.ptc.entity;

/**
 * File entity
 * contain file name string and file data byte array
 *
 * @author Maxim L. Maximchuk
 */
public class FileEntity {

    private String filename;
    private byte[] data;

    /**
     * Entity constructor
     *
     * @param filename file name
     * @param data file data in byte array
     */
    public FileEntity(String filename, byte[] data) {
        this.filename = filename;
        this.data = data;
    }

    /**
     * Getting file name
     *
     * @return file name string
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Setting file name
     *
     * @param filename file name string
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Getting file data
     *
     * @return file data in byte array
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Setting file data
     *
     * @param data file data in byte array
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
