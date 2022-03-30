package dev.austech.betterstaffchat.spigot;

import dev.austech.betterstaffchat.common.libraries.KotlinLibraries;
import dev.austech.betterstaffchat.common.libraries.libby.LibraryManager;
import dev.austech.betterstaffchat.spigot.libby.BukkitLibraryManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BSCSpigotBootstrap extends JavaPlugin {
    private BSCSpigot implPlugin;

    @Override
    public void onLoad() {
        LibraryManager libraryManager = new BukkitLibraryManager(this);
        libraryManager.addMavenCentral();
        libraryManager.addJitPack();

        libraryManager.loadLibrary(KotlinLibraries.STDLIB);
        libraryManager.loadLibrary(KotlinLibraries.STDLIB_COMMON);
        libraryManager.loadLibrary(KotlinLibraries.STDLIB_JDK8);

        implPlugin = new BSCSpigot(this);
        implPlugin.onLoad();
    }

    @Override
    public void onEnable() {
        implPlugin.onEnable();
    }
}
