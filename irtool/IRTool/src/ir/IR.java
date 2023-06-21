package ir;

import document.SimilarityMatrix;
import document.SingleLink;
import document.TextDataset;

import java.io.FileWriter;
import java.io.IOException;

public class IR {

    public static void compute(TextDataset textDataset, String modelType) {


        try {
            Class modelTypeClass = Class.forName(modelType);
            IRModel irModel = (IRModel) modelTypeClass.newInstance();
            // 计算得到IR候选追踪矩阵
            SimilarityMatrix similarityMatrix = irModel.Compute(textDataset.getSourceCollection(),
                    textDataset.getTargetCollection());
            // TODO 2018-8-26
//			for (SingleLink link : similarityMatrix.allLinks()) {
//				System.out.println(IR.class.getName()+"/compute/line29: "+link.getSourceArtifactId()+" "+link.getTargetArtifactId()+" "+link.getScore());
//			}


            FileWriter fileWriter = new FileWriter("D:\\nju\\irtool\\IRTool\\data\\relation.txt");
            String lastsource="";
            int i=0;
            for (SingleLink link : similarityMatrix.allLinks()) {
                String source = link.getSourceArtifactId();
                String target = link.getTargetArtifactId();
                if(!source.equals(lastsource)){
                    i=0;
                }
                if(i<10){
                    i++;
                    fileWriter.write(source+"&&"+target+"&&"+link.getScore()+"\r\n");
                    System.out.println(source+"  "+target+"  "+link.getScore());
                }
                lastsource=source;
//                similarityMatrix.addLink(source, target, link.getScore());
//                if (textDataset.getRtm().sourceArtifactsIds().contains(source) && textDataset.getRtm().targetArtifactsIds().contains(target)) {
//                    similarityMatrix.addLink(source, target, link.getScore());
//                }
            }

            fileWriter.close();
			/*
			 * add a code line
			 */


        } catch (ClassNotFoundException e) {
            System.out.println("No such IR model exists");
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
