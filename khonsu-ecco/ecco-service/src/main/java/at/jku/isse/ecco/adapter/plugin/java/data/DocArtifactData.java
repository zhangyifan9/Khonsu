package at.jku.isse.ecco.adapter.plugin.java.data;

import at.jku.isse.ecco.artifact.ArtifactData;

import java.util.Objects;

public class DocArtifactData implements ArtifactData {

	private String doc;

	public DocArtifactData(String doc) {
		this.doc = doc;
	}

	public String getName() {
		return doc;
	}

	public void setName(String doc) {
		this.doc = doc;
	}

	@Override
	public String toString() {
		return this.doc;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.doc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocArtifactData other = (DocArtifactData) obj;
		if (doc == null) {
			if (other.doc != null)
				return false;
		} else if (!doc.equals(other.doc))
			return false;
		return true;
	}

}
