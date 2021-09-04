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
package org.spongepowered.plugin.blackboard;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public final class Key<V> implements Comparable<Key<V>> {

    private final String name;
    private final Class<V> clazz;

    private Key(final String name, final Class<V> clazz) {
        this.name = Objects.requireNonNull(name, "name");
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public static <V> Key<V> of(final String name, final Class<? super V> clazz) {
        return new Key<>(name, (Class<V>) clazz);
    }

    public String name() {
        return this.name;
    }

    public Class<V> clazz() {
        return this.clazz;
    }

    @Override
    public int compareTo(final @NonNull Key<V> that) {
        if (this == that) {
            return 0;
        }

        return this.name.compareTo(that.name);
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

        return this.name.equals(((Key<?>) obj).name);
    }
}