/*
 * BetterStaffChat - LibraryLoader.java
 * Copyright (C) 2021 AusTech Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.austech.betterstaffchat.common.dependency;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import dev.austech.betterstaffchat.common.util.LogUtil;
import me.lucko.jarrelocator.Relocation;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public final class LibraryLoader {
    private static final Supplier<Method> ADD_URL_METHOD = Suppliers.memoize(() -> {
        try {
            Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addUrlMethod.setAccessible(true);
            return addUrlMethod;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    });

    public static void loadAll(Object object) {
        loadAll(object.getClass());
    }

    public static void loadAll(Class<?> clazz) {
        MavenLibrary[] libs = clazz.getDeclaredAnnotationsByType(MavenLibrary.class);
        if (libs == null) {
            return;
        }

        for (MavenLibrary lib : libs) {
            load(lib.groupId(), lib.artifactId(), lib.version(), lib.repo().url());
        }
    }

    public static void load(String groupId, String artifactId, String version, Object plugin) {
        load(groupId, artifactId, version, "https://repo1.maven.org/maven2", plugin);
    }


    public static void load(String groupId, String artifactId, String version, String repoUrl, Object plugin) {
        load(new Dependency(groupId, artifactId, version, repoUrl), plugin, false);
    }

    public static void load(Dependency d, Object plugin, boolean relocate) {
        LogUtil.logPrefixDebug(plugin, String.format("&7Loading dependency %s:%s:%s", d.getGroupId(), d.getArtifactId(), d.getVersion(), d.getRepoUrl()));
        String name = d.getArtifactId() + "-" + d.getVersion();

        File saveLocation = new File(getLibFolder(plugin), name + ".jar");

        if (relocate) {
            saveLocation = new File(getLibFolder(plugin), name + ".original.jar");
        }

        if (!saveLocation.exists() && !new File(getLibFolder(plugin), name + ".jar").exists()) {
            try {
                LogUtil.logPrefixDebug(plugin, "&6Dependency '" + name + "' is not already in the libraries folder. Attempting to download...");
                URL url = d.getUrl();

                try (InputStream is = url.openStream()) {
                    Files.copy(is, saveLocation.toPath());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (relocate) {
                ArrayList<Relocation> ruleSet = Lists.newArrayList();

                ruleSet.add(new me.lucko.jarrelocator.Relocation(d.getGroupId(), "dev.austech.betterstaffchat.shaded." + d.getGroupId()));

                try {
                    new me.lucko.jarrelocator.JarRelocator(saveLocation, new File(getLibFolder(plugin), name + ".jar"), ruleSet).run();
                    saveLocation.delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            LogUtil.logPrefixDebug(plugin, "&aSuccessfully downloaded '" + name + "'");
        }

        if (!relocate && !saveLocation.exists() && !saveLocation.exists()) {
            throw new RuntimeException("Unable to download dependency: " + d.toString());
        } else if (!relocate && !new File(getLibFolder(plugin), name + ".jar").exists() && !new File(getLibFolder(plugin), name + ".jar").exists()) {
            throw new RuntimeException("Unable to download dependency: " + d.toString());
        }

        URLClassLoader classLoader = (URLClassLoader) plugin.getClass().getClassLoader();
        try {
            ADD_URL_METHOD.get().invoke(classLoader, relocate ? new File(getLibFolder(plugin), name + ".jar").toURI().toURL() : saveLocation.toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException("&cUnable to load dependency: " + saveLocation.toString(), e);
        }

        LogUtil.logPrefixDebug(plugin, "&aLoaded dependency '" + name + "' successfully.");
    }
    
    public static void loadRelocate(String groupId, String artifactId, String version, String repoUrl, Object plugin) {
            load(new Dependency(groupId, artifactId, version, repoUrl), plugin, true);
    }

    private static File getLibFolder(Object plugin) {
        File pluginDataFolder = null;

        try {
            pluginDataFolder = (File) plugin.getClass().getDeclaredMethod("getPluginDataFolder").invoke(plugin);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }

        File libs = new File(pluginDataFolder, "libraries");
        libs.mkdirs();
        return libs;
    }

    public static final class Dependency {
        private final String groupId;
        private final String artifactId;
        private final String version;
        private final String repoUrl;

        public Dependency(String groupId, String artifactId, String version, String repoUrl) {
            this.groupId = Objects.requireNonNull(groupId, "groupId");
            this.artifactId = Objects.requireNonNull(artifactId, "artifactId");
            this.version = Objects.requireNonNull(version, "version");
            this.repoUrl = Objects.requireNonNull(repoUrl, "repoUrl");
        }

        public String getGroupId() {
            return this.groupId;
        }

        public String getArtifactId() {
            return this.artifactId;
        }

        public String getVersion() {
            return this.version;
        }

        public String getRepoUrl() {
            return this.repoUrl;
        }

        public URL getUrl() throws MalformedURLException {
            String repo = this.repoUrl;
            if (!repo.endsWith("/")) {
                repo += "/";
            }
            repo += "%s/%s/%s/%s-%s.jar";

            String url = String.format(repo, this.groupId.replace(".", "/"), this.artifactId, this.version, this.artifactId, this.version);
            return new URL(url);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Dependency)) return false;
            final Dependency other = (Dependency) o;
            return this.getGroupId().equals(other.getGroupId()) &&
                    this.getArtifactId().equals(other.getArtifactId()) &&
                    this.getVersion().equals(other.getVersion()) &&
                    this.getRepoUrl().equals(other.getRepoUrl());
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.getGroupId().hashCode();
            result = result * PRIME + this.getArtifactId().hashCode();
            result = result * PRIME + this.getVersion().hashCode();
            result = result * PRIME + this.getRepoUrl().hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "LibraryLoader.Dependency(" +
                    "groupId=" + this.getGroupId() + ", " +
                    "artifactId=" + this.getArtifactId() + ", " +
                    "version=" + this.getVersion() + ", " +
                    "repoUrl=" + this.getRepoUrl() + ")";
        }
    }


}
