package at.jku.isse.ecco.adapter.plugin.java;

import at.jku.isse.ecco.adapter.ArtifactPlugin;
import com.google.inject.Module;

public class JavaPlugin implements ArtifactPlugin {

	private JavaModule module = new JavaModule();

	@Override
	public String getPluginId() {
		return JavaPlugin.class.getName();
	}

	@Override
	public Module getModule() {
		return this.module;
	}

	@Override
	public String getName() {
		return "JavaArtifactPlugin";
	}

	@Override
	public String getDescription() {
		return "Java Artifact Plugin";
	}

}
