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

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

public final class PluginMetadata {

    public static Builder builder() {
        return new Builder();
    }

    private final String id;
    private final String name;
    private final String version;
    private final String description;
    private final String author;

    private PluginMetadata(final Builder builder) {
        Preconditions.checkNotNull(builder);

        this.id = builder.id;
        this.name = builder.name;
        this.version = builder.version;
        this.author = builder.author;
        this.description = builder.description;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return this.description;
    }

    public String getAuthor() {
        return this.author;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("id", this.id)
            .add("name", this.name)
            .add("version", this.version)
            .add("author", this.author)
            .add("description", this.description)
            .toString();
    }

    public static final class Builder {
        String id, name, version, description, author;

        public Builder setId(final String id) {
            this.id = id;
            return this;
        }

        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        public Builder setVersion(final String version) {
            this.version = version;
            return this;
        }

        public Builder setDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder setAuthor(final String author) {
            this.author = author;
            return this;
        }

        public PluginMetadata build() {
            Preconditions.checkNotNull(this.id);

            if (this.name == null) {
                this.name = id;
            }

            return new PluginMetadata(this);
        }
    }
}
