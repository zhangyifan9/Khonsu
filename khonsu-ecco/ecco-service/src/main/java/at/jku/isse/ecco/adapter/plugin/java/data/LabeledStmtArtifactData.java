package at.jku.isse.ecco.adapter.plugin.java.data;

/**
 * @ClassName: LabeledStmtArtifactData
 * @Description: TODO
 * @Author panpan
 */
public class LabeledStmtArtifactData extends BlockArtifactData{
    String label;

    public LabeledStmtArtifactData(String block, String label) {
        super(block);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LabeledStmtArtifactData other = (LabeledStmtArtifactData) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return true;
    }
}
