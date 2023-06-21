package document;

import util.FileIOUtil;

public class TextDataset {
    private ArtifactsCollection sourceCollection;
    private ArtifactsCollection targetCollection;
//    private SimilarityMatrix rtm;

    /**
     *
     * @param sourceDirPath: uc files' directory path
     * @param targetDirPath: class code files' directory path
     */
    public TextDataset(String sourceDirPath, String targetDirPath) {
        this.setSourceCollection(FileIOUtil.getCollections(sourceDirPath, ".txt"));
        this.setTargetCollection(FileIOUtil.getCollections(targetDirPath, ".txt"));

    }



    public ArtifactsCollection getSourceCollection() {
        return sourceCollection;
    }

    public void setSourceCollection(ArtifactsCollection sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    public ArtifactsCollection getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(ArtifactsCollection targetCollection) {
        this.targetCollection = targetCollection;
    }

}
