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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.PluginLanguageService;
import org.spongepowered.plugin.jvm.locator.JVMPluginResource;
import org.spongepowered.plugin.jvm.locator.JVMPluginResourceLocatorService;
import org.spongepowered.plugin.metadata.Container;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class JVMPluginLanguageService implements PluginLanguageService<JVMPluginResource> {

    private final Logger logger = LogManager.getLogger(this.name());

    @Override
    public void initialize(final PluginEnvironment environment) {
    }

    @Override
    public final List<PluginCandidate<JVMPluginResource>> createPluginCandidates(final PluginEnvironment environment,
            final JVMPluginResource resource) throws Exception {
        Objects.requireNonNull(environment, "environment");
        Objects.requireNonNull(resource, "resource");

        final List<PluginCandidate<JVMPluginResource>> candidates = new LinkedList<>();

        try (final InputStream stream = this.getFileAsStream(resource.path(), this.metadataFile())) {
            final Container container = this.loadMetadataContainer(environment, this.metadataFileName(), stream);
            if (!container.loader().name().equals(this.name())) {
                throw new IOException(String.format("Attempt made to load Container in path '%s' with loader '%s' yet it requires '%s'!",
                        resource.path(), this.name(), container.loader().name()));
            }

            if (!this.isValidContainer(environment, container)) {
                this.logger.debug("Container in path '{}' with loader '{}' is not valid, skipping...", resource.path(), container.loader().name());
            } else {
                boolean containerHasMetadata = false;
                for (final PluginMetadata metadata : container.metadata()) {
                    if (!this.isValidMetadata(environment, metadata)) {
                        this.logger.debug("PluginMetadata '{}' within Container in path '{}' with loader '{}' is not valid, skipping...",
                                metadata.id(), resource.path(), container.loader().name());
                        continue;
                    }
                    containerHasMetadata = true;
                    candidates.add(new PluginCandidate<>(metadata, resource));
                }

                if (!containerHasMetadata) {
                    this.logger.debug("Container in path '{}' with loader '{}' has no plugin metadata, skipping...", resource.path(),
                            container.loader().name());
                }
            }
        }

        return candidates;
    }

    public String metadataFileName() {
        return JVMPluginResourceLocatorService.DEFAULT_METADATA_FILENAME;
    }

    public String metadataFile() {
        return JVMPluginResourceLocatorService.DEFAULT_METADATA_FILE;
    }

    public abstract Container loadMetadataContainer(final PluginEnvironment environment, final String filename, final InputStream stream)
            throws Exception;

    private boolean isValidContainer(final PluginEnvironment environment, final Container container) {
        return true;
    }

    protected boolean isValidMetadata(final PluginEnvironment environment, final PluginMetadata metadata) {
        return true;
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
            final JarEntry entry = jf.getJarEntry(relativePath);
            return jf.getInputStream(entry);
        } else {
            return Files.newInputStream(rootDirectory.resolve(relativePath));
        }
    }
}
