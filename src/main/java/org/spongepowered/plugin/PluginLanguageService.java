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
package org.spongepowered.plugin;

import java.util.Collection;

public interface PluginLanguageService {

    /**
     * Gets the name of this service.
     *
     * <p>This should be given to plugin makers using this service to indicate that intention.</p>
     *
     * @return The name
     */
    String getName();

    /**
     * Initializes this service.
     *
     * @param environment The environment the service is running under
     */
    void initialize(final PluginEnvironment environment);

    /**
     * Discovers {@link PluginArtifact artifacts} as potential plugin candidates.
     *
     * <p>No plugins should be classloaded whatsoever at this point.</p>
     *
     * @param environment The environment
     * @return The discovered plugins
     */
    Collection<PluginArtifact> discoverPlugins(final PluginEnvironment environment);

    /**
     * Creates {@link PluginContainer plugins} from their {@link PluginArtifact}.
     *
     * @param environment The environment
     * @param targetClassloader The classloader to load the plugin into
     * @return The plugins
     */
    Collection<PluginContainer> createPlugins(final PluginEnvironment environment, final ClassLoader targetClassloader);
}
