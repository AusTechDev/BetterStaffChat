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
 */

package dev.austech.betterstaffchat.common.dependency;

import dev.austech.betterstaffchat.common.dependency.maven.builder.MavenDependencyProvider;
import dev.austech.betterstaffchat.common.dependency.maven.builder.MavenDependencyProviderBuilder;
import dev.austech.betterstaffchat.common.dependency.relocation.RelocationInfo;

public class BetterStaffChatDependencyProvider {
    public static MavenDependencyProvider getDependencies(boolean discordEnabled, boolean sentryEnabled) {
        MavenDependencyProviderBuilder builder = new MavenDependencyProviderBuilder();

        if (discordEnabled) {
            builder.dependency("org|slf4j", "slf4j-api", "1.7.25");
            builder.dependency("org|slf4j", "slf4j-nop", "1.7.25");
            builder.relocation("org|slf4j", "dev.austech.betterstaffchat.shaded.slf4j");

            builder.repository("https://m2.dv8tion.net/releases/");
            builder.dependency("net|dv8tion", "JDA", "4.2.1_262");

            builder.relocation(RelocationInfo.of("net|dv8tion|jda", "dev.austech.betterstaffchat.shaded.jda"));

            builder.dependency("com|squareup|okhttp3", "okhttp", "3.13.0");
            builder.dependency("com|squareup|okio", "okio", "1.17.2");
            builder.dependency("org|apache|commons", "commons-collections4", "4.1");
            builder.dependency("com|fasterxml|jackson|core", "jackson-databind", "2.10.1");
            builder.dependency("com|fasterxml|jackson|core", "jackson-core", "2.10.1");
            builder.dependency("com|fasterxml|jackson|core", "jackson-annotations", "2.10.1");

            builder.dependency("net|sf|trove4j", "trove4j", "3.0.3");
            builder.dependency("com|neovisionaries", "nv-websocket-client", "2.14");
        }
        if (sentryEnabled) {
            builder.dependency("io|sentry", "sentry", "5.0.1");
            builder.relocation("io|sentry", "dev.austech.betterstaffchat.shaded.sentry");
        }

        return (MavenDependencyProvider) builder.build();
    }
}
