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

import org.spongepowered.plugin.InvalidPluginException;
import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.PluginLanguageService;
import org.spongepowered.plugin.jvm.discover.DiscoverStrategies;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.PluginMetadataContainer;
import org.spongepowered.plugin.metadata.util.PluginMetadataHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public abstract class JVMPluginLanguageService<P extends JVMPluginContainer> implements PluginLanguageService<P> {

    private static final String DEFAULT_METADATA_FILE_NAME = "plugins.json";

    private final Map<String, Collection<Path>> pluginResources;
    private final Map<String, Collection<PluginCandidate>> pluginCandidates;

    protected JVMPluginLanguageService() {
        this.pluginResources = new HashMap<>();
        this.pluginCandidates = new HashMap<>();
    }

    @Override
    public void initialize(final PluginEnvironment environment) {
    }

    @Override
    public List<Path> discoverPluginResources(final PluginEnvironment environment) {
        final List<Path> pluginFiles = new ArrayList<>();

        for (final DiscoverStrategies strategy : DiscoverStrategies.values()) {
            final List<Path> files = strategy.discoverResources(environment, this);
            environment.getLogger().info("Discovered '{}' [{}] plugin resource(s) for '{}'.", files.size(), strategy.getName(), this.getName());
            this.pluginResources.put(strategy.getName(), files);
            pluginFiles.addAll(files);
        }

        return pluginFiles;
    }

    @Override
    public List<PluginCandidate> createPluginCandidates(final PluginEnvironment environment) {
        List<PluginCandidate> pluginCandidates = new ArrayList<>();

        for (final Map.Entry<String, Collection<Path>> resourcesEntry : this.pluginResources.entrySet()) {
            final Collection<Path> resources = resourcesEntry.getValue();
            final List<PluginCandidate> perStrategyCandidates = new ArrayList<>();

            for (final Path pluginFile : resources) {
                final String pluginMetadataFileName = this.getPluginMetadataFileName();
                try (final InputStream stream = this.getFileAsStream(pluginFile, JVMConstants.META_INF_LOCATION + "/" + pluginMetadataFileName)) {
                    final PluginMetadataContainer pluginMetadataContainer = this.createPluginMetadata(environment, pluginMetadataFileName, stream).orElse(null);
                    if (pluginMetadataContainer != null) {
                        for (final Map.Entry<String, PluginMetadata> metadataEntry : pluginMetadataContainer.getAllMetadata().entrySet()) {
                            final PluginMetadata metadata = metadataEntry.getValue();
                            final PluginCandidate candidate = new PluginCandidate(metadata, pluginFile);
                            pluginCandidates.add(candidate);
                            perStrategyCandidates.add(candidate);
                        }
                    }
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }

            this.pluginCandidates.put(resourcesEntry.getKey(), perStrategyCandidates);
        }

        return this.sortCandidates(pluginCandidates);
    }

    @Override
    public void loadPlugin(final PluginEnvironment environment, final P container, final ClassLoader targetClassLoader) throws InvalidPluginException {
        Objects.requireNonNull(environment);
        Objects.requireNonNull(container);
        Objects.requireNonNull(targetClassLoader);

        container.setInstance(this.createPluginInstance(environment, container, targetClassLoader));
    }

    private InputStream getFileAsStream(final Path rootDirectory, final String relativePath) throws URISyntaxException, IOException {
        final URI uri = rootDirectory.toUri();
        Path jarFile = null;
        if (uri.getRawSchemeSpecificPart().contains("!")) {
            jarFile = Paths.get(new URI(uri.getRawSchemeSpecificPart().split("!")[0]));
        } else if (rootDirectory.toString().endsWith(".jar")) {
            jarFile = rootDirectory;
        }

        if (jarFile != null) {
            final JarFile jf = new JarFile(jarFile.toFile());
            final JarEntry pluginMetadataJarEntry = jf.getJarEntry(relativePath);
            return jf.getInputStream(pluginMetadataJarEntry);
        } else {
            return Files.newInputStream(rootDirectory.resolve(relativePath));
        }
    }

    public String getPluginMetadataFileName() {
        return JVMPluginLanguageService.DEFAULT_METADATA_FILE_NAME;
    }

    public Optional<PluginMetadataContainer> createPluginMetadata(final PluginEnvironment environment, final String filename, final InputStream stream) {
        final PluginMetadataHelper metadataHelper = PluginMetadataHelper.builder().build();
        try {
            final List<PluginMetadata> pluginMetadata = new ArrayList<>(metadataHelper.read(stream));
            pluginMetadata.removeIf(nextMetadata -> !this.isValidMetadata(environment, nextMetadata));
            if (pluginMetadata.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(new PluginMetadataContainer(pluginMetadata));
        } catch (IOException e) {
            environment.getLogger().error("An error occurred reading plugin metadata file '{}'.", filename, e);
            return Optional.empty();
        }
    }

    public boolean isValidManifest(final PluginEnvironment environment, final Manifest manifest) {
        return true;
    }

    public boolean isValidMetadata(final PluginEnvironment environment, final PluginMetadata pluginMetadata) {
        return true;
    }

    protected List<PluginCandidate> sortCandidates(final List<PluginCandidate> pluginCandidates) {
        return pluginCandidates;
    }

    protected abstract Object createPluginInstance(final PluginEnvironment environment, final P container, final ClassLoader targetClassLoader) throws InvalidPluginException;
}
