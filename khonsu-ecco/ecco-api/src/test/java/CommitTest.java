import at.jku.isse.ecco.api.resource.ArtifactsResource;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @ClassName CommitTest
 * @Description TODO
 * @Author panpan
 */
public class CommitTest {
    @Test
    public void commit() throws IOException {

        ArtifactsResource ar = new ArtifactsResource("D:\\Workplace\\nju\\tmp\\repo");
        ar.commitAllVersions("test", "D:\\Workplace\\nju\\tmp\\java");
        ar.compose("test", "D:\\Workplace\\nju\\tmp");
        ar.close();

    }
}
