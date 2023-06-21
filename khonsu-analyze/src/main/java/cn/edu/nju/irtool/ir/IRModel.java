package cn.edu.nju.irtool.ir;

import cn.edu.nju.irtool.document.ArtifactsCollection;
import cn.edu.nju.irtool.document.SimilarityMatrix;
import cn.edu.nju.irtool.document.TermDocumentMatrix;

public interface IRModel {
    public SimilarityMatrix Compute(ArtifactsCollection source, ArtifactsCollection target);

//    public SimilarityMatrix Compute(ArtifactsCollection source, ArtifactsCollection target);

    public TermDocumentMatrix getTermDocumentMatrixOfQueries();

    public TermDocumentMatrix getTermDocumentMatrixOfDocuments();
}
