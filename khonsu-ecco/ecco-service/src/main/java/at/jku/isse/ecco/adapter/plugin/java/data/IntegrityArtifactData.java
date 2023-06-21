package at.jku.isse.ecco.adapter.plugin.java.data;

import at.jku.isse.ecco.artifact.ArtifactData;

import java.util.Objects;

/**
 * @ClassName: IntegrityArtifactData
 * @Description: 作为不变量来用，判断类和方法是否是分割出去的
 * @Author panpan
 */
public class IntegrityArtifactData implements ArtifactData {
    private boolean isPerfect;
    public IntegrityArtifactData(boolean isPerfect) {
        this.isPerfect = isPerfect;
    }

    @Override
    public String toString() {
        return this.isPerfect ? "perfect" : "imperfect";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isPerfect);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IntegrityArtifactData other = (IntegrityArtifactData) obj;
        if (isPerfect != other.isPerfect)
            return false;
        return true;
    }

}
