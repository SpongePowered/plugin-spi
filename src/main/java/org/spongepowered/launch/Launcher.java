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
package org.spongepowered.launch;

import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.launch.plugin.PluginLoader;
import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.PluginKeys;

import java.nio.file.Path;

public abstract class Launcher {

    private static final Logger logger = LogManager.getLogger("Sponge");
    private static final PluginEnvironment pluginEnvironment = new PluginEnvironment();
    private static PluginLoader pluginLoader;

    public static Logger getLogger() {
        return Launcher.logger;
    }

    public static PluginEnvironment getPluginEnvironment() {
        return Launcher.pluginEnvironment;
    }

    public static PluginLoader getPluginLoader() {
        return Launcher.pluginLoader;
    }

    protected static void loadPlugins(final Injector parentInjector, final Path gameDirectory) {
        Launcher.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.VERSION, () -> "0.1");
        Launcher.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.BASE_DIRECTORY, () -> gameDirectory);
        Launcher.pluginEnvironment.getBlackboard().getOrCreate(PluginKeys.PARENT_INJECTOR, () -> parentInjector);
        Launcher.pluginLoader = new PluginLoader(Launcher.pluginEnvironment);
        pluginLoader.discoverServices();
        pluginLoader.getServices().forEach((k, v) -> v.initialize(pluginLoader.getEnvironment()));
        pluginLoader.initialize();
        pluginLoader.discoverResources();
        pluginLoader.determineCandidates();
        pluginLoader.createContainers();
    }
}
