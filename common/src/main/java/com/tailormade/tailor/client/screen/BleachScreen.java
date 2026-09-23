package com.tailormade.tailor.client.screen;

import com.tailormade.tailor.client.menu.BleachMenu;
import com.tailormade.tailor.network.payloads.ConfirmBleachPayload;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.tailormade.tailor.Tailormade.MODID;

public class BleachScreen extends AbstractContainerScreen<BleachMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/bleaching_counter_gui.png");

    private static final int GUI_W = 110;
    private static final int GUI_H = 94;
    private static final int GUI_OFFSET_X = 9;
    private static final int GUI_OFFSET_Y = 17;

    private static final int BTN_W = 60;
    private static final int BTN_H = 20;
    private Button confirmButton;

    public BleachScreen(BleachMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = GUI_W;
        this.imageHeight = GUI_H;
    }

    @Override
    protected void init() {
        super.init();

        int btnX = leftPos + 40;
        int btnY = topPos + 80;

        confirmButton = Button.builder(Component.translatable("gui.tailormade.modal.confirm"), btn -> onConfirm()).pos(btnX, btnY).size(BTN_W, BTN_H).build();
        addRenderableWidget(confirmButton);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g, mouseX, mouseY, partialTick);
        super.render(g, mouseX, mouseY, partialTick);
        confirmButton.active = menu.canBleach();
        renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        g.blit(GUI_TEXTURE, x, y, GUI_OFFSET_X, GUI_OFFSET_Y, imageWidth, imageHeight, 128, 128);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        //
    }

    private void onConfirm() {
        if (!menu.canBleach()) return;
        NetworkManager.sendToServer(new ConfirmBleachPayload());
    }

    @Override
    public void removed() {
        super.removed();
    }
}
