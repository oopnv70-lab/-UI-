package com.antisoium.ui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 反钠UI (Anti-Sodium UI)
 * 
 * Hook Sodium 的视频设置界面替换，恢复原版 Minecraft 视频设置界面。
 * 原理：Sodium 启动时会将视频设置替换为自己的界面，
 * 此 Mixin 拦截该替换过程，用反射构造原版 VideoSettingsScreen 替换回去。
 */
public class AntiSodiumUIMixin {

    private static void replaceWithVanilla(Screen parent,
                                           CallbackInfoReturnable<Screen> cir) {
        try {
            Screen currentScreen = parent;
            java.lang.reflect.Field minecraftField =
                Screen.class.getDeclaredField("minecraft");
            minecraftField.setAccessible(true);
            Minecraft mc = (Minecraft) minecraftField.get(currentScreen);
            Options options = mc.options;

            Class<?> vanillaVideoSettingsClass =
                Class.forName("net.minecraft.client.gui.screens.options.VideoSettingsScreen");
            java.lang.reflect.Constructor<?> constructor =
                vanillaVideoSettingsClass.getDeclaredConstructor(
                    Screen.class, Minecraft.class, Options.class);
            constructor.setAccessible(true);

            Screen vanillaScreen = (Screen) constructor.newInstance(
                parent, mc, options);
            cir.setReturnValue(vanillaScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}