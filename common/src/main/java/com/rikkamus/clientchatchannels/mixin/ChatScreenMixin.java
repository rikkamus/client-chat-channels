package com.rikkamus.clientchatchannels.mixin;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicator;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltipType;
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
        final ClientChatChannelsMod mod = ClientChatChannelsMod.getInstance();

        final ChannelIndicatorType type = mod.getConfig().getChannelIndicatorType();
        if (!type.isEnabled()) return;

        final int x = 4;
        final int y = this.height - 12;
        final int rightPadding = 4;

        final ChannelIndicator indicator = mod.getDispatcher().getChannel().getChannelIndicator();
        final Component indicatorComponent = type.selectIndicatorComponent(indicator);

        final Font font = this.minecraft.fontFilterFishy;

        // Update edit box bounds
        final int editBoxX = x + font.width(indicatorComponent) + rightPadding;

        this.input.setPosition(editBoxX, y);
        this.input.setSize(this.width - editBoxX, this.input.getHeight());

        // Render channel indicator
        guiGraphics.drawString(font, indicatorComponent, x, y, 0xFFFFFFFF);

        // Render channel tooltip
        final ChannelIndicatorTooltipType tooltipType = mod.getConfig().getChannelIndicatorTooltipType();

        if (tooltipType.isEnabled()) {
            if (mouseX >= x && mouseX <= x + font.width(indicatorComponent) && mouseY >= y && mouseY <= y + font.lineHeight) {
                guiGraphics.setComponentTooltipForNextFrame(this.minecraft.font, tooltipType.selectTooltipComponents(indicator), mouseX, mouseY);
            }
        }
    }

}
