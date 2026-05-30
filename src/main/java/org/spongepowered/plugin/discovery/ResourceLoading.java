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

public enum ResourceLoading {
    /**
     * The resource doesn't need to be loaded.
     * The resource will not be loaded unless another locator provided a different loading strategy.
     */
    IGNORED,
    /**
     * Assuming the resource is a jar, it will be added to a classloader and accessible by plugins.
     * The library doesn't need to access the game or to be class-transformable.
     */
    LIBRARY,
    /**
     * Assuming the resource is a jar, it will be added to a classloader and accessible by plugins.
     * The library needs access to the game or to be class-transformable.
     */
    GAME_LIBRARY;

    /**
     * Assuming two locators returned different loading strategies,
     * this will return a strategy that can satisfy both needs.
     *
     * @param other The other strategy
     * @return The resulting strategy
     */
    public ResourceLoading merge(final ResourceLoading other) {
        return this.ordinal() > other.ordinal() ? this : other;
    }
}
