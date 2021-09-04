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
package org.spongepowered.plugin.builtin;

import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.PluginResource;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.util.StringJoiner;

public final class StandardPluginCandidate<P extends PluginResource> implements PluginCandidate<P> {

    private final PluginMetadata metadata;
    private final P resource;

    public StandardPluginCandidate(final PluginMetadata metadata, final P resource) {
        this.metadata = metadata;
        this.resource = resource;
    }

    @Override
    public PluginMetadata metadata() {
        return this.metadata;
    }

    @Override
    public P resource() {
        return this.resource;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StandardPluginCandidate.class.getSimpleName() + "[", "]")
                .add("metadata=" + this.metadata)
                .add("resource=" + this.resource)
                .toString();
    }
}
