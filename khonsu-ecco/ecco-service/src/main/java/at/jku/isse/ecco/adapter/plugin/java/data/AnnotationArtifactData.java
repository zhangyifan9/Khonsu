package at.jku.isse.ecco.adapter.plugin.java.data;

import at.jku.isse.ecco.artifact.ArtifactData;

import java.util.Objects;

public class AnnotationArtifactData implements ArtifactData {

    private String annotation;

    public AnnotationArtifactData(String annotation) {
        this.annotation = annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getAnnotation() {
        return this.annotation;
    }

    @Override
    public String toString() {
        return this.annotation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.annotation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnnotationArtifactData other = (AnnotationArtifactData) obj;
        if (annotation == null) {
            if (other.annotation != null)
                return false;
        } else if (!annotation.equals(other.annotation))
            return false;
        return true;
    }
}
