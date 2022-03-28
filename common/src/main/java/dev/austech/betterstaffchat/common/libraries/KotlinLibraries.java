package dev.austech.betterstaffchat.common.libraries;

import dev.austech.betterstaffchat.common.libraries.libby.Library;

public class KotlinLibraries {
    private static final String kotlinString = "_kotlin".replace("_", "");

    private static final Library.Builder builder = new Library.Builder()
            .groupId("org{}jetbrains{}kotlin")
            .version("1.6.10")
            .relocate(kotlinString, "dev{}austech{}betterstaffchat{}depend{}kotlin");

    public static final Library STDLIB_JDK8 = builder
            .artifactId(kotlinString + "-stdlib-jdk8") // Break here because shadowJar tries to replace it.
            .build();

    public static final Library STDLIB = builder
            .artifactId(kotlinString + "-stdlib")
            .build();

    public static final Library STDLIB_COMMON = builder
            .artifactId(kotlinString + "-stdlib-common")
            .build();
}
