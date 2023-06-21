package at.jku.isse.ecco.adapter.plugin.java.data;

import at.jku.isse.ecco.artifact.ArtifactData;

import java.util.Objects;

public class ClassArtifactData implements ArtifactData {

	private String name;

	private String fullName;

	public ClassArtifactData(String name, String fullName) {
		this.name = name;
		this.fullName = fullName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassArtifactData other = (ClassArtifactData) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		return true;
	}

}
