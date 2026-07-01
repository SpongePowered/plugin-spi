import org.spongepowered.plugin.builtin.StandardPluginMetadataReader;
import org.spongepowered.plugin.discovery.PluginMetadataReader;
import org.spongepowered.plugin.discovery.PluginResourceLocator;
import org.spongepowered.plugin.builtin.jvm.locator.ClasspathPluginResourceLocator;
import org.spongepowered.plugin.builtin.jvm.locator.DirectoryPluginResourceLocator;
import org.spongepowered.plugin.builtin.jvm.locator.EnvironmentPluginResourceLocator;

module org.spongepowered.plugin.spi {
    requires transitive org.spongepowered.plugin.metadata;
    requires transitive org.apache.logging.log4j;

    exports org.spongepowered.plugin;
    exports org.spongepowered.plugin.blackboard;
    exports org.spongepowered.plugin.builtin;
    exports org.spongepowered.plugin.builtin.jvm;
    exports org.spongepowered.plugin.builtin.jvm.locator;
    exports org.spongepowered.plugin.discovery;

    provides PluginResourceLocator with
            ClasspathPluginResourceLocator,
            DirectoryPluginResourceLocator,
            EnvironmentPluginResourceLocator;

    provides PluginMetadataReader with StandardPluginMetadataReader;
}
