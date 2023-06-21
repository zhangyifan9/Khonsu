package document;

import java.io.Serializable;

/**
 * Created by niejia on 15/2/10.
 */
public class Artifact implements Serializable {
    public String id;
    public String text;

    public Artifact(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
