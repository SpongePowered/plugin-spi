/*
 * This file is part of plugin-spi, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.launch.plugin;

import org.spongepowered.launch.LauncherConstants;
import org.spongepowered.launch.plugin.config.PluginMetadataConfiguration;
import org.spongepowered.launch.plugin.config.section.LinksSection;
import org.spongepowered.launch.plugin.config.section.PluginSection;
import org.spongepowered.plugin.PluginArtifact;
import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.PluginMetadataContainer;
import org.spongepowered.plugin.jdk.JDKPluginLanguageService;
import org.spongepowered.plugin.PluginMetadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JavaPluginLanguageService extends JDKPluginLanguageService {

    private final static String NAME = "java_sponge";

    @Override
    public String getName() {
        return JavaPluginLanguageService.NAME;
    }

    @Override
    public String getPluginMetadataFileName() {
        return LauncherConstants.Plugin.Metadata.FILENAME;
    }

    @Override
    public Optional<PluginMetadataContainer> createPluginMetadata(final PluginEnvironment environment, final String filename, final InputStream stream) {
        final PluginMetadataConfiguration configuration;
        try {
            configuration = PluginMetadataConfiguration.loadFrom(filename, stream);
        } catch (final Exception e) {
            environment.getLogger().error("Encountered an issue reading plugin metadata!", e);
            return Optional.empty();
        }

        final List<PluginMetadata> pluginMetadata = new ArrayList<>();
        for (final PluginSection pluginSection : configuration.getPluginSections()) {
            final PluginMetadata.Builder builder = PluginMetadata.builder();
            builder
                .setId(pluginSection.getId())
                .setName(pluginSection.getName())
                .setVersion(pluginSection.getVersion())
                .setDescription(pluginSection.getDescription())
                .setAuthor(pluginSection.getAuthor());

            final LinksSection linksSection = pluginSection.getLinksSection();
            if (linksSection != null) {
                builder.setHomepageURL(linksSection.getHomepage());
                builder.setSourceURL(linksSection.getSource());
                builder.setIssuesURL(linksSection.getIssues());
            }

            pluginMetadata.add(builder.build());
        }

        return Optional.of(PluginMetadataContainer.of(pluginMetadata));
    }

    @Override
    protected Object createPluginInstance(final PluginEnvironment environment, final PluginArtifact artifact) {
        return null;
    }
}
