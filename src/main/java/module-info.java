module org.spongepowered.plugin.spi {
    requires transitive org.spongepowered.plugin.metadata;
    requires transitive org.apache.logging.log4j;

    exports org.spongepowered.plugin;
    exports org.spongepowered.plugin.blackboard;
    exports org.spongepowered.plugin.builtin;
    exports org.spongepowered.plugin.builtin.jvm;
    exports org.spongepowered.plugin.builtin.jvm.locator;

    provides org.spongepowered.plugin.PluginResourceLocatorService with
            org.spongepowered.plugin.builtin.jvm.locator.ClasspathPluginResourceLocatorService,
            org.spongepowered.plugin.builtin.jvm.locator.DirectoryPluginResourceLocatorService,
            org.spongepowered.plugin.builtin.jvm.locator.EnvironmentPluginResourceLocatorService;
}