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
package org.spongepowered.plugin.builtin.jvm.locator;

import org.spongepowered.plugin.Environment;
import org.spongepowered.plugin.blackboard.Keys;
import org.spongepowered.plugin.builtin.jvm.JVMPluginResource;
import org.spongepowered.plugin.discovery.PluginResourceLocator;
import org.spongepowered.plugin.discovery.UnknownResourceStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public final class DirectoryPluginResourceLocator implements PluginResourceLocator {

    @Override
    public String name() {
        return "java_directory";
    }

    @Override
    public Collection<Result> locatePluginResources(final Environment environment) {
        final Optional<List<Path>> dirs = environment.blackboard().find(Keys.PLUGIN_DIRECTORIES);
        if (dirs.isEmpty()) {
            environment.logger().debug("Locator '{}' is disabled.", this.name());
            return Collections.emptySet();
        }

        final Set<Result> results = new HashSet<>();
        for (final Path pluginsDir : dirs.get()) {
            if (Files.notExists(pluginsDir)) {
                environment.logger().debug("Plugin directory '{}' does not exist. Skipping...", pluginsDir);
                continue;
            }

            try (final Stream<Path> st = Files.walk(pluginsDir)) {
                st.filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".jar"))
                        .map(path -> new Result(JVMPluginResource.create(environment, path), UnknownResourceStrategy.WARN))
                        .forEach(results::add);
            } catch (final IOException ex) {
                environment.logger().error("Error walking plugins directory {}", pluginsDir, ex);
            }
        }
        return results;
    }
}
