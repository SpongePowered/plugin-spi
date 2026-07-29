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

/**
 * Defines what happens to a resource that has not been recognized by any {@link PluginMetadataReader reader}
 * and does not contain {@link PluginResourceLocator locators} or {@link PluginMetadataReader readers}.
 *
 * @param warn Whether the platform should warn when the associated resource is not recognized.
 * @param load Whether the resource will be added to the game classloader, assuming it is a jar.
 */
public record UnknownResourceStrategy(boolean warn, boolean load) {

    /**
     * The unknown resource will be ignored and a warning logged.
     * The default strategy most locators should stick to.
     */
    public static final UnknownResourceStrategy WARN = new UnknownResourceStrategy(true, false);

    /**
     * The unknown resource will be ignored silently.
     */
    public static final UnknownResourceStrategy IGNORE = new UnknownResourceStrategy(false, false);

    /**
     * Assuming two locators returned different strategies,
     * this will return a strategy that can satisfy both needs.
     *
     * @param other The other strategy
     * @return The resulting strategy
     */
    public UnknownResourceStrategy merge(final UnknownResourceStrategy other) {
        return new UnknownResourceStrategy(this.warn || other.warn, this.load || other.load);
    }
}
