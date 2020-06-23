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
import java.util.List;
import java.util.Optional;
import java.util.jar.Manifest;

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
     * Discovers {@link PluginFile launch resources} for use with ecosystems that are pluggable
     * with other constructs. As an example and a use case, Sponge passes off the files's {@link Manifest}
     * to ModLauncher for Mixin interoperability.
     *
     * <p>Under no circumstance should plugins be classloaded in the invocation of this method.</p>
     *
     * <p>It is also assumed that the library user will track its plugin resources, for use when discovering candidates.</p>
     *
     * @param environment The environment
     * @return The discovered files
     */
    List<PluginFile> discoverResources(final PluginEnvironment environment);

    /**
     * Determines the {@link PluginCandidate candidates} that will be, eventually, loaded as plugins.
     *
     * <p>Under no circumstance should plugins be classloaded in the invocation of this method.</p>
     *
     * @param environment The environment
     * @return The discovered candidates
     */
    List<PluginCandidate> determineCandidates(final PluginEnvironment environment);

    /**
     * Creates a {@link PluginContainer} which encapsulates the plugin and is the final representation of a plugin by this library.
     *
     * @param candidate The candidate
     * @param environment The environment
     * @param targetClassloader The target classloader
     * @throws InvalidPluginException If the plugin is considered invalid by the implementation
     * @return The container
     */
    Optional<PluginContainer> createPlugin(final PluginCandidate candidate, final PluginEnvironment environment, final ClassLoader targetClassloader) throws InvalidPluginException;
}
