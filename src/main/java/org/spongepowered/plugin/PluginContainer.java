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

import org.apache.logging.log4j.Logger;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

/**
 * An object that joins a plugin's {@link PluginMetadata metadata} with it's instance.
 *
 * <p>Typically becomes the fundamental operator of the implementation's registration system
 * for anything that is deemed "a plugin".</p>
 */
public interface PluginContainer {

    /**
     * Gets the {@link PluginMetadata metadata} used to describe this plugin.
     *
     * @return The metadata
     */
    PluginMetadata metadata();

    /**
     * Gets a {@link Logger} with the id set to this plugin's id.
     *
     * <p>@see {@link PluginMetadata#id()}</p>
     *
     * @return The logger
     */
    Logger logger();

    /**
     * Gets the instance created when loading this plugin.
     *
     * @return The instance
     */
    Object instance();

    /**
     * Resolves the location of a bundled resource, given a relative {@link URI}.
     *
     * @param relative The relative URI
     * @return The resolved resource location, if available
     */
    Optional<URI> locateResource(URI relative);

    /**
     * Opens an {@link InputStream} of the location of a bundled resource, given a relative {@link URI}.
     *
     * @param relative The relative URI
     * @return The opened resource, if available
     */
    default Optional<InputStream> openResource(final URI relative) {
        return this.locateResource(relative).flatMap(url -> {
            try {
                return Optional.of(url.toURL().openStream());
            } catch (final IOException ignored) {
                return Optional.empty();
            }
        });
    }
}
