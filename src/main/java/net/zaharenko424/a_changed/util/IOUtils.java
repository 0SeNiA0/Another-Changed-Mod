package net.zaharenko424.a_changed.util;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import org.apache.commons.lang3.function.TriConsumer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {

    public static void visitAllFiles(TriConsumer<String, File, String> fileConsumer, File dir, String relativePath){
        File[] files = dir.listFiles();
        if(files == null) return;

        String filename;
        for(File f : files){
            filename = f.getName();
            if(f.isDirectory()){
                visitAllFiles(fileConsumer, f, relativePath + filename + "/");
                continue;
            }
            fileConsumer.accept(relativePath, f, filename);
        }
    }

    public static void visitAllFilesNoRecursion(TriConsumer<String, File, String> fileConsumer, File dir, String relativePath){
        File[] files = dir.listFiles();
        if(files == null) return;

        List<Pair<File, String>> dirStack = new ArrayList<>();
        dirStack.add(ObjectObjectMutablePair.of(dir, relativePath));

        String path;
        Pair<File, String> currentDir;
        while (!dirStack.isEmpty()){
            currentDir = dirStack.removeLast();
            files = currentDir.first().listFiles();
            if(files == null) continue;
            path = currentDir.second();

            for(File file : files){
                if(file.isDirectory()) {
                    dirStack.add(ObjectObjectMutablePair.of(file, path + file.getName() + "/"));
                } else fileConsumer.accept(path, file, file.getName());
            }
        }
    }
}
