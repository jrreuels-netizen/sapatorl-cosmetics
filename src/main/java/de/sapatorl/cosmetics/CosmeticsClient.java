package de.sapatorl.cosmetics;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class CosmeticsClient implements ClientModInitializer {
    public static final String MOD_ID = "sapatorl_cosmetics";

    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));

    private static KeyMapping openKey;

    @Override
    public void onInitializeClient() {
        Cosmetics.load();

        openKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.sapatorl_cosmetics.open",
                InputConstants.KEY_K,
                CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.consumeClick()) {
                if (client.player != null) {
                    client.setScreenAndShow(new CosmeticsScreen());
                }
            }
        });
    }
}
