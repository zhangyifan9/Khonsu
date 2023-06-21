package at.jku.isse.ecco.api.dto;

import java.io.Serializable;
import java.util.ArrayList;

//TODO:改动
public class NodeDTO implements Serializable {
    public static final long serialVersionUID = 1234567890L;
    String name;
    ArrayList<NodeDTO> childs;
    boolean isUnique=false;

    private String uuid;

    private String javaFileId;

    public String getJavaFileId() {
        return javaFileId;
    }

    public void setJavaFileId(String javaFileId) {
        this.javaFileId = javaFileId;
    }

    private String dir;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isIn() {
        return isUnique;
    }

    public void setIn(boolean isUnique) {
        isUnique = isUnique;
    }

    public NodeDTO(){
        childs=new ArrayList<>();
    }
    public NodeDTO(String name,boolean isUnique){
        this.name=name;
        childs=new ArrayList<>();
        this.isUnique=isUnique;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<NodeDTO> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<NodeDTO> childs) {
        this.childs = childs;
    }
}
