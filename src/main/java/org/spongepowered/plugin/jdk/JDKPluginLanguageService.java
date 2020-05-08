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
package org.spongepowered.plugin.jdk;

import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.PluginLanguageService;
import org.spongepowered.plugin.jdk.discover.DiscoverStrategies;
import org.spongepowered.plugin.metadata.PluginMetadataContainer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class JDKPluginLanguageService implements PluginLanguageService {

    private final List<PluginCandidate> pluginCandidates;
    private PluginEnvironment environment;

    protected JDKPluginLanguageService() {
        this.pluginCandidates = new ArrayList<>();
    }

    @Override
    public void initialize(final PluginEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public final Collection<PluginCandidate> discoverPlugins(final PluginEnvironment environment) {
        this.environment.getLogger().info("Discovering {} '{}' plugins...", DiscoverStrategies.CLASSPATH.getName(), this.getName());

        this.pluginCandidates.addAll(DiscoverStrategies.CLASSPATH.discoverPlugins(environment, this));

        return this.pluginCandidates;
    }

    @Override
    public final Collection<PluginContainer> createPlugins(final PluginEnvironment environment, final ClassLoader targetClassloader) {
        return null;
    }

    @Override
    public final Collection<PluginCandidate> getArtifacts() {
        return Collections.unmodifiableList(this.pluginCandidates);
    }

    public abstract String getPluginMetadataFileName();

    public abstract Optional<PluginMetadataContainer> createPluginMetadata(final PluginEnvironment environment, final String filename,
        final InputStream stream);

    protected abstract Object createPluginInstance(final PluginEnvironment environment, final PluginCandidate artifact);
}
