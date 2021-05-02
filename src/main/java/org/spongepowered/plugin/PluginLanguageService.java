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

import java.util.List;
import java.util.Optional;

public interface PluginLanguageService<P extends PluginResource> {

    /**
     * Gets the name of this service.
     *
     * <p>This should be given to plugin makers using this service to indicate that intention.</p>
     *
     * @return The name
     */
    String name();

    /**
     * Gets the fully qualified path to the {@link PluginLoader loader} that should be used to load
     * {@link PluginContainer plugins}.
     *
     * @return The classpath to the loader
     */
    String pluginLoader();

    /**
     * Initializes this service.
     *
     * @param environment The environment the service is running under
     */
    void initialize(final PluginEnvironment environment);

    /**
     * Asks the service if the provided {@link PluginResource resource} can become {@link PluginCandidate candidates}.
     *
     * @param environment The environment
     * @param resource The resource
     * @return The candidate or {@link Optional#empty()} if the resource can't be a candidate
     */
    List<PluginCandidate<P>> createPluginCandidates(final PluginEnvironment environment, P resource);
}
