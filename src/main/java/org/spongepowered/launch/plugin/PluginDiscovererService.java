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

import com.google.common.collect.ImmutableList;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;
import org.spongepowered.launch.util.MixinUtils;
import org.spongepowered.plugin.PluginArtifact;
import org.spongepowered.launch.util.ImmutableMapEntry;
import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.PluginKeys;
import org.spongepowered.plugin.PluginLanguageService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.Manifest;

import javax.annotation.Nonnull;

public final class PluginDiscovererService implements ITransformationService {

    private static final String NAME = "plugin_discoverer";

    private final PluginEnvironment pluginEnvironment;
    private final Map<String, PluginLanguageService> pluginLoaders;

    public PluginDiscovererService() {
        this.pluginEnvironment = new PluginEnvironment();
        this.pluginLoaders = new HashMap<>();
    }

    @Nonnull
    @Override
    public String name() {
        return PluginDiscovererService.NAME;
    }

    @Override
    public void initialize(final IEnvironment environment) {
        final Path gameDirectory = environment.getProperty(IEnvironment.Keys.GAMEDIR.get()).orElse(Paths.get("."));
        final Path pluginsDirectory = gameDirectory.resolve("plugins"); // TODO Read Sponge config/command line
        this.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.BASE_DIRECTORY, () -> gameDirectory);
        this.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.VERSION, () -> "1.14.4-8.0.0-0"); // TODO Get actual version...
        this.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.PLUGINS_DIRECTORY, () -> pluginsDirectory);

        for (final Map.Entry<String, PluginLanguageService> pluginLoader : this.pluginLoaders.entrySet()) {
            pluginLoader.getValue().initialize(this.pluginEnvironment);
        }
    }

    @Override
    public void beginScanning(final IEnvironment environment) {
        //NOOP
    }

    @Override
    public List<Map.Entry<String, Path>> runScan(final IEnvironment environment) {
        final List<Map.Entry<String, Path>> launchResources = new ArrayList<>();
        for (final Map.Entry<String, PluginLanguageService> pluginLoader : this.pluginLoaders.entrySet()) {
            final Collection<PluginArtifact> pluginArtifacts = pluginLoader.getValue().discoverPlugins(this.pluginEnvironment);
            for (final PluginArtifact pluginArtifact : pluginArtifacts) {
                final Manifest manifest = pluginArtifact.getManifest().orElse(null);
                if (manifest == null) {
                    continue;
                }

                if (!MixinUtils.getMixinConfigs(manifest).isPresent()) {
                    continue;
                }

                launchResources.add(ImmutableMapEntry.of(pluginArtifact.getMetadata().getId(), pluginArtifact.getRootPath()));
            }
        }

        return launchResources;
    }

    @Override
    public void onLoad(final IEnvironment env, final Set<String> otherServices) throws IncompatibleEnvironmentException {
        final ServiceLoader<PluginLanguageService> serviceLoader = ServiceLoader.load(PluginLanguageService.class, ClassLoader.getSystemClassLoader());
        for (final Iterator<PluginLanguageService> iter = serviceLoader.iterator(); iter.hasNext();) {
            final PluginLanguageService next;

            try {
                next = iter.next();
                this.pluginEnvironment.getLogger().info("Plugin language loader '{}' found.", next.getName());
            } catch (final ServiceConfigurationError e) {
                this.pluginEnvironment.getLogger().error("Error encountered initializing plugin loader service!", e);
                continue;
            }

            this.pluginLoaders.put(next.getName(), next);
        }
    }

    @Nonnull
    @Override
    public List<ITransformer> transformers() {
        return ImmutableList.of();
    }
}
