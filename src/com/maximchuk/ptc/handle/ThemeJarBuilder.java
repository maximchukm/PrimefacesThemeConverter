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

    private FileEntity css;
    private List<FileEntity> images;

    public ThemeJarBuilder(FileEntity cssData, List<FileEntity> imagesData) {
        this.css = cssData;
        this.images = imagesData;
    }

    public void build(String outputDirName, String themeName) throws IOException {
        String cssDirName;
        String imagesDirName;
        StringBuilder fileStructureNameBuilder = new StringBuilder("temp");
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
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(jarNameBuilder.toString()));
        compressTheme("temp", null, out);
        out.close();
        System.out.print("cleaning temporary data... ");
        if (new File("temp").delete()) {
            System.out.println("ok");
        } else {
            System.out.println("error");
        }
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

}
