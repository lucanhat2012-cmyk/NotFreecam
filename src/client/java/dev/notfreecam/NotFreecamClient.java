package dev.notfreecam;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.ArmorStand;

public final class NotFreecamClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("notfreecam", "main"));
    private static KeyMapping toggleKey;
    private static KeyMapping settingsKey;

    private static ArmorStand camera;
    private static float speed = 0.65F;
    private static boolean active;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.notfreecam.toggle", InputConstants.Type.KEYSYM,
                InputConstants.KEY_F4, CATEGORY));
        settingsKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.notfreecam.settings", InputConstants.Type.KEYSYM,
                InputConstants.KEY_F6, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                if (active) disable(client);
                else enable(client);
            }
            while (settingsKey.consumeClick()) {
                if (client.player != null) client.setScreen(new FreecamSettingsScreen(client.screen));
            }

            if (active) tickCamera(client);
        });
    }

    private static void enable(Minecraft client) {
        if (client.player == null || client.level == null) return;

        var player = client.player;
        camera = new ArmorStand(client.level, player.getX(), player.getY() + player.getEyeHeight(),
                player.getZ());
        camera.setNoGravity(true);
        camera.noPhysics = true;
        camera.setInvisible(true);
        camera.setYRot(player.getYRot());
        camera.setXRot(player.getXRot());
        camera.setYHeadRot(player.getYRot());
        client.setCameraEntity(camera);
        active = true;
    }

    private static void disable(Minecraft client) {
        if (client.player != null) client.setCameraEntity(client.player);
        camera = null;
        active = false;
    }

    private static void tickCamera(Minecraft client) {
        if (client.player == null || client.level == null || camera == null) {
            disable(client);
            return;
        }

        float yaw = camera.getYRot();
        double forward = (client.options.keyUp.isDown() ? 1.0 : 0.0)
                - (client.options.keyDown.isDown() ? 1.0 : 0.0);
        double strafe = (client.options.keyRight.isDown() ? 1.0 : 0.0)
                - (client.options.keyLeft.isDown() ? 1.0 : 0.0);
        double vertical = (client.options.keyJump.isDown() ? 1.0 : 0.0)
                - (client.options.keyShift.isDown() ? 1.0 : 0.0);

        double length = Math.sqrt(forward * forward + strafe * strafe);
        if (length > 0.0) {
            forward /= length;
            strafe /= length;
        }

        double radians = Math.toRadians(yaw);
        double dx = (-Math.sin(radians) * forward + Math.cos(radians) * strafe) * speed;
        double dz = (Math.cos(radians) * forward + Math.sin(radians) * strafe) * speed;
        double dy = vertical * speed;

        if (client.options.keySprint.isDown()) {
            dx *= 2.5;
            dy *= 2.5;
            dz *= 2.5;
        }

        camera.setPos(camera.getX() + dx, camera.getY() + dy, camera.getZ() + dz);
    }

    static boolean isActive() { return active; }
    static float getSpeed() { return speed; }
    static void setSpeed(float value) { speed = Math.max(0.1F, Math.min(5.0F, value)); }
}
