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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.discovery.PluginResource;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class StandardPluginContainer implements PluginContainer {

    private final PluginResource resource;
    private final PluginMetadata metadata;
    private final Logger logger;
    private Object instance;

    public StandardPluginContainer(final PluginResource resource, final PluginMetadata metadata) {
        this(resource, metadata, LogManager.getLogger(metadata.id()));
    }

    public StandardPluginContainer(final PluginResource resource, final PluginMetadata metadata, final Logger logger) {
        this.resource = Objects.requireNonNull(resource, "resource");
        this.metadata = Objects.requireNonNull(metadata, "metadata");
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    @Override
    public final PluginMetadata metadata() {
        return this.metadata;
    }

    @Override
    public final Logger logger() {
        return this.logger;
    }

    @Override
    public final Object instance() {
        return this.instance;
    }

    protected void initializeInstance(final Object instance) {
        if (this.instance != null) {
            throw new RuntimeException(String.format("Attempt made to set the plugin within container '%s' twice!", this.metadata.id()));
        }
        this.instance = Objects.requireNonNull(instance, "instance");
    }

    @Override
    public Optional<URI> locateResource(final String path) {
        return this.resource.locateResource(path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.metadata.id());
    }

    @Override
    public boolean equals(final Object that) {
        if (that == this) {
            return true;
        }

        if (!(that instanceof PluginContainer)) {
            return false;
        }

        return this.metadata.id().equals(((PluginContainer) that).metadata().id());
    }

    protected StringJoiner toStringJoiner() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("metadata=" + this.metadata);
    }

    @Override
    public final String toString() {
        return this.toStringJoiner().toString();
    }
}
