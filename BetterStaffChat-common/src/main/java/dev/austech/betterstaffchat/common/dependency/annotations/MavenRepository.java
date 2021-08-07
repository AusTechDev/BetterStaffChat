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

package dev.austech.betterstaffchat.common.dependency.annotations;

import dev.austech.betterstaffchat.common.dependency.maven.MavenRepositoryInfo;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Used to define a Maven repo URL for the dependencies.
 *
 * @see MavenRepositoryInfo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(MavenRepository.List.class)
public @interface MavenRepository {

    /**
     * The URL to the Maven repository.
     *
     * @return A string that represents the URL to a Maven repo
     */
    @NotNull String value() default "https://repo1.maven.org/maven2/";

    /**
     * Used to store multiple {@link MavenRepository} annotations on a single class type.
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {

        /**
         * An array of maven repositories.
         *
         * @return An array of {@link MavenRepository} annotations.
         */
        @NotNull MavenRepository @NotNull [] value() default {};
    }
}