package dev.austech.betterstaffchat.spigot.libby;

import static java.util.Objects.requireNonNull;

import dev.austech.betterstaffchat.common.libraries.libby.LibraryManager;
import dev.austech.betterstaffchat.common.libraries.libby.classloader.URLClassLoaderHelper;
import dev.austech.betterstaffchat.common.libraries.libby.logging.adapters.JDKLogAdapter;
import java.net.URLClassLoader;
import java.nio.file.Path;
import org.bukkit.plugin.Plugin;

/**
 * A runtime dependency manager for Bukkit plugins.
 */
public class BukkitLibraryManager extends LibraryManager {
  /**
   * Plugin classpath helper
   */
  private final URLClassLoaderHelper classLoader;

  /**
   * Creates a new Bukkit library manager.
   *
   * @param plugin the plugin to manage
   */
  public BukkitLibraryManager(Plugin plugin) { this(plugin, "libs"); }

  /**
   * Creates a new Bukkit library manager.
   *
   * @param plugin the plugin to manage
   * @param directoryName download directory name
   */
  public BukkitLibraryManager(Plugin plugin, String directoryName) {
    super(new JDKLogAdapter(requireNonNull(plugin, "plugin").getLogger()),
          plugin.getDataFolder().toPath(), directoryName);
    classLoader = new URLClassLoaderHelper(
        (URLClassLoader)plugin.getClass().getClassLoader(), this);
  }

  /**
   * Adds a file to the Bukkit plugin's classpath.
   *
   * @param file the file to add
   */
  @Override
  protected void addToClasspath(Path file) {
    classLoader.addToClasspath(file);
  }
}