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
import org.spongepowered.plugin.builtin.jvm.JVMKeys;
import org.spongepowered.plugin.builtin.jvm.JVMPluginResource;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public final class EnvironmentPluginResourceLocatorService implements JVMPluginResourceLocatorService {

    @Override
    public String name() {
        return "java_environment";
    }

    @Override
    public Set<JVMPluginResource> locatePluginResources(final Environment environment) {
        final Optional<String> envName = environment.blackboard().find(JVMKeys.ENVIRONMENT_LOCATOR_VARIABLE_NAME);
        if (envName.isEmpty()) {
            environment.logger().debug("Locator '{}' is disabled.", this.name());
            return Collections.emptySet();
        }

        environment.logger().info("Locating '{}' resources...", this.name());

        final Set<JVMPluginResource> resources = new HashSet<>();
        final String env = System.getenv(envName.get());
        if (env != null) {
            for (final String entry : env.split(";")) {
                if (entry.isBlank()) {
                    continue;
                }

                final Path[] paths = Stream.of(entry.split("&")).map(Path::of).toArray(Path[]::new);
                resources.add(JVMPluginResource.create(environment, this.name(), paths));
            }
        }

        environment.logger().info("Located [{}] resource(s) for '{}'...", resources.size(), this.name());

        return resources;
    }
}
