package at.jku.isse.ecco.adapter.plugin.java.data;

public class DoStmtArtifactData extends BlockArtifactData{

    private String condition;

    public DoStmtArtifactData(String block,String condition) {
        super(block);
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
