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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public final class ClasspathPluginResourceLocatorService implements JVMPluginResourceLocatorService {

    @Override
    public String name() {
        return "java_classpath";
    }

    @Override
    public Set<JVMPluginResource> locatePluginResources(final Environment environment) {
        environment.logger().info("Locating '{}' resources...", this.name());

        final String metadataPath = environment.blackboard().get(Keys.METADATA_FILE_PATH);
        final String[] cp = System.getProperty("java.class.path").split(File.pathSeparator);

        final Set<JVMPluginResource> resources = new HashSet<>();

        for (final String str : cp) {
            if (str.endsWith(".jar")) {
                final Path path = Paths.get(str);

                try (final JarFile jar = new JarFile(path.toFile())) {
                    final ZipEntry metadataEntry = jar.getEntry(metadataPath);
                    if (metadataEntry == null) {
                        environment.logger().debug("'{}' does not contain any plugin metadata so it is not a plugin. Skipping...", str);
                        continue;
                    }

                    resources.add(JVMPluginResource.create(environment, this.name(), path));
                } catch (final IOException e) {
                    environment.logger().error("Error reading '{}' as a Jar file. Skipping...", path, e);
                }
            }
        }

        environment.logger().info("Located [{}] resource(s) for '{}'...", resources.size(), this.name());

        return resources;
    }
}
