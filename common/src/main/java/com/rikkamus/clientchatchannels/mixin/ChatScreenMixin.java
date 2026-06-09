package com.rikkamus.clientchatchannels.mixin;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicator;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltipType;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {

    @Shadow
    private EditBox input;

    @Unique
    private ChannelIndicatorType clientchatchannels$indicatorType;

    private ChatScreenMixin() {
        super(null);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        this.clientchatchannels$indicatorType = ClientChatChannelsMod.getInstance().getConfig().getChannelIndicatorType();
    }

    @Inject(method = "extractRenderState", at = @At(value = "HEAD"))
    private void beforeRender(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!this.clientchatchannels$indicatorType.isEnabled()) return;

        final ClientChatChannelsMod mod = ClientChatChannelsMod.getInstance();

        final int y = this.height - ChatScreenMixinConstants.Y;

        final ChannelIndicator indicator = mod.getDispatcher().getChannel().getChannelIndicator();
        final Component indicatorComponent = this.clientchatchannels$indicatorType.selectIndicatorComponent(indicator);

        final Font font = this.minecraft.fontFilterFishy;

        // Update edit box bounds
        final int editBoxX = ChatScreenMixinConstants.X + font.width(indicatorComponent) + ChatScreenMixinConstants.RIGHT_PADDING;

        this.input.setPosition(editBoxX, y);
        this.input.setSize(this.width - editBoxX, this.input.getHeight());
    }

    @Inject(method = "extractRenderState", at = @At(value = "TAIL"))
    private void afterRender(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!this.clientchatchannels$indicatorType.isEnabled()) return;

        final ClientChatChannelsMod mod = ClientChatChannelsMod.getInstance();

        final int y = this.height - ChatScreenMixinConstants.Y;

        final ChannelIndicator indicator = mod.getDispatcher().getChannel().getChannelIndicator();
        final Component indicatorComponent = this.clientchatchannels$indicatorType.selectIndicatorComponent(indicator);

        final Font font = this.minecraft.fontFilterFishy;

        // Render channel indicator
        guiGraphics.text(font, indicatorComponent, ChatScreenMixinConstants.X, y, 0xFFFFFFFF);

        // Render channel tooltip
        final ChannelIndicatorTooltipType tooltipType = mod.getConfig().getChannelIndicatorTooltipType();

        if (tooltipType.isEnabled()) {
            if (mouseX >= ChatScreenMixinConstants.X && mouseX <= ChatScreenMixinConstants.X + font.width(indicatorComponent) && mouseY >= y && mouseY <= y + font.lineHeight) {
                guiGraphics.setComponentTooltipForNextFrame(this.minecraft.font, tooltipType.selectTooltipComponents(indicator), mouseX, mouseY);
            }
        }
    }

}
