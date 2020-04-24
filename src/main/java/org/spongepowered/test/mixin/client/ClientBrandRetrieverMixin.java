package org.spongepowered.test.mixin.client;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public abstract class ClientBrandRetrieverMixin {

    /**
     * @author Zidane
     * @reason This isn't vanilla anymore...
     */
    @Overwrite
    public static String getClientModName() {
        return "spongevanilla";
    }
}
