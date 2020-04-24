package org.spongepowered.launch.handler;

import cpw.mods.gross.Java9ClassLoaderUtil;
import cpw.mods.modlauncher.api.ILaunchHandlerService;
import cpw.mods.modlauncher.api.ITransformingClassLoader;
import cpw.mods.modlauncher.api.ITransformingClassLoaderBuilder;
import org.spongepowered.launch.Launcher;

import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public final class ClientDevLaunchHandler implements ILaunchHandlerService {

    @Override
    public String name() {
        return "sponge_client_dev";
    }

    @Override
    public void configureTransformationClassLoader(final ITransformingClassLoaderBuilder builder) {
        // Allow the entire classpath to be transformed...for now
        for (final URL url : Java9ClassLoaderUtil.getSystemClassPathURLs()) {
            try {
                builder.addTransformationPath(Paths.get(url.toURI()));
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Callable<Void> launchService(String[] arguments, final ITransformingClassLoader launchClassLoader) {

        Launcher.getLogger().info("Bootstrapping Minecraft client, please wait...");

        final String[] args = arguments;

        return () -> {
            final Class<?> mcClass = Class.forName("net.minecraft.client.main.Main", true, launchClassLoader.getInstance());
            final Method mcClassMethod = mcClass.getMethod("main", String[].class);
            mcClassMethod.invoke(null, (Object) args);
            return null;
        };
    }
}
