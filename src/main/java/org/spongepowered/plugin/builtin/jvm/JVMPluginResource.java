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
package org.spongepowered.plugin.builtin.jvm;

import org.spongepowered.plugin.Environment;
import org.spongepowered.plugin.discovery.PluginResource;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.Manifest;

public interface JVMPluginResource extends PluginResource {

    Manifest manifest();

    @Override
    default Optional<String> property(final String key) {
        return Optional.ofNullable(this.manifest().getMainAttributes().getValue(Objects.requireNonNull(key, "key")));
    }

    /**
     * Creates a {@link JVMPluginResource} from an array of paths.
     * Each path can point to a JAR file or a directory.
     * When multiple paths are given, the resulting resource is a virtual union of all files contained by these paths.
     *
     * @param environment The environment
     * @param paths The paths
     * @return The {@link JVMPluginResource}.
     */
    static JVMPluginResource create(final Environment environment, final Path... paths) {
        return environment.blackboard().get(JVMKeys.JVM_PLUGIN_RESOURCE_FACTORY).create(paths);
    }

    interface Factory {
        JVMPluginResource create(final Path[] paths);
    }
}
