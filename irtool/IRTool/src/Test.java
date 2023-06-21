import document.TextDataset;
import ir.IR;
import preprocess.DataPreprecess;

public class Test {
    public static void main(String[] args){
        long startTime = System.currentTimeMillis();

        String irModelName = "ir.VSM";
        DataPreprecess dataProcess = new DataPreprecess();
        dataProcess.arrangeData();
        IR ir = new IR();
        TextDataset textDataset = new TextDataset(dataProcess.getClassProcessedPath(),dataProcess.getUcPreProcessedPath());
//        TextDataset textDataset = new TextDataset(dataProcess.getUcPreProcessedPath(),dataProcess.getClassProcessedPath());
        ir.compute(textDataset,irModelName);

        long endTime = System.currentTimeMillis();
        System.out.println("time cost:" + (endTime - startTime) * 1.0 / 1000 / 60);
    }
}
