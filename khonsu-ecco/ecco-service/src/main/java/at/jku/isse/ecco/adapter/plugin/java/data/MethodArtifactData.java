package at.jku.isse.ecco.adapter.plugin.java.data;

import at.jku.isse.ecco.artifact.ArtifactData;

import java.util.Objects;

public class MethodArtifactData implements ArtifactData {

	private String signature;
	private String fullName;

	public MethodArtifactData(String signature, String fullName) {
		this.signature = signature;
		this.fullName = fullName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return this.signature;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.signature);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodArtifactData other = (MethodArtifactData) obj;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		return true;
	}

}
