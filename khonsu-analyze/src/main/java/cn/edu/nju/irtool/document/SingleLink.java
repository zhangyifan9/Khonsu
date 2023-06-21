package cn.edu.nju.irtool.document;

/**
 * Created by niejia on 15/2/10.
 */
public class SingleLink implements Comparable<SingleLink> {
	// UC1 AddPatientAction 1.0

    private String sourceArtifactId;
    private String targetArtifactId;
    private Double score;

    public SingleLink(String sourceArtifactId, String targetArtifactId, Double score) {
        this.setSourceArtifactId(sourceArtifactId);
        this.setTargetArtifactId(targetArtifactId);
        this.setScore(score);
    }

    @Override
    public int compareTo(SingleLink o) {
        return getScore().compareTo(o.getScore());
    }

    @Override
    public int hashCode() {
        int sourceArtifacyhash = sourceArtifactId.hashCode();
        int targetArtifactHash = targetArtifactId.hashCode();
        int probabilityHash = score.hashCode();

        int hash = sourceArtifacyhash ^ targetArtifactHash ^ probabilityHash;

        return hash;
    }

    @Override
    public boolean equals(Object object) {
        SingleLink other = (SingleLink) object;
        if (other != null) {
            return ((sourceArtifactId.equals(other.sourceArtifactId)
                    && (targetArtifactId.equals(other.targetArtifactId)
                    && (score.equals(other.score)))));
        }
        return false;
    }

    public String getSourceArtifactId() {
        return sourceArtifactId;
    }

    public void setSourceArtifactId(String sourceArtifactId) {
        this.sourceArtifactId = sourceArtifactId;
    }

    public String getTargetArtifactId() {
        return targetArtifactId;
    }

    public void setTargetArtifactId(String targetArtifactId) {
        this.targetArtifactId = targetArtifactId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(sourceArtifactId);
        sb.append(" ");
        sb.append(targetArtifactId);
        sb.append(" ");
        sb.append(score);
        return sb.toString();
    }
}

