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
package org.spongepowered.plugin.jvm;

import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.PluginFile;
import org.spongepowered.plugin.PluginLanguageService;
import org.spongepowered.plugin.jvm.discover.DiscoverStrategies;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.PluginMetadataContainer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class JVMPluginLanguageService implements PluginLanguageService {

    private final Map<String, Collection<PluginFile>> pluginResources;
    private final Map<String, Collection<PluginCandidate>> pluginCandidates;

    protected JVMPluginLanguageService() {
        this.pluginResources = new HashMap<>();
        this.pluginCandidates = new HashMap<>();
    }

    @Override
    public void initialize(PluginEnvironment environment) {
    }

    @Override
    public List<PluginFile> discoverResources(final PluginEnvironment environment) {
        final List<PluginFile> pluginFiles = DiscoverStrategies.CLASSPATH.discoverResources(environment, this);
        environment.getLogger().info("Discovered '{}' [{}] plugin resource(s) for '{}'.", pluginFiles.size(), DiscoverStrategies.CLASSPATH.getName(), this.getName());
        this.pluginResources.put(DiscoverStrategies.CLASSPATH.getName(), pluginFiles);
        // TODO Directory-installed plugin files
        return pluginFiles;
    }

    @Override
    public List<PluginCandidate> determineCandidates(final PluginEnvironment environment) {
        List<PluginCandidate> pluginCandidates = new ArrayList<>();

        for (final Map.Entry<String, Collection<PluginFile>> resourcesEntry : this.pluginResources.entrySet()) {
            final Collection<PluginFile> resources = resourcesEntry.getValue();
            final List<PluginCandidate> perStrategyCandidates = new ArrayList<>();

            for (final PluginFile pluginFile : resources) {
                final Path pluginMetadataFile = pluginFile.getRootPath().resolve(this.getPluginMetadataFileName());
                try (final InputStream stream = Files.newInputStream(pluginMetadataFile)) {
                    final PluginMetadataContainer pluginMetadataContainer = this.createPluginMetadata(environment, pluginMetadataFile.getFileName().toString(), stream).orElse(null);
                    if (pluginMetadataContainer != null) {
                        for (final Map.Entry<String, PluginMetadata> metadataEntry : pluginMetadataContainer.getAllMetadata().entrySet()) {
                            final PluginMetadata metadata = metadataEntry.getValue();
                            final PluginCandidate candidate = PluginCandidate.of(metadata, pluginFile);
                            pluginCandidates.add(candidate);
                            perStrategyCandidates.add(candidate);
                        }
                    }
                } catch (final IOException e) {
                    environment.getLogger().error("An error occurred when opening the plugin metadata file for '{}'!", pluginFile.getRootPath().toString(), e);
                }
            }

            this.pluginCandidates.put(resourcesEntry.getKey(), perStrategyCandidates);
        }

        return this.sortCandidates(pluginCandidates);
    }

    @Override
    public Optional<PluginContainer> createPlugin(final PluginCandidate candidate, final PluginEnvironment environment, final ClassLoader targetClassloader) {
        final Object pluginInstance;
        try {
            pluginInstance = this.createPluginInstance(environment, candidate, targetClassloader);
        } catch (final ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            environment.getLogger().error("Encountered an error attempting to create an instance of Plugin '{}'!", candidate.getMetadata().getId(), e);
            return Optional.empty();
        }
        return Optional.of(JavaPluginContainer.of(candidate, pluginInstance));
    }

    public abstract String getPluginMetadataFileName();

    public abstract Optional<PluginMetadataContainer> createPluginMetadata(final PluginEnvironment environment, final String filename, final InputStream stream);

    protected abstract List<PluginCandidate> sortCandidates(final List<PluginCandidate> pluginCandidates);

    protected abstract Object createPluginInstance(final PluginEnvironment environment, final PluginCandidate candidate, final ClassLoader targetClassLoader)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException;
}
