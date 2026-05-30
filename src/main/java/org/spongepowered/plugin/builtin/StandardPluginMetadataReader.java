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

import org.spongepowered.plugin.Environment;
import org.spongepowered.plugin.discovery.PluginMetadataReader;
import org.spongepowered.plugin.discovery.PluginResource;
import org.spongepowered.plugin.blackboard.Keys;
import org.spongepowered.plugin.discovery.PluginResourceLocator;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.builtin.MetadataParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class StandardPluginMetadataReader implements PluginMetadataReader {

    @Override
    public String name() {
        return "standard";
    }

    @Override
    public Collection<? extends PluginMetadata> readPluginMetadata(final Environment environment, final PluginResource resource, final List<PluginResourceLocator> locators) throws Exception {
        final Set<PluginMetadata> result = new LinkedHashSet<>();
        for (final String metadataPath : environment.blackboard().get(Keys.METADATA_FILE_PATHS)) {
            final Optional<InputStream> stream = resource.openResource(metadataPath);
            if (stream.isPresent()) {
                try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream.get(), StandardCharsets.UTF_8))) {
                    result.addAll(MetadataParser.read(reader).metadata());
                }
            }
        }
        return result;
    }
}
