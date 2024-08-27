module org.spongepowered.plugin.spi {
    exports org.spongepowered.plugin;
    exports org.spongepowered.plugin.blackboard;
    exports org.spongepowered.plugin.builtin;
    exports org.spongepowered.plugin.builtin.jvm;
    exports org.spongepowered.plugin.builtin.jvm.locator;

    requires transitive org.spongepowered.plugin.metadata;
    requires transitive org.apache.logging.log4j;
}