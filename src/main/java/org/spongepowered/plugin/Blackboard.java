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

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

// Inspired by ModLauncher's TypesafeMap/Environment keys concept but simplified to one map
public final class Blackboard {

    private final Map<Key<Object>, Object> values;

    public Blackboard() {
        this.values = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <V> V getOrCreate(final Key<V> key, final Supplier<V> defaultValue) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(defaultValue);

        return key.clazz.cast(this.values.computeIfAbsent((Key<Object>) key, k -> defaultValue.get()));
    }

    public <V> Optional<V> get(final Key<V> key) {
        Preconditions.checkNotNull(key);

        return Optional.ofNullable((V) key.clazz.cast(this.values.get(key)));
    }

    public static final class Key<V> implements Comparable<Key<V>> {

        public static <V> Key<V> of(final String name, final Class<V> clazz) {
            return new Key<>(name, clazz);
        }

        private final String name;
        private final Class<V> clazz;

        private Key(final String name, final Class<V> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        public String getName() {
            return this.name;
        }

        public Class<V> getClazz() {
            return this.clazz;
        }

        @Override
        public int compareTo(final Blackboard.Key<V> o) {
            if (this == o) {
                return 0;
            }

            return this.name.compareTo(o.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name);
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }

            if (!(obj instanceof Key)) {
                return false;
            }

            return this.name.equals(((Key) obj).name);
        }
    }
}
