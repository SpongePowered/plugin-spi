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
package org.spongepowered.plugin.jvm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.locator.JVMPluginResource;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class JVMPluginContainer implements PluginContainer {

    private final PluginCandidate<JVMPluginResource> candidate;
    private final Logger logger;
    private Object instance;

    public JVMPluginContainer(final PluginCandidate<JVMPluginResource> candidate) {
        this(candidate, LogManager.getLogger(candidate.getMetadata().getId()));
    }

    public JVMPluginContainer(final PluginCandidate<JVMPluginResource> candidate, final Logger logger) {
        Objects.requireNonNull(candidate);
        Objects.requireNonNull(logger);

        this.candidate = candidate;
        this.logger = logger;
    }

    @Override
    public PluginMetadata getMetadata() {
        return this.candidate.getMetadata();
    }

    @Override
    public Path getPath() {
        return this.candidate.getResource().getPath();
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public Object getInstance() {
        return this.instance;
    }

    protected void setInstance(final Object instance) {
        if (this.instance != null) {
            throw new RuntimeException(String.format("Attempt made to set the plugin within container '%s' twice!",
                this.candidate.getMetadata().getId()));
        }
        Objects.requireNonNull(instance);
        this.instance = instance;
    }

    @Override
    public Optional<URL> locateResource(final URL relative) {
        final ClassLoader classLoader = this.getInstance().getClass().getClassLoader();
        final URL resolved = classLoader.getResource(relative.getPath());
        return Optional.ofNullable(resolved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.candidate.getMetadata().getId());
    }

    @Override
    public boolean equals(final Object that) {
        if (that == this) {
            return true;
        }

        if (!(that instanceof PluginContainer)) {
            return false;
        }

        return this.candidate.getMetadata().getId().equals(((PluginContainer) that).getMetadata().getId());
    }

    protected StringJoiner toStringJoiner() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("metadata=" + this.candidate.getMetadata())
                .add("path=" + this.candidate.getResource().getPath());
    }

    @Override
    public String toString() {
        return this.toStringJoiner().toString();
    }
}
