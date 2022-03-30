package dev.austech.betterstaffchat.bungeecord;

import dev.austech.betterstaffchat.bungeecord.libby.BungeeLibraryManager;
import dev.austech.betterstaffchat.common.libraries.KotlinLibraries;
import dev.austech.betterstaffchat.common.libraries.libby.LibraryManager;
import net.md_5.bungee.api.plugin.Plugin;

public class BSCBungeeBootstrap extends Plugin {
    private BSCBungee implPlugin;

    @Override
    public void onLoad() {
        LibraryManager libraryManager = new BungeeLibraryManager(this);
        libraryManager.addMavenCentral();
        libraryManager.addJitPack();

        libraryManager.loadLibrary(KotlinLibraries.STDLIB);
        libraryManager.loadLibrary(KotlinLibraries.STDLIB_COMMON);
        libraryManager.loadLibrary(KotlinLibraries.STDLIB_JDK8);

        implPlugin = new BSCBungee(this);
        implPlugin.onLoad();
    }

    @Override
    public void onEnable() {
        implPlugin.onEnable();
    }
}
