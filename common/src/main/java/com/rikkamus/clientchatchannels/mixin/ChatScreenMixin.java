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

    private static final int X_OFFSET = 4;
    private static final int RIGHT_PADDING = 4;
    private static final int Y_OFFSET = 12;

    @Shadow
    private EditBox input;

    public ChatScreenMixin() {
        super(null);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        final ChannelIndicatorType type = ClientChatChannelsMod.getInstance().getConfig().getChannelIndicatorType();
        if (!type.isEnabled()) return;

        final ChannelIndicator indicator = ClientChatChannelsMod.getInstance().getChannelIndicator();
        final Component indicatorComponent = type.selectIndicatorComponent(indicator);

        updateEditBoxBounds(this.minecraft.fontFilterFishy, indicatorComponent);
        renderChannelIndicator(guiGraphics, this.minecraft.fontFilterFishy, indicatorComponent);
    }

    private void updateEditBoxBounds(Font font, Component indicator) {
        final int indicatorWidth = font.width(indicator);
        final int editBoxXOffset = X_OFFSET + indicatorWidth + RIGHT_PADDING;

        this.input.setPosition(editBoxXOffset, this.height - Y_OFFSET);
        this.input.setSize(this.width - editBoxXOffset, this.input.getHeight());
    }

    private void renderChannelIndicator(GuiGraphics guiGraphics, Font font, Component indicator) {
        guiGraphics.drawString(font, indicator, X_OFFSET, this.height - Y_OFFSET, 0xFFFFFFFF);
    }

}
