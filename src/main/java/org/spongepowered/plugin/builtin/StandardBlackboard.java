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
package org.spongepowered.plugin.builtin;

import org.spongepowered.plugin.blackboard.Blackboard;
import org.spongepowered.plugin.blackboard.Key;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class StandardBlackboard implements Blackboard {

    private final Map<Key<Object>, Object> values;

    public StandardBlackboard() {
        this.values = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V getOrCreate(final Key<V> key, final Supplier<? super V> defaultValue) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(defaultValue, "defaultValue");

        return key.clazz().cast(this.values.computeIfAbsent((Key<Object>) key, k -> defaultValue.get()));
    }

    @Override
    public <V> V get(final Key<V> key) {
        final V value = Objects.requireNonNull(key, "key").clazz().cast(this.values.get(key));
        if (value == null) {
            throw new IllegalArgumentException(String.format("Key '%s' has no value!", key.name()));
        }
        return value;
    }

    @Override
    public <V> Optional<V> find(final Key<V> key) {
        return Optional.ofNullable(Objects.requireNonNull(key, "key").clazz().cast(this.values.get(key)));
    }
}
