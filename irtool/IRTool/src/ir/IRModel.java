package ir;

import document.ArtifactsCollection;
import document.SimilarityMatrix;
import document.TermDocumentMatrix;

public interface IRModel {
    public SimilarityMatrix Compute(ArtifactsCollection source, ArtifactsCollection target);

//    public SimilarityMatrix Compute(ArtifactsCollection source, ArtifactsCollection target);

    public TermDocumentMatrix getTermDocumentMatrixOfQueries();

    public TermDocumentMatrix getTermDocumentMatrixOfDocuments();
}
