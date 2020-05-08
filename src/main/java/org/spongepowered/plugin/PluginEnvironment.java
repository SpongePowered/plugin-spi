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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public final class PluginEnvironment {

    private final Logger logger;
    private final Blackboard blackboard;
    private Map<String, PluginLanguageService> languageServices;

    public PluginEnvironment() {
        this.logger = LogManager.getLogger("Plugin");
        this.blackboard = new Blackboard();
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Blackboard getBlackboard() {
        return this.blackboard;
    }

    public Map<String, PluginLanguageService> getLanguageServices() {
        return Collections.unmodifiableMap(this.languageServices);
    }

    public void setLanguageServices(final Map<String, PluginLanguageService> languageServices) {
        if (this.languageServices != null) {
            throw new RuntimeException("Plugin language services are not allowed to be re-assigned!");
        }

        this.languageServices = languageServices;
    }
}
