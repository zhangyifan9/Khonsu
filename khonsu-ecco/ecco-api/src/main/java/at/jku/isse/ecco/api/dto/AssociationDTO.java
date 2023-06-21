package at.jku.isse.ecco.api.dto;

//TODO:改动
public class AssociationDTO {
    String id;
    String condition;
    Integer numArtifacts;

    public String getId() {
        return id;
    }

    public String getCondition() {
        return condition;
    }

    public Integer getNumArtifacts() {
        return numArtifacts;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setNumArtifacts(Integer numArtifacts) {
        this.numArtifacts = numArtifacts;
    }
}
