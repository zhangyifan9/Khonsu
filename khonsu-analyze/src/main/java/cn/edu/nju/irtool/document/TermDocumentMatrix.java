package cn.edu.nju.irtool.document;

import java.io.*;
import java.util.*;

/**
 * Created by niejia on 15/2/23.
 */
public class TermDocumentMatrix implements Serializable{

    private double[][] matrix;
    private List<String> termNameList;
    private List<String> docNameList;
    private Map<String, Integer> termNameOrderMap;
    private Map<String, Integer> docNameOrderMap;

    public TermDocumentMatrix() {
    }

    public double getValue(int docIndex, int termIndex) {
        return getMatrix()[docIndex][termIndex];
    }

    public void setValue(int docIndex, int termIndex, double value) {
        getMatrix()[docIndex][termIndex] = value;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public List<String> getTermIndex() {
        return termNameList;
    }

    public void setTermIndex(List<String> termIndex) {
        this.termNameList = termIndex;
    }

    public List<String> getDocIndex() {
        return docNameList;
    }

    public void setDocIndex(List<String> docIndex) {
        this.docNameList = docIndex;
    }

    public int NumTerms() {
        return termNameList.size();
    }

    public int NumDocs() {
        return docNameList.size();
    }

    
    public TermDocumentMatrix(ArtifactsCollection artifacts) {
    	// 保存所有不同的term
        termNameList = new ArrayList<>();
        
        docNameList = new ArrayList<>();
        
        // 保存所有不同的term以及插入时的次序
        termNameOrderMap = new HashMap<>();
        docNameOrderMap = new HashMap<>();

        // create temporary corpus to build matrix with
        Map<String, Map<String, Double>> allDocTermMap = new LinkedHashMap<>();
        for (Artifact a : artifacts.values()) {
            // update document maps
            docNameList.add(a.id);
            docNameOrderMap.put(a.id, docNameList.size() - 1);
            allDocTermMap.put(a.id, new LinkedHashMap<String, Double>());

            for (String term : a.text.split(" ")) {
                // update term maps
                if (term != null && !term.equals(" ")) {
                    if (!termNameOrderMap.containsKey(term)) {
                        termNameList.add(term);
                        termNameOrderMap.put(term, termNameList.size() - 1);
                    }
                }

                // update document counts
                if (allDocTermMap.get(a.id).containsKey(term)) {
                    double count = allDocTermMap.get(a.id).get(term);
                    allDocTermMap.get(a.id).put(term, count + 1); // 更新该文件中该term的个数
                } else {
                    allDocTermMap.get(a.id).put(term, Double.valueOf(1));
                }
            }
        }

        matrix = new double[docNameList.size()][];
        for (int i = 0; i < docNameList.size(); i++) {
            matrix[i] = new double[termNameList.size()];
            for (int j = 0; j < termNameList.size(); j++) {
                Double v = allDocTermMap.get(docNameList.get(i)).get(termNameList.get(j));  // v是该term的个数
                if (v != null) {
                    matrix[i][j] = v;
                } else {
                    matrix[i][j] = 0.0;
                }
            }
        }

    }

    public double[] getDocument(int index) {
        return matrix[index];
    }

    public double[] getDocument(String artifactID) {
        return getDocument(docNameOrderMap.get(artifactID));
    }

    public double getValue(String artifactID, String term) {
        return getValue((int) docNameOrderMap.get(artifactID), (int) termNameOrderMap.get(term));
    }

    public int getTermIndex(String term) {
        return termNameOrderMap.get(term);
    }

    public String getTermName(int index) {
        return termNameList.get(index);
    }

    public int getDocumentIndex(String artifactID) {
        return docNameOrderMap.get(artifactID);
    }

    public String getDocumentName(int index) {
        return docNameList.get(index);
    }

    public void setDocument(int index, double[] doc) {
        if (doc.length != matrix[index].length) {
            throw new IllegalArgumentException("The array sizes do not match.");
        }
        matrix[index] = doc;
    }

    public void setDocument(String artifactID, double[] doc) {
        setDocument(docNameOrderMap.get(artifactID), doc);
    }

    public void setValue(String artifactID, String term, double value) {
        setValue(docNameOrderMap.get(artifactID), termNameOrderMap.get(term), value);
    }

    // in TermDocumentMatrix is setMatrix
    public void setNewMatrix(double[][] matrix) {
        if (matrix.length != this.matrix.length) {
            throw new IllegalArgumentException("The matrix has the wrong number of rows.");
        }
        for (int i = 0; i < this.matrix.length; i++) {
            if (matrix[i].length != this.matrix[i].length) {
                throw new IllegalArgumentException("The matrix has the wrong number of columns in row " + i + ".");
            }
        }
        this.matrix = matrix;
    }
    
    

    // 把source和target中所有独特的terms进行合并，
    public static List<TermDocumentMatrix> Equalize(TermDocumentMatrix matrix1, TermDocumentMatrix matrix2) {
        // initialize matrices
        List<TermDocumentMatrix> matrices = new ArrayList<>();

        //matrix 1
        matrices.add(new TermDocumentMatrix());
        matrices.get(0).matrix = new double[matrix1.NumDocs()][];
        matrices.get(0).docNameList = new ArrayList<String>(matrix1.docNameList);
        matrices.get(0).docNameOrderMap = new HashMap<String, Integer>(matrix1.docNameOrderMap);

        //matrix 2
        matrices.add(new TermDocumentMatrix());
        matrices.get(1).matrix = new double[matrix2.NumDocs()][];
        matrices.get(1).docNameList = new ArrayList<String>(matrix2.docNameList);
        matrices.get(1).docNameOrderMap = new HashMap<String, Integer>(matrix2.docNameOrderMap);

        List<String> termIndex = new ArrayList<String>();
        Map<String, Integer> termIndexLookup = new HashMap<String, Integer>();
        Map<String, Integer> leftovers = new HashMap<String, Integer>(matrix2.termNameOrderMap);

        // 把source中所有的term（不重复）加入
        for (String term : matrix1.termNameList) {
            termIndex.add(term);
            termIndexLookup.put(term, termIndex.size() - 1);
            // remove duplicate terms
            if (matrix2.termNameOrderMap.containsKey(term)) {
                leftovers.remove(term);
            }
        }

        // 把target中特有的terms加入
        for (String term : leftovers.keySet()) {
            termIndex.add(term);
            termIndexLookup.put(term, termIndex.size() - 1);
        }

        // create new term distributions for each document
        // matrix 1
        matrices.get(0).termNameList = new ArrayList<String>(termIndex); // 长度是二者合并后的长度
        matrices.get(0).termNameOrderMap = new HashMap<String, Integer>(termIndexLookup);

        for (int i = 0; i < matrices.get(0).NumDocs(); i++) {
            matrices.get(0).matrix[i] = new double[termIndex.size()];
            // fill in original values
            for (int j = 0; j < matrix1.NumTerms(); j++) {  // source所有terms（无重复）的长度
                matrices.get(0).setValue(i, j, matrix1.getValue(i, j));
            }
            // fill in missing terms target中特有的terms
            for (int j = matrix1.NumTerms(); j < termIndex.size(); j++) {
                matrices.get(0).setValue(i, j, 0.0);
            }
        }

        // matrix 2
        matrices.get(1).termNameList = new ArrayList<String>(termIndex);
        matrices.get(1).termNameOrderMap = new HashMap<String, Integer>(termIndexLookup);
        for (int i = 0; i < matrices.get(1).NumDocs(); i++) {
            matrices.get(1).matrix[i] = new double[termIndex.size()];
            // fill in values
            for (int j = 0; j < termIndex.size(); j++) {
                if (matrix2.containsTerm(termIndex.get(j))) {
                    matrices.get(1).setValue(i, j, matrix2.getValue(matrix2.getDocumentName(i), termIndex.get(j)));
                } else {
                    matrices.get(1).setValue(i, j, 0.0);
                }
            }
        }

        // return
        return matrices;
    }

    public boolean containsTerm(String term) {
        return termNameOrderMap.containsKey(term);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < matrix.length; i++) {
            sb.append(docNameList.get(i) + ": ");
            for (int j = 0; j < matrix[0].length; j++) {
                sb.append(termNameList.get(j));
                sb.append("._");
//                sb.append("component._");
                sb.append(matrix[i][j]);
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    

    /**
     * @author gaohui
     * @date 2018-9-19 20:44
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * deep clone
     */
    public Object deepClone() throws IOException, ClassNotFoundException {//将对象写到流里
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(this);//从流里读出来
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (oi.readObject());
    }
}
