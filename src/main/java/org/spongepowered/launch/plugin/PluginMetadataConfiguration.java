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
package org.spongepowered.launch.plugin;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.launch.Launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ConfigSerializable
public final class PluginMetadataConfiguration {

    private static final ObjectMapper<PluginMetadataConfiguration> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.forClass(PluginMetadataConfiguration.class);
        } catch (ObjectMappingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static PluginMetadataConfiguration loadFrom(final String fileName, final InputStream stream) throws IOException, ObjectMappingException {
        final PluginMetadataConfiguration configuration;

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            final JSONConfigurationLoader loader = JSONConfigurationLoader.builder().setSource(() -> reader).build();
            final ConfigurationNode node = loader.load();
            configuration = MAPPER.bindToNew().populate(node);

            // Plugin validation checks
            final Iterator<PluginMetadataEntry> iter = configuration.pluginMetadataEntries.iterator();
            while (iter.hasNext()) {
                final PluginMetadataEntry pluginMetadataEntry = iter.next();

                if (pluginMetadataEntry.getId() == null) {
                    // TODO Use regex to check for malformed plugin id, this will do for the moment
                    Launcher.getLogger().error("Plugin specified with no id in '{}'. This plugin will be skipped...", fileName);
                    iter.remove();
                    continue;
                }

                if (pluginMetadataEntry.getVersion() == null) {
                    // TODO Enforce sane versioning...maybe
                    Launcher.getLogger().error("Plugin '{}' has no version specified. This plugin will be skipped...", pluginMetadataEntry.getId());
                    iter.remove();
                }

                if (pluginMetadataEntry.getName() == null) {
                    Launcher.getLogger().error("Plugin '{}' has no name specified. Using the id instead...", pluginMetadataEntry.getId());
                    pluginMetadataEntry.setName(pluginMetadataEntry.getId());
                }
            }
        }

        return configuration;
    }

    @Setting(value = "plugins")
    private List<PluginMetadataEntry> pluginMetadataEntries = new ArrayList<>();

    public List<PluginMetadataEntry> getPluginMetadataEntries() {
        return this.pluginMetadataEntries;
    }
}
