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

import org.spongepowered.plugin.PluginCandidate;
import org.spongepowered.plugin.metadata.PluginMetadata;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

public final class JarPluginCandidate extends PluginCandidate {

    private final URI jarUri;
    private FileSystem fileSystem;

    public JarPluginCandidate(final PluginMetadata metadata, final Path file) {
        super(metadata, file);
        try {
            this.jarUri = new URI("jar:" + file.toUri().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Should be impossible!");
        }
    }

    public FileSystem getFileSystem() {
        if (this.fileSystem == null) {
            try {
                this.fileSystem = FileSystems.newFileSystem(this.jarUri, Collections.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException("Should be impossible!");
            }
        }

        return this.fileSystem;
    }
}
