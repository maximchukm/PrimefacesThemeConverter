package com.maximchuk.ptc.handle;

import com.maximchuk.ptc.entity.FileEntity;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Maxim L. Maximchuk
 *         Date: 25.12.12
 */
public class ThemeJarBuilder {
    private static final String TEMP_DIR = "temp";

    private FileEntity css;
    private List<FileEntity> images;

    public ThemeJarBuilder(FileEntity cssData, List<FileEntity> imagesData) {
        this.css = cssData;
        this.images = imagesData;
    }

    public void build(String outputDirName, String themeName) throws IOException {
        String cssDirName;
        String imagesDirName;
        if (new File(TEMP_DIR).exists()) {
            cleaningTemp();
        }
        StringBuilder fileStructureNameBuilder = new StringBuilder(TEMP_DIR);
        mkdir(fileStructureNameBuilder.toString());
        fileStructureNameBuilder.append("/META-INF/");
        mkdir(fileStructureNameBuilder.toString());
        fileStructureNameBuilder.append("resources/");
        mkdir(fileStructureNameBuilder.toString());
        fileStructureNameBuilder.append("primefaces-").append(themeName).append("/");
        mkdir(fileStructureNameBuilder.toString());
        cssDirName = fileStructureNameBuilder.toString();
        fileStructureNameBuilder.append("images/");
        mkdir(fileStructureNameBuilder.toString());
        imagesDirName = fileStructureNameBuilder.toString();

        //create files
        OutputStream os = new FileOutputStream(cssDirName + css.getFilename());
        os.write(css.getData());
        os.close();
        for (FileEntity image: images) {
            os = new FileOutputStream(imagesDirName + image.getFilename());
            os.write(image.getData());
            os.close();
        }

        //Create jar
        StringBuilder jarNameBuilder = new StringBuilder(outputDirName);
        mkdir(jarNameBuilder.toString());
        jarNameBuilder.append("/").append(themeName).append(".jar");
        if (new File(jarNameBuilder.toString()).exists()) {
            delete(new File(jarNameBuilder.toString()));
        }
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(jarNameBuilder.toString()));
        compressTheme(TEMP_DIR, null, out);
        out.close();
        cleaningTemp();
        System.out.println("complete!");
    }

    private void mkdir(String dirName) {
        File f = new File(dirName);
        f.mkdir();
    }

    private void compressTheme(String directory, String entryName, ZipOutputStream out) throws IOException {
        File files = new File(directory);
        String[] contents = files.list();
        for (String content : contents) {
            File f = new File(directory, content);
            String name = entryName == null ? f.getName() : entryName + "/" + f.getName();
            System.out.println("processing entry: " + name);
            if (f.isDirectory()) {
                out.putNextEntry(new ZipEntry(name + "/"));
                compressTheme(f.getPath(), name, out);
            } else {
                FileInputStream in = new FileInputStream(f);
                out.putNextEntry(new ZipEntry(name));

                int len;
                byte[] data = new byte[in.available()];
                while ((len = in.read(data)) > 0) {
                    out.write(data, 0, len);
                }
                out.flush();
                out.closeEntry();
                in.close();
            }
        }
    }

    private void cleaningTemp() {
        System.out.print("cleaning temporary data... ");
        if (delete(new File("temp"))) {
            System.out.println("ok");
        } else {
            System.out.println("error");
        }
    }

    private boolean delete(File toDelete) {
        boolean isDeleted = true;
        if (toDelete.isDirectory()) {
            for (File f: toDelete.listFiles()) {
                isDeleted = delete(f);
            }
            if (isDeleted) {
                isDeleted = toDelete.delete();
            }
        } else {
            isDeleted = toDelete.delete();
        }
        return isDeleted;
    }

}
