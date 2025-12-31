package com.rikkamus.clientchatchannels.mixin;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicator;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {

    @Shadow
    private EditBox input;

    private ChatScreenMixin() {
        super(null);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        final int xOffset = 4;
        final int rightPadding = 4;
        final int yOffset = 12;

        final ChannelIndicatorType type = ClientChatChannelsMod.getInstance().getConfig().getChannelIndicatorType();
        if (!type.isEnabled()) return;

        final ChannelIndicator indicator = ClientChatChannelsMod.getInstance().getChannelIndicator();
        final Component indicatorComponent = type.selectIndicatorComponent(indicator);

        final Font font = this.minecraft.fontFilterFishy;

        // Update edit box bounds
        final int editBoxXOffset = xOffset + font.width(indicatorComponent) + rightPadding;

        this.input.setPosition(editBoxXOffset, this.height - yOffset);
        this.input.setSize(this.width - editBoxXOffset, this.input.getHeight());

        // Render channel indicator
        guiGraphics.drawString(font, indicatorComponent, xOffset, this.height - yOffset, 0xFFFFFFFF);
    }

}
