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
import org.spongepowered.plugin.builtin.jvm.JVMPluginResource;
import org.spongepowered.plugin.discovery.PluginResourceLocator;
import org.spongepowered.plugin.discovery.UnknownResourceStrategy;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public final class ClasspathPluginResourceLocator implements PluginResourceLocator {

    @Override
    public String name() {
        return "java_classpath";
    }

    @Override
    public Set<Result> locatePluginResources(final Environment environment) {
        final String[] cp = System.getProperty("java.class.path").split(File.pathSeparator);

        final Set<Result> results = new HashSet<>();
        for (final String str : cp) {
            if (str.endsWith(".jar")) {
                final Path path = Paths.get(str);
                results.add(new Result(JVMPluginResource.create(environment, path), UnknownResourceStrategy.IGNORE));
            }
        }
        return results;
    }
}
