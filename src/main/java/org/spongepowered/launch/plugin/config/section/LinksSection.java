package org.spongepowered.launch.plugin.config.section;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.net.URL;

@ConfigSerializable
public final class LinksSection {

    @Setting(value = "homepage")
    private URL homepage;

    @Setting(value = "source")
    private URL source;

    @Setting(value = "issues")
    private URL issues;

    public URL getHomepage() {
        return this.homepage;
    }

    public URL getSource() {
        return this.source;
    }

    public URL getIssues() {
        return this.issues;
    }
}
