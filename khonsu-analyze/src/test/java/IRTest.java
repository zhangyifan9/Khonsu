import cn.edu.nju.irtool.document.TextDataset;
import cn.edu.nju.irtool.preprocess.DataPreprecess;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName IRTest
 * @Description TODO
 * @Author panpan
 */
public class IRTest {
    @Test
    public void IRCompute() throws IOException {
        long startTime = System.currentTimeMillis();

        DataPreprecess dataPreprecess = new DataPreprecess("spring-framework", "D:\\Workplace\\nju\\tmp");
        List<String> paths = new ArrayList<>();
        paths.add(dataPreprecess.getClassProcessedPath());
        paths.add(dataPreprecess.getClassPath());
        paths.add(dataPreprecess.getUcPreProcessedPath());
        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                FileUtils.deleteDirectory(file);
            }
            file.mkdirs();
        }

        String irModelName = "cn.edu.nju.irtool.ir.VSM";
        dataPreprecess.arrangeData();
        TextDataset textDataset = new TextDataset(dataPreprecess.getUcPreProcessedPath(), dataPreprecess.getClassProcessedPath());
//        TextDataset textDataset = new TextDataset(dataProcess.getUcPreProcessedPath(),dataProcess.getClassProcessedPath());
//         ir.compute(textDataset, irModelName, "spring-framework");

        long endTime = System.currentTimeMillis();
        System.out.println("time cost:" + (endTime - startTime) * 1.0 / 1000 / 60);
    }

}
