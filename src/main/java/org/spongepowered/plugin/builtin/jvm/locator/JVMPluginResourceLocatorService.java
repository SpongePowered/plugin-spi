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
import org.spongepowered.plugin.PluginResourceLocatorService;
import org.spongepowered.plugin.builtin.jvm.JVMConstants;

import java.util.jar.Manifest;

public abstract class JVMPluginResourceLocatorService implements PluginResourceLocatorService<JVMPluginResource> {

    public static final String DEFAULT_METADATA_FILENAME = "plugins.json";

    public static final String DEFAULT_METADATA_FILE =
            JVMConstants.META_INF + "/" + JVMPluginResourceLocatorService.DEFAULT_METADATA_FILENAME;

    public boolean isValidManifest(final Environment environment, final Manifest manifest) {
        return true;
    }

    public String metadataPath() {
        return JVMPluginResourceLocatorService.DEFAULT_METADATA_FILE;
    }
}
