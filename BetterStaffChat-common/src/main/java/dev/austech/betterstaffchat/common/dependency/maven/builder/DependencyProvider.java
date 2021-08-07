/*
 * MIT License
 *
 * Copyright (c) 2021 Justin Heflin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is apart of PluginBase (https://github.com/Demeng7215/PluginBase)
 */

package dev.austech.betterstaffchat.common.dependency.maven.builder;

import dev.austech.betterstaffchat.common.dependency.generics.TypeDefinition;
import dev.austech.betterstaffchat.common.dependency.maven.Dependency;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Base level dependency provider.
 *
 * @param <T> The dependency this class provides
 */
public interface DependencyProvider<T extends Dependency> extends TypeDefinition<T> {

    /**
     * Gets the set of dependencies attached to this provider.
     *
     * @return The set of dependencies attached to this provider
     */
    @NotNull Set<T> getDependencies();
}