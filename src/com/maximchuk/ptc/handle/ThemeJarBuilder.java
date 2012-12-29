package com.maximchuk.ptc.handle;

import com.maximchuk.ptc.entity.FileEntity;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Builds Primefaces theme jar file
 *
 * @author Maxim L. Maximchuk
 */
public class ThemeJarBuilder {
    private static final String TEMP_DIR = "temp";

    private FileEntity css;
    private List<FileEntity> images;

    /**
     * Class constructor
     *
     * @param cssData css entity object
     * @param imagesData images entity list
     */
    public ThemeJarBuilder(FileEntity cssData, List<FileEntity> imagesData) {
        this.css = cssData;
        this.images = imagesData;
    }

    /**
     * Main method for build jar processing
     *
     * @param outputDirName result output directory
     * @param themeName theme name
     * @throws IOException
     */
    public void build(String outputDirName, String themeName) throws IOException {
        String cssDirName;
        String imagesDirName;
        if (new File(TEMP_DIR).exists()) {
            if (!cleaningTemp()) {
                return;
            }
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

        //creating files
        OutputStream os = new FileOutputStream(cssDirName + css.getFilename());
        os.write(css.getData());
        os.close();
        for (FileEntity image: images) {
            os = new FileOutputStream(imagesDirName + image.getFilename());
            os.write(image.getData());
            os.close();
        }

        //creating jar
        StringBuilder jarNameBuilder = new StringBuilder(outputDirName);
        mkdir(jarNameBuilder.toString());
        jarNameBuilder.append("/").append(themeName).append(".jar");
        if (new File(jarNameBuilder.toString()).exists()) {
            delete(new File(jarNameBuilder.toString()));
        }
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(jarNameBuilder.toString()));
        System.out.println("Creating theme jar...");
        compressTheme(TEMP_DIR, null, out);
        out.close();
        if (!cleaningTemp()) {
            return;
        }
        System.out.println("Complete! Check '" + new File(outputDirName).getAbsolutePath() +"' folder");
    }

    /**
     * Compressing file structure
     *
     * @param directory initiate directory
     * @param entryName current zip entry name
     * @param out zip output stream
     * @throws IOException
     */
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

    /**
     * Cleaning temporary files
     *
     * @return true if execute successful
     */
    private boolean cleaningTemp() {
        boolean isOk;
        System.out.print("Cleaning temporary data... ");
        if (delete(new File("temp"))) {
            System.out.println("ok");
            isOk = true;
        } else {
            System.out.println("error");
            isOk = false;
        }
        return isOk;
    }

    /**
     * Making empty directory
     *
     * @param dirName directory name
     */
    private void mkdir(String dirName) {
        File f = new File(dirName);
        f.mkdir();
    }

    /**
     * Deleting file or directory
     *
     * @param toDelete
     * @return
     */
    private boolean delete(File toDelete) {
        boolean isDeleted = true;
        if (toDelete.isDirectory()) {
            for (File f: toDelete.listFiles()) {
                isDeleted = delete(f);
                if (!isDeleted) {
                    break;
                }
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
