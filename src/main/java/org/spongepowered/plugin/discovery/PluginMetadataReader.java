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
package org.spongepowered.plugin.discovery;

import org.spongepowered.plugin.Environment;
import org.spongepowered.plugin.PluginService;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.util.Collection;
import java.util.List;

/**
 * A service used to read {@link PluginMetadata metadata} from {@link PluginResource resources}.
 * <p>
 * Implementors are required to have a no-args constructor
 * <p>
 * Jars declaring this service are loaded in an isolated classloader.
 * They can only access the plugin-spi and cannot be accessed by plugins.
 */
public interface PluginMetadataReader extends PluginService {

    /**
     * Reads a list of {@link PluginMetadata plugin metadata} from the given {@link PluginResource resource}.
     *
     * @param environment The environment
     * @param resource The resource
     * @param locators The services that located the resource
     * @return The {@link List candidates}
     */
    Collection<? extends PluginMetadata> readPluginMetadata(Environment environment, PluginResource resource, List<PluginResourceLocator> locators) throws Exception;
}
