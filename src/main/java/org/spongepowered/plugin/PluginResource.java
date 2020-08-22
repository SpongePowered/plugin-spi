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
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.StringJoiner;

public class PluginResource {

    private final String locator;
    private final Path path;

    private FileSystem fileSystem;

    public PluginResource(final String locator, final Path path) {
        this.locator = locator;
        this.path = path;
    }

    public String getLocator() {
        return this.locator;
    }

    public Path getPath() {
        return this.path;
    }

    public FileSystem getFileSystem() {
        if (this.fileSystem == null) {
            try {
                this.fileSystem = FileSystems.newFileSystem(this.getPath(), this.getClass().getClassLoader());
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return this.fileSystem;
    }

    protected StringJoiner toStringJoiner() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("locator='" + this.locator + "'")
                .add("path=" + this.path)
                .add("fileSystem=" + this.fileSystem);
    }

    @Override
    public String toString() {
        return this.toStringJoiner().toString();
    }
}
