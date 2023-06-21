package util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import document.Artifact;
import document.ArtifactsCollection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileIOUtil {
    public static void abort(String m) {
        System.err.println(m);
        System.err.flush();
        Thread.dumpStack();
        System.exit(1);
    }

    @Nullable
    public static String readFile(@NotNull String path) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return Charset.forName("UTF-8").decode(ByteBuffer.wrap(encoded)).toString();
        } catch (IOException e) {
            return null;
        }
    }

    public static void writeFile(@NotNull String input, String path) {
        Path outPath = Paths.get(path);
        Charset charset = Charset.forName("UTF-8");

        try (BufferedWriter writer = Files.newBufferedWriter(outPath, charset)) {
            writer.write(input, 0, input.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    public static ArtifactsCollection getCollections(String dirPath, String postfixName) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            abort("Artifacts directory doesn't exist");
        }

        if (!dirFile.isDirectory()) {
            abort("Artifacts path should be a directory");
        }

        ArtifactsCollection collections = new ArtifactsCollection();
        for (File f1 : dirFile.listFiles()) {
            for(File f:f1.listFiles()){
                if (f.getName().endsWith(postfixName)) {
                    String id = f1.getName()+"&&"+f.getName().split(".txt")[0];
                    // warning!! jsp文件名 存在“-”字符的编码问题
                    id = id.replace("‐", "-");
//                System.out.println(id);

                    Artifact artifact = new Artifact(id, FileIOUtil.readFile(f.getPath()));
                    collections.put(id, artifact);
                }
            }

        }

//        System.out.println(collections.size() + " " + postfixName + " collections improted.");
        return collections;
    }


}
