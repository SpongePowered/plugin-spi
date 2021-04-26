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
package org.spongepowered.plugin.jvm.locator;

import org.spongepowered.plugin.PluginEnvironment;
import org.spongepowered.plugin.jvm.JVMConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public final class ClasspathPluginResourceLocatorService extends JVMPluginResourceLocatorService {

    private static final String NAME = "java_classpath";

    @Override
    public String getName() {
        return ClasspathPluginResourceLocatorService.NAME;
    }

    @Override
    public List<JVMPluginResource> locatePluginResources(final PluginEnvironment environment) {
        environment.getLogger().info("Locating '{}' resources...", this.getName());

        final List<JVMPluginResource> pluginFiles = new ArrayList<>();
        final Enumeration<URL> resources;
        try {
            resources = ClassLoader.getSystemClassLoader().getResources(this.getMetadataPath());
        } catch (final IOException e) {
            throw new RuntimeException("Failed to enumerate classloader resources!");
        }

        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();

            final URI uri;
            try {
                uri = url.toURI();
            } catch (final URISyntaxException e) {
                environment.getLogger().error("Malformed URL '{}'. Skipping...", url, e);
                continue;
            }
            final Path path;

            // Jars
            if (uri.getRawSchemeSpecificPart().contains("!")) {

                final URI parentUri;
                try {
                    parentUri = new URI(uri.getRawSchemeSpecificPart().split("!")[0]);
                } catch (final URISyntaxException e) {
                    environment.getLogger().error("Malformed URI for Jar '{}. Skipping...", url, e);
                    continue;
                }

                path = Paths.get(parentUri);

                try (final JarFile jf = new JarFile(path.toFile())) {
                    pluginFiles.add(new JVMPluginResource(this.getName(), ResourceType.JAR, path, jf.getManifest()));
                } catch (final IOException e) {
                    environment.getLogger().error("Error reading '{}' as a Jar file. Skipping...", url, e);
                }
            } else {
                try {
                    path = Paths.get(new URI("file://" + uri.getRawSchemeSpecificPart().substring(0, uri.getRawSchemeSpecificPart().length() - (this.getMetadataPath().length()))));
                } catch (final URISyntaxException e) {
                    environment.getLogger().error("Error creating root URI for '{}'. Skipping...", url, e);
                    continue;
                }

                // Manifests may not be present in unpacked plugins
                final Path manifestPath = path.resolve(JVMConstants.Manifest.LOCATION);

                Manifest manifest = null;
                try (final InputStream stream = Files.newInputStream(manifestPath)) {
                    manifest = new Manifest(stream);
                } catch (final NoSuchFileException ignored) {
                    //
                } catch (final IOException e) {
                    environment.getLogger().error("Malformed URL '{}' in locator '{}'. Skipping...", url, e);
                    continue;
                }

                if (manifest != null && !this.isValidManifest(environment, manifest)) {
                    environment.getLogger().error("Manifest specified in '{}' is not valid. Skipping...", url);
                    continue;
                }

                pluginFiles.add(new JVMPluginResource(this.getName(), ResourceType.DIRECTORY, path, manifest));
            }
        }

        environment.getLogger().info("Located [{}] resource(s) for '{}'...", pluginFiles.size(), this.getName());

        return pluginFiles;
    }
}
