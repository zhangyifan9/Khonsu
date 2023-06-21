package cn.edu.nju.irtool.document;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by niejia on 15/2/10.
 */

public class LinksList extends ArrayList<SingleLink> implements Serializable {

    public LinksList() {
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (SingleLink link : this) {
            sb.append(link);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void updateLink(String source, String target, Double score) {
        for (SingleLink link : this) {
            if (link.getSourceArtifactId().equals(source)
                    && link.getTargetArtifactId().equals(target)) {
                link.setScore(score);
            }
        }
    }

    public double getScore(String source, String target) {
        for (SingleLink link : this) {
            if (link.getSourceArtifactId().equals(source)
                    && link.getTargetArtifactId().equals(target)) {
                return link.getScore();
            }
        }
        return -1;
    }
}
