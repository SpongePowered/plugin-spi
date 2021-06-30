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
package org.spongepowered.plugin.jvm.locator;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.plugin.PluginResource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.jar.Manifest;

public class JVMPluginResource implements PluginResource {

    private final String locator;
    private final Path path;
    private final ResourceType type;
    private final Manifest manifest;

    private FileSystem fileSystem;

    public JVMPluginResource(final String locator, final ResourceType type, final Path path, @Nullable final Manifest manifest) {
        this.locator = Objects.requireNonNull(locator, "locator");
        this.path = Objects.requireNonNull(path, "path");
        this.type = Objects.requireNonNull(type, "type");
        this.manifest = manifest;
    }

    @Override
    public final String locator() {
        return this.locator;
    }

    public final ResourceType type() {
        return this.type;
    }

    public final Path path() {
        return this.path;
    }

    public final Optional<Manifest> manifest() {
        return Optional.ofNullable(this.manifest);
    }

    @Override
    public Optional<URI> locateResource(final URI relative) {
        switch (this.type) {
            case DIRECTORY:
                final ClassLoader classLoader = this.getClass().getClassLoader();
                final URL resolved = classLoader.getResource(relative.getPath());
                try {
                    if (resolved == null) {
                        return Optional.empty();
                    }
                    return Optional.of(resolved.toURI());
                } catch (final URISyntaxException ignored) {
                    return Optional.empty();
                }
            case JAR:
                final Path file = this.fileSystem().getPath(relative.getPath());
                return Files.exists(file) ? Optional.of(file.toUri()) : Optional.empty();
            default:
                throw new IllegalStateException("Unknown resource type " + this.type);
        }
    }

    public FileSystem fileSystem() {
        if (this.fileSystem == null) {
            try {
                this.fileSystem = FileSystems.newFileSystem(this.path, this.getClass().getClassLoader());
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return this.fileSystem;
    }

    protected StringJoiner toStringJoiner() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
            .add("locator='" + this.locator + "'")
            .add("type=" + this.type)
            .add("path=" + this.path)
            .add("fileSystem=" + this.fileSystem)
        ;
    }

    @Override
    public String toString() {
        return this.toStringJoiner().toString();
    }
}
