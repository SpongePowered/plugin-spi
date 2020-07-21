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

import com.google.inject.Injector;

import java.nio.file.Path;
import java.util.List;

public final class PluginKeys {

    /**
     * Indicates whether the target environment is a development environment.
     * <p>
     * The implementation may choose to interpret this flag in a number of ways,
     * for example it may disable certain behaviour in a development environment - or
     * even change the way it handles some behaviour entirely.
     */
    public static final Blackboard.Key<Boolean> DEVELOPMENT = Blackboard.Key.of("development", Boolean.class);

    public static final Blackboard.Key<String> VERSION = Blackboard.Key.of("version", String.class);

    public static final Blackboard.Key<Path> BASE_DIRECTORY = Blackboard.Key.of("base_directory", Path.class);

    public static final Blackboard.Key<List<Path>> PLUGIN_DIRECTORIES = Blackboard.Key.of("plugin_directories", List.class);

    public static final Blackboard.Key<Injector> PARENT_INJECTOR = Blackboard.Key.of("parent_injector", Injector.class);
}
