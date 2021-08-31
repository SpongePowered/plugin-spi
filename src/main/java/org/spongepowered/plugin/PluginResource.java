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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a resource that has been located by a {@link PluginResourceLocatorService locator}.
 */
public interface PluginResource {

    /**
     * @return The name of the {@link PluginResourceLocatorService service} that located this resource
     */
    String locator();

    /**
     * @return The path where this resource originates from
     */
    Path path();

    /**
     * Retrieve a {@link String property} of this resource by {@link String key}.
     * <p>
     * Consult the vendor of this library for expected keys
     *
     * @param key The key
     * @return The value or {@link Optional#empty()} if not found
     */
    Optional<String> property(final String key);

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
        return this.locateResource(Objects.requireNonNull(relative, "relative")).flatMap(url -> {
            try {
                return Optional.of(url.toURL().openStream());
            } catch (final IOException ignored) {
                return Optional.empty();
            }
        });
    }
}
