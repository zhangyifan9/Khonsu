import at.jku.isse.ecco.adapter.ArtifactPlugin;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

/**
 * @ClassName PluginTest
 * @Description TODO
 * @Author panpan
 */
public class PluginTest {
    @Test
    public void pluginPrint() {
        ServiceLoader<ArtifactPlugin> loader = ServiceLoader.load(ArtifactPlugin.class);
        for (ArtifactPlugin artifactPlugin : loader) {
            System.out.println(artifactPlugin.getPluginId());
        }
    }
}
