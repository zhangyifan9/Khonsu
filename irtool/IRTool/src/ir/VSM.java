package ir;

import document.*;

import java.util.Collections;
import java.util.List;

public class VSM implements IRModel{
    private TermDocumentMatrix queries;
    private TermDocumentMatrix documents;

    // 2019-1-9
    private TermDocumentMatrix termsNumOfSource;
    private TermDocumentMatrix termsNumOfTarget;
    private TermDocumentMatrix mergedTermNumMatrix;

    public SimilarityMatrix Compute(ArtifactsCollection source, ArtifactsCollection target) {
        ArtifactsCollection bothSourceAndTarget = new ArtifactsCollection();
        bothSourceAndTarget.putAll(source);
        bothSourceAndTarget.putAll(target);
        return Compute(new TermDocumentMatrix(source), new TermDocumentMatrix(target), new TermDocumentMatrix(bothSourceAndTarget));
    }



    public SimilarityMatrix Compute(TermDocumentMatrix source, TermDocumentMatrix target, TermDocumentMatrix both) {

        this.mergedTermNumMatrix = both;
        List<TermDocumentMatrix> matrices = TermDocumentMatrix.Equalize(source, target);
        this.termsNumOfSource = matrices.get(0);
        this.termsNumOfTarget = matrices.get(1);

        // 计算词频
        TermDocumentMatrix TF = ComputeTF(both);

        // 计算逆向文本频率
        double[] IDF = ComputeIDF(ComputeDF(both), both.NumDocs());

        // 计算TF-IDF
        TermDocumentMatrix TFIDF = ComputeTFIDF(TF, IDF);

        TermDocumentMatrix sourceIDs = ComputeIdentities(source);
        TermDocumentMatrix targetIDs = ComputeIdentities(target);

        TermDocumentMatrix sourceWithTFIDF = ReplaceIDWithTFIDF(sourceIDs, TFIDF);
        TermDocumentMatrix targetWithTFIDF = ReplaceIDWithTFIDF(targetIDs, TFIDF);

        return ComputeSimilarities(sourceWithTFIDF, targetWithTFIDF);
    }

    private TermDocumentMatrix ReplaceIDWithTFIDF(TermDocumentMatrix ids, TermDocumentMatrix tfidf) {

//        double[][] idsMatrix = ids.getMatrix();
//        List<String> idsDocIndex = ids.getDocIndex();
//        Map<String, Integer> idsDocIndexLookup = new HashMap<>(ids.getDocIndex());
//
//        ids.getMatrix() = new double[tfidf.NumDocs()][];
//
        for (int i = 0; i < ids.NumDocs(); i++) {
            for (int j = 0; j < ids.NumTerms(); j++) {
                ids.setValue(i, j, tfidf.getValue(ids.getDocumentName(i), ids.getTermName(j)));
            }
        }
//
        return ids;
//        List<TermDocumentMatrix> matrices = TermDocumentMatrix.Equalize(ids, tfidf);
//        return matrices.get(0);

    }

    private TermDocumentMatrix ComputeTFIDF(TermDocumentMatrix tf, double[] idf) {
        for (int i = 0; i < tf.NumDocs(); i++) {
            for (int j = 0; j < tf.NumTerms(); j++) {
                tf.setValue(i, j, tf.getValue(i, j) * idf[j]);
            }
        }
        return tf;
    }

    private double[] ComputeIDF(double[] df, int numDocs) {
        double[] idf = new double[df.length];
        for (int i = 0; i < df.length; i++) {
            if (df[i] <= 0.0) {
                idf[i] = 0.0;
            } else {
                idf[i] = Math.log(numDocs / df[i]);
            }
        }
        return idf;
    }

    // 计算某一个词出现在多少个文本当中
    private double[] ComputeDF(TermDocumentMatrix matrix) {
        double[] df = new double[matrix.NumTerms()];
        for (int j = 0; j < matrix.NumTerms(); j++) {
            df[j] = 0.0;
            for (int i = 0; i < matrix.NumDocs(); i++) {
                df[j] += (matrix.getValue(i, j) > 0.0) ? 1.0 : 0.0;
            }
        }
        return df;
    }

    // 计算某个词在每个文档中出现的频率
    private TermDocumentMatrix ComputeTF(TermDocumentMatrix matrix) {
        for (int i = 0; i < matrix.NumDocs(); i++) {
            double max = 0.0;
            for (int k = 0; k < matrix.NumTerms(); k++) {
                max += matrix.getValue(i, k);
            }

            for (int j = 0; j < matrix.NumTerms(); j++) {
                matrix.setValue(i, j, (matrix.getValue(i, j) / max));
            }
        }
        return matrix;
    }

    // 确认文本中是否出现某个词，出现则设为1.0
    private TermDocumentMatrix ComputeIdentities(TermDocumentMatrix matrix) {
        for (int i = 0; i < matrix.NumDocs(); i++) {
            for (int j = 0; j < matrix.NumTerms(); j++) {
                matrix.setValue(i, j, ((matrix.getValue(i, j) > 0.0) ? 1.0 : 0.0));
            }
        }
        return matrix;
    }

    private SimilarityMatrix ComputeSimilarities(TermDocumentMatrix ids, TermDocumentMatrix tfidf) {
        SimilarityMatrix sims = new SimilarityMatrix();
        List<TermDocumentMatrix> matrices = TermDocumentMatrix.Equalize(ids, tfidf);

        queries = matrices.get(0);
        documents = matrices.get(1);

        for (int i = 0; i < ids.NumDocs(); i++) {
            LinksList links = new LinksList();
            for (int j = 0; j < tfidf.NumDocs(); j++) {
                double product = 0.0;
                double asquared = 0.0;
                double bsquared = 0.0;
                for (int k = 0; k < matrices.get(0).NumTerms(); k++) {
                    double a = matrices.get(0).getValue(i, k);
                    double b = matrices.get(1).getValue(j, k);
                    product += (a * b);
                    asquared += Math.pow(a, 2);
                    bsquared += Math.pow(b, 2);
                }
                double cross = Math.sqrt(asquared) * Math.sqrt(bsquared);
                if (cross == 0.0) {
                    links.add(new SingleLink(ids.getDocumentName(i).trim(), tfidf.getDocumentName(j).trim(), 0.0));
                } else {
                    links.add(new SingleLink(ids.getDocumentName(i), tfidf.getDocumentName(j), product / cross));
                }
            }

            Collections.sort(links, Collections.reverseOrder());

            for (SingleLink link : links) {
                sims.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            }
        }

        sims.setSourceTermMatrix(queries);
        sims.setTargetTermMatrix(documents);

        // 2019-1-9
        sims.setMergedTermNumMatrix(mergedTermNumMatrix);
        sims.setSourceTermNumMatrix(termsNumOfSource);
        sims.setTargetTermNumMatrix(termsNumOfTarget);

        return sims;
    }

    @Override
    public TermDocumentMatrix getTermDocumentMatrixOfQueries() {
        return queries;
    }

    @Override
    public TermDocumentMatrix getTermDocumentMatrixOfDocuments() {
        return documents;
    }
}
