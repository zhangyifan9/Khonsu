package at.jku.isse.ecco.api.dto;

import java.util.ArrayList;
import java.util.HashMap;

//TODO:改动
public class SingleFeatureDTO {
    private int id;
    private String name;
    private int nums;
    private HashMap<String, ArrayList<String>> javaFiles=new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public HashMap<String, ArrayList<String>> getJavaFiles() {
        return javaFiles;
    }

    public void setJavaFiles(HashMap<String, ArrayList<String>> javaFiles) {
        this.javaFiles = javaFiles;
    }
}
