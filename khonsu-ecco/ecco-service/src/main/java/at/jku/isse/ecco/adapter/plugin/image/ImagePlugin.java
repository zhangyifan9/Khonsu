package at.jku.isse.ecco.adapter.plugin.image;

import at.jku.isse.ecco.adapter.ArtifactPlugin;
import com.google.inject.Module;

public class ImagePlugin implements ArtifactPlugin {

	private ImageModule module = new ImageModule();

	@Override
	public String getPluginId() {
		return ImagePlugin.class.getName();
	}

	@Override
	public Module getModule() {
		return this.module;
	}

	@Override
	public String getName() {
		return "ImageArtifactPlugin";
	}

	@Override
	public String getDescription() {
		return "Image Artifact Plugin";
	}

}
