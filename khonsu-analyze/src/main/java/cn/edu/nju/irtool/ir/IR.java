package cn.edu.nju.irtool.ir;

import cn.edu.nju.domain.entity.CodeBlock;
import cn.edu.nju.domain.entity.RCLink;
import cn.edu.nju.domain.entity.Release;
import cn.edu.nju.irtool.document.ArtifactsCollection;
import cn.edu.nju.irtool.document.SimilarityMatrix;
import cn.edu.nju.irtool.document.SingleLink;
import cn.edu.nju.irtool.document.TextDataset;
import redis.clients.jedis.Jedis;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IR {
    public static List<RCLink> compute(TextDataset textDataset, String modelType, String repoName, Map<String, Release> sourceMap, Map<String, CodeBlock> targetMap) {
        List<RCLink> rcLinks = new ArrayList<>();
        // 相似度保留小数点后五位
        DecimalFormat df = new DecimalFormat("0.00000");
        try {
            Jedis jedis = new Jedis("localhost", 6379);
            // IR模型加载
            Class modelTypeClass = Class.forName(modelType);
            IRModel irModel = (IRModel) modelTypeClass.newInstance();
            ArtifactsCollection targetCollection = textDataset.getTargetCollection();

            // rn 一般不会太多，但代码块可能很多，分批处理相似度矩阵计算
            int batchSize = 1000;
            Map<Integer, ArtifactsCollection> batches = IntStream.range(0, (targetCollection.size() + batchSize - 1) / batchSize)
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> i,
                            i -> targetCollection.entrySet().stream()
                                    .skip(i * batchSize)
                                    .limit(batchSize)
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, ArtifactsCollection::new))
                    ));
            batches.forEach((batchNumber, batch) -> {
                // 计算得到IR候选追踪矩阵
                SimilarityMatrix similarityMatrix = irModel.Compute(textDataset.getSourceCollection(), batch);
                for (SingleLink link : similarityMatrix.allLinks()) {
                    String source = link.getSourceArtifactId();
                    String target = link.getTargetArtifactId();
                    System.out.println(source + "  " + target + "  " + link.getScore());
                    Release release = sourceMap.get(source);
                    CodeBlock codeBlock = targetMap.get(target);
                    // 该代码片段不是此版本的增量，与此版本的rn无关
                    String[] tags = codeBlock.getTags().split("&");
                    boolean delete = (Integer.parseInt(jedis.hget(repoName, tags[0])) - 1 == Integer.parseInt(jedis.hget(repoName, release.getTag())));
                    boolean add = tags[tags.length-1].equals(release.getTag());
                    if (!(add || delete)) continue;
                    rcLinks.add(new RCLink(0L, repoName, release.getId(), codeBlock.getId(), df.format(link.getScore()), false));
                }
            });

            jedis.close();

        } catch (ClassNotFoundException e) {
            System.out.println("No such IR model exists");
            e.printStackTrace();
            return rcLinks;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return rcLinks;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return rcLinks;
        }

        // 排序，对于对应同一个release的codeBlock，相似度得分高的在前
        Map<Long, List<RCLink>> rcLinksByReleaseId = rcLinks.stream()
                .collect(Collectors.groupingBy(RCLink::getReleaseId));

        List<RCLink> sortedRcLinks = new ArrayList<>();
        for (Map.Entry<Long, List<RCLink>> entry : rcLinksByReleaseId.entrySet()) {
            List<RCLink> rcLinksForRelease = entry.getValue();
            rcLinksForRelease.sort(Comparator.comparing(RCLink::getScore).reversed());
            sortedRcLinks.addAll(rcLinksForRelease);
        }

        return rcLinks;
    }

    public static boolean isApproximatelyZero(double x) {
        return Math.abs(x) < 0.0000000001;
    }
}
