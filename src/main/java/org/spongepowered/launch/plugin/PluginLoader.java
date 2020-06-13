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

import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.PluginFile;
import org.spongepowered.plugin.PluginKeys;
import org.spongepowered.plugin.PluginLanguageService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public final class PluginLoader {

    private final Map<String, PluginLanguageService> languageServices;
    private final Map<String, Collection<PluginFile>> pluginFiles;
    private final Map<PluginLanguageService, Collection<PluginCandidate>> pluginCandidates;
    private final PluginEnvironment pluginEnvironment;

    public PluginLoader(final PluginEnvironment pluginEnvironment) {
        this.pluginEnvironment = pluginEnvironment;
        this.languageServices = new HashMap<>();
        this.pluginFiles = new HashMap<>();
        this.pluginCandidates = new HashMap<>();
    }

    public PluginEnvironment getEnvironment() {
        return this.pluginEnvironment;
    }

    public Map<String, PluginLanguageService> getServices() {
        return Collections.unmodifiableMap(this.languageServices);
    }

    public Map<String, Collection<PluginFile>> getResources() {
        return Collections.unmodifiableMap(this.pluginFiles);
    }

    public void initialize() {
        final Path gameDirectory = this.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.BASE_DIRECTORY, () -> Paths.get("."));
        final Path pluginsDirectory = gameDirectory.resolve("plugins"); // TODO Read Sponge config/command line
        this.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.BASE_DIRECTORY, () -> gameDirectory);
        this.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.PLUGINS_DIRECTORY, () -> pluginsDirectory);

        for (final Map.Entry<String, PluginLanguageService> entry : this.languageServices.entrySet()) {
            entry.getValue().initialize(this.pluginEnvironment);
        }
    }

    public void discoverServices() {
        final ServiceLoader<PluginLanguageService> serviceLoader = ServiceLoader.load(PluginLanguageService.class, PluginLoader.class.getClassLoader());

        for (final Iterator<PluginLanguageService> iter = serviceLoader.iterator(); iter.hasNext(); ) {
            final PluginLanguageService next;

            try {
                next = iter.next();
            } catch (final ServiceConfigurationError e) {
                this.pluginEnvironment.getLogger().error("Error encountered initializing plugin loader!", e);
                continue;
            }

            this.languageServices.put(next.getName(), next);
        }
    }

    public void discoverResources() {
        for (final Map.Entry<String, PluginLanguageService> languageEntry : this.languageServices.entrySet()) {
            final PluginLanguageService languageService = languageEntry.getValue();
            final Collection<PluginFile> pluginFiles = languageService.discoverResources(this.pluginEnvironment);
            if (pluginFiles.size() > 0) {
                this.pluginFiles.put(languageEntry.getKey(), Collections.unmodifiableCollection(pluginFiles));
            }
        }
    }

    public void determineCandidates() {
        for (final Map.Entry<String, PluginLanguageService> languageEntry : this.languageServices.entrySet()) {
            final PluginLanguageService languageService = languageEntry.getValue();
            final Collection<PluginCandidate> pluginCandidates = languageService.determineCandidates(this.pluginEnvironment);
            if (pluginCandidates.size() > 0) {
                this.pluginCandidates.put(languageService, Collections.unmodifiableCollection(pluginCandidates));
            }
        }
    }

    public void createContainers() {
        for (final Map.Entry<PluginLanguageService, Collection<PluginCandidate>> languageCandidates : this.pluginCandidates.entrySet()) {
            final PluginLanguageService languageService = languageCandidates.getKey();
            final Collection<PluginCandidate> candidates = languageCandidates.getValue();
            for (final PluginCandidate candidate : candidates) {
                // TODO Woo, onwards to the game!
                final PluginContainer plugin = languageService.createPlugin(candidate, this.pluginEnvironment, PluginLoader.class.getClassLoader());
            }
        }
    }
}
