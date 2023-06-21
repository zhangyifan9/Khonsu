import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;

public class TestPachong {
    public void test() {
        int count=0;
        FileWriter fw =null;
        int a =1;
        try {
            String allUrl ="https://lucene.apache.org/core/4_7_0/changes/Changes.html#older";
            Document docAll = Jsoup.connect(allUrl).get();
            Element list360 = docAll.body().getElementById("older.list").getElementById("v4.0.0.list");
            for(Element element:list360.children()){//分类
                 Elements lis=element.getElementsByTag("ol");
                if(lis.size()>0){
                    for(Element li:lis.get(0).children()){
                        // Elements urls = li.select("a[href]");
                        String issue= li.text();
                        int  leftBracketLocation = 0;
                        char[] chr = issue.toCharArray();
                        for (int i = chr.length-1 ; i >= 0; i--) {
                            if(chr[i] == '(') {
                                leftBracketLocation = i;
                                break;
                            }
                        }
                        issue = issue.substring(0 ,leftBracketLocation);
                        System.out.println(issue);
                        // String description="";
                        // System.out.println(li.text());
                        // for(Element url:urls){
                        //     try {
                        //         Document inDoc = Jsoup.connect(url.attr("abs:href")).get();
                        //         Elements des=inDoc.select("#description-val");
                        //         if(des.size()>0){
                        //             description=des.get(0).text();
                        //             System.out.println(description );
                        //         }
                        //     }catch (HttpStatusException exception){
                        //         exception.printStackTrace();
                        //     }
                        //
                        // }
                        System.out.println();
                        FileWriter fileWriter = new FileWriter("D:\\nju\\irtool\\IRTool\\data\\lucene-release\\version4.0.0\\r"+count+".txt");
                        fileWriter.write(issue);
                        fileWriter.close();
                        count++;

                    }
                }

            }
//            Elements hrefAll = urlall.select("a[href]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestPachong pt = new TestPachong();
        pt.test();


    }
}
