package editor.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public class PluginServiceLoader {

	private static PluginServiceLoader instance;

	private final ServiceLoader<Plugin> pluginServiceLoader;

	private PluginServiceLoader() {
		pluginServiceLoader = ServiceLoader.load(Plugin.class);
	}

	public static PluginServiceLoader getInstance() {
		if (instance == null) {
			instance = new PluginServiceLoader();
		}
		return instance;
	}

	public List<Plugin> loadPlugins() {
		List<Plugin> plugins = new ArrayList<>();
		pluginServiceLoader.forEach(plugins::add);
		return Collections.unmodifiableList(plugins);
	}
}