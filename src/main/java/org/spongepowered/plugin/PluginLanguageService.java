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

/**
 * A service that processes {@link PluginResource resources} and triggers loading of
 * {@link PluginContainer containers} via a specified {@link PluginLoader loader}.
 * <p>
 * No class loading should occur at this time
 * <p>
 * Implementors are required to have a no-args constructor
 * @param <P> The resource type
 */
public interface PluginLanguageService<P extends PluginResource> {

    /**
     * @return The {@link String name}
     */
    String name();

    /**
     * @return The fully qualified path to the {@link PluginLoader loader}
     */
    String pluginLoader();

    /**
     * Callback so that implementors can perform any necessary initialization of the service.
     *
     * @param environment The environment the service is running under
     */
    void initialize(PluginEnvironment environment);

    /**
     * Asks the service if the provided {@link PluginResource resource} can become {@link PluginCandidate candidates}.
     * <p>
     * It is left up to implementors on when to throw exceptions (typically from parsing the resource) or to simply return
     * {@link List lists} that are empty. Therefore it should be known that an empty list returned is not an error and should
     * be discarded by callers of this method.
     *
     * @param environment The environment
     * @param resource The resource
     * @return The {@link List candidates}
     */
    List<PluginCandidate<P>> createPluginCandidates(PluginEnvironment environment, P resource) throws Exception;
}
