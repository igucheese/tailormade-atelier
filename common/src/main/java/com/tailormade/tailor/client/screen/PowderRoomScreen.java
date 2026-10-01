package com.tailormade.tailor.client.screen;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tailormade.tailor.client.gui.ColorPalette;
import com.tailormade.tailor.client.gui.ColorPickerWidget;
import com.tailormade.tailor.client.gui.HueBarWidget;
import com.tailormade.tailor.client.gui.PowderRoomEditableRegions;
import com.tailormade.tailor.client.renderer.SkinLayerRenderLayer;
import com.tailormade.tailor.client.renderer.TailorTextureCompositor;
import com.tailormade.tailor.data.*;
import com.tailormade.tailor.data.constants.PatternType;
import com.tailormade.tailor.data.records.PixelData;
import com.tailormade.tailor.data.records.SkinDataRecord;
import com.tailormade.tailor.data.records.UnderwearSetting;
import com.tailormade.tailor.data.constants.UnderwearType;
import com.tailormade.tailor.network.payloads.SaveSkinLayerPayload;
import com.tailormade.tailor.utils.MannequinStylePreviewHelper;
import com.tailormade.tailor.utils.editor.ColorService;
import com.tailormade.tailor.utils.editor.PixelCanvas;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static com.tailormade.tailor.Tailormade.MODID;
import static com.tailormade.tailor.data.Constants.TRANSPARENT;

public class PowderRoomScreen extends Screen {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/powder_room_gui.png");
    private static final ResourceLocation BRUSH_1_ICON =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/brush_1.png");
    private static final ResourceLocation BRUSH_2_ICON =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/brush_2.png");
    private static final ResourceLocation BRUSH_3_ICON =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/brush_3.png");
    private static final ResourceLocation BUCKET_ICON =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/bucket.png");
    private static final ResourceLocation EYEDROPPER_ICON =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/eyedropper.png");
    private static final ResourceLocation ERASER_ICON =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/eraser.png");

    private static final ResourceLocation TOOL_ICON_BRUSH =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/editor_tools_brush.png");
    private static final ResourceLocation TOOL_ICON_ERASER =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/editor_tools_eraser.png");
    private static final ResourceLocation TOOL_ICON_BUCKET =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/editor_tools_bucket.png");
    private static final ResourceLocation TOOL_ICON_EYEDROPPER =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/editor_tools_eyedropper.png");
    private static final ResourceLocation TOOL_ICON_SELECTION =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/editor_tools_selection.png");
    private static final ResourceLocation TOOL_ICON_SIZE_1 =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/brush_size_1.png");
    private static final ResourceLocation TOOL_ICON_SIZE_2 =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/brush_size_2.png");
    private static final ResourceLocation TOOL_ICON_SIZE_3 =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/brush_size_3.png");

    private static final ResourceLocation SKIN_TOGGLE_BTN_SHOW =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/skin_toggle_btn_show.png");
    private static final ResourceLocation SKIN_TOGGLE_BTN_HIDE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/skin_toggle_btn_hide.png");
    private static final ResourceLocation SEG_BUTTON_BASE =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/editor_bottom_btns_2.png");
    private static final ResourceLocation SEG_BUTTON_R =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/ebb_reg_a.png");
    private static final ResourceLocation SEG_BUTTON_S =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/ebb_slim_a.png");
    private static final ResourceLocation SEG_BUTTON_N =
            ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/editor/ebb_off_a.png");

    private static final int GUI_W = 384;
    private static final int GUI_H = 216;
    private static final int GUI_OFFSET_X = 64;
    private static final int GUI_OFFSET_Y = 148;

    private static final int ED_X = 28;
    private static final int ED_Y = 6;
    private static final int ED_W = 188;
    private static final int ED_H = 188;

    private static final int PV_X = 248;
    private static final int PV_Y = 6;
    private static final int PV_W = 124;
    private static final int PV_H = 172;
    private static final int TOGGLE_X = 246;
    private static final int TOGGLE_Y = 186;
    private static final int TOGGLE_W = 60;
    private static final int TOGGLE_H = 24;
    private static final int SEG_BTN_X = 168;
    private static final int SEG_BTN_Y = 200;

    private static final int PAL_X = 8;
    private static final int PAL_Y = 7;

    private static final int TOOLBAR_X = 223;
    private static final int TOOLBAR_Y = 10;
    private static int BRUSH_SIZE = 1;
    private static String TOOL_MODE = "brush";

    private static final int RGB_Y_OFFSET = 4;  // エディタ下端からの距離
    private static final int RGB_BOX_W = 28;
    private static final int RGB_BOX_H = 10;
    private int labelBorderColor = 0xFFFF4444;

    private int leftPos;
    private int topPos;
    private int imageWidth;
    private int imageHeight;

    private String clickedArea = null;

    private PixelCanvas canvas;
    private ColorPalette palette;
    private ColorPickerWidget colorPicker;
    private HueBarWidget hueBar;

    private float zoomScale = 1.0f;
    private float panOffsetX = 0f;
    private float panOffsetY = 0f;
    private double rightDragStartX = -1;
    private double rightDragStartY = -1;

    private EditBox rBox, gBox, bBox;
    private Button saveButton;
    private String beforeEyedropperTool = null;

    private float previewYaw = 235.0f;
    private float previewPitch = 0.0f;
    private double lastDragX;
    private boolean draggingPreview = false;
    private double dragStartX = -1;
    private double dragStartY = -1;

    private UnderwearSetting previewUnderwear = UnderwearSetting.DEFAULT;
    private UnderwearType selectedType = UnderwearType.MALE_BOXER;
    private int selectedColor = 0xFF000000;

    private TailorTextureCompositor previewCompositor;
    private boolean hasUnsavedChanges = false;

    private static final int FACE_LINE_COLOR = 0x3300DDFF;
    private static final int FACE_LABEL_COLOR = 0x7700DDFF;
    private UUID playerId = null;
    private boolean isVisible = true;
    private boolean isSlim = false;
    private boolean isGuideVisible = true;

    public PowderRoomScreen(UUID playerId) {
        super(Component.translatable("gui.tailormade.powder_room"));
        this.imageWidth = GUI_W;
        this.imageHeight = GUI_H;
        this.playerId = playerId;
    }

    @Override
    protected void init() {
        leftPos = (width  - GUI_W) / 2;
        topPos = (height - GUI_H) / 2;

        previewCompositor = TailorTextureCompositor.createForPreview(this.playerId);

        canvas = new PixelCanvas(64, 64);
        canvas.init();

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            SkinDataRecord skin = SkinDataClientCache.get(mc.player.getUUID());
            PixelData existingData = skin != null ? skin.pixelData() : null;
            int[] existing = existingData != null ? existingData.getPixels() : null;
            if (existing != null) {
                canvas.loadPixels(existing);
            } else {
                fillWithSampledSkinColor(mc.player);
            }
            if (skin != null) {
                this.isVisible = skin.isVisible();
            }

            UnderwearSetting current = UnderwearDataClientCache.get(mc.player.getUUID());
            if (current != null) {
                selectedType = current.type();
                selectedColor = current.color();
                this.previewUnderwear = current;
            }

            // モデル体型に応じて isSlim をセットする
            PlayerSkin playerSkin = mc.player.getSkin();
            PlayerSkin.Model playerModel = playerSkin.model();
            if (playerModel.id().equals("slim")) {
                this.isSlim = true;
            }
        }

        PowderRoomEditableRegions.lockNonEditablePixels(canvas);
        canvas.setIsSkin(true);

        palette = new ColorPalette(leftPos + PAL_X, topPos + PAL_Y);
        hueBar = new HueBarWidget(leftPos + PAL_X, topPos + PAL_Y + 8 * 9);
        hueBar.setH(57);
        hueBar.init();
        colorPicker = new ColorPickerWidget(leftPos + PAL_X, topPos + PAL_Y + 8 * 9 + 57);
        colorPicker.setH(57);
        colorPicker.init();

        int rgbBaseX = leftPos + 14;
        int rgbY = topPos + ED_Y + ED_H + RGB_Y_OFFSET;
        rBox = makeRgbBox(rgbBaseX, rgbY, "R");
        gBox = makeRgbBox(rgbBaseX + RGB_BOX_W + 6, rgbY, "G");
        bBox = makeRgbBox(rgbBaseX + (RGB_BOX_W * 2) + 12, rgbY, "B");
        rBox.setResponder(s -> onRgbEdited());
        gBox.setResponder(s -> onRgbEdited());
        bBox.setResponder(s -> onRgbEdited());
        addRenderableWidget(rBox);
        addRenderableWidget(gBox);
        addRenderableWidget(bBox);

        int saveX = leftPos + PV_X + PV_W - 63;
        int saveY = topPos + PV_Y + PV_H + 8;
        saveButton = Button.builder(Component.translatable("gui.tailormade.designer.save"), btn -> onSave())
                .pos(saveX, saveY)
                .size(65, 24)
                .build();
        addRenderableWidget(saveButton);
    }

    private EditBox makeRgbBox(int x, int y, String hint) {
        EditBox box = new EditBox(font, x, y, RGB_BOX_W, RGB_BOX_H, Component.literal(hint));
        box.setMaxLength(3);
        box.setValue("0");
        return box;
    }

    private void fillWithSampledSkinColor(AbstractClientPlayer player) {
        NativeImage src = readSkinPixels(player.getSkin().texture());
        if (src == null) {
            canvas.fill(0xFFC8A882);
            return;
        }
        canvas.snapshot();
        try {
            int w = Math.min(src.getWidth(), 64);
            int h = Math.min(src.getHeight(), 64);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    canvas.setPixel(x, y, abgrToArgb(src.getPixelRGBA(x, y)), 1);
                }
            }
        } finally {
            canvas.commitPendingAction();
            src.close();
        }
    }

    private static NativeImage readSkinPixels(ResourceLocation location) {
        try {
            RenderSystem.assertOnRenderThread();
            AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(location);
            NativeImage img = new NativeImage(64, 64, false);
            tex.bind();
            img.downloadTexture(0, false);
            return img;
        } catch (Exception e) {
            return null;
        }
    }

    private static int abgrToArgb(int abgr) {
        int a = (abgr >> 24) & 0xFF;
        int b = (abgr >> 16) & 0xFF;
        int g = (abgr >>  8) & 0xFF;
        int r =  abgr & 0xFF;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBg(g, partialTick, mouseX, mouseY);
        palette.render(g, mouseX, mouseY);
        hueBar.render(g, mouseX, mouseY);
        colorPicker.render(g, mouseX, mouseY);

        renderEditor(g, mouseX, mouseY);
        renderPreview(g, mouseX, mouseY);
        renderRgbLabels(g);
        renderRgbBoxes(g, mouseX, mouseY, partialTick);
        renderToolabr(g);
        renderToggleBtn(g);
        renderSegmentBar(g);

        if (canvas != null) canvas.uploadIfDirty();
        saveButton.render(g, mouseX, mouseY, partialTick);
    }

    private void renderToggleBtn(GuiGraphics g) {
        if (this.isVisible) {
            g.blit(SKIN_TOGGLE_BTN_SHOW, leftPos + TOGGLE_X, topPos + TOGGLE_Y, 0, 0, TOGGLE_W, TOGGLE_H, TOGGLE_W, TOGGLE_H);
        } else {
            g.blit(SKIN_TOGGLE_BTN_HIDE, leftPos + TOGGLE_X, topPos + TOGGLE_Y, 0, 0, TOGGLE_W, TOGGLE_H, TOGGLE_W, TOGGLE_H);
        }
    }

    private void renderToolabr(GuiGraphics g)
    {
        int toolBarX = this.leftPos + TOOLBAR_X;
        int toolBarY = this.topPos + TOOLBAR_Y;
        renderHalfSize(g, TOOL_ICON_BRUSH, toolBarX, toolBarY, 20, 20);
        renderHalfSize(g, TOOL_ICON_ERASER, toolBarX, toolBarY + 12, 20, 20);
        renderHalfSize(g, TOOL_ICON_BUCKET, toolBarX, toolBarY + 24, 20, 20);
        renderHalfSize(g, TOOL_ICON_EYEDROPPER, toolBarX, toolBarY + 36, 20, 20);

        // アクティブ枠
        int toolBarActiveWidth = 12;
        int tollBarActiveX = toolBarX - 1;
        int toolBarActiveY;
        switch (TOOL_MODE) {
            case "brush": toolBarActiveY = toolBarY - 1; break;
            case "eraser": toolBarActiveY = toolBarY + 11; break;
            case "bucket": toolBarActiveY = toolBarY + 23; break;
            case "eyedropper": toolBarActiveY = toolBarY + 35; break;
            default: toolBarActiveY = toolBarY - 1;
        }
        g.fill(tollBarActiveX, toolBarActiveY, tollBarActiveX + toolBarActiveWidth, toolBarActiveY + 1, labelBorderColor);
        g.fill(tollBarActiveX,  toolBarActiveY + toolBarActiveWidth - 1, tollBarActiveX + toolBarActiveWidth, toolBarActiveY + toolBarActiveWidth, labelBorderColor);
        g.fill(tollBarActiveX, toolBarActiveY, tollBarActiveX + 1, toolBarActiveY + toolBarActiveWidth, labelBorderColor);
        g.fill(tollBarActiveX + toolBarActiveWidth - 1, toolBarActiveY, tollBarActiveX + toolBarActiveWidth, toolBarActiveY + toolBarActiveWidth, labelBorderColor);

        // ブラシサイズ選択
        renderHalfSize(g, TOOL_ICON_SIZE_1, toolBarX, toolBarY + 60, 20, 12);
        renderHalfSize(g, TOOL_ICON_SIZE_2, toolBarX, toolBarY + 66, 20, 12);
        renderHalfSize(g, TOOL_ICON_SIZE_3, toolBarX, toolBarY + 72, 20, 12);

        // ブラシサイズ表示
        switch (BRUSH_SIZE) {
            case 1: toolBarActiveY = toolBarY + 59; break;
            case 2: toolBarActiveY = toolBarY + 65; break;
            case 3: toolBarActiveY = toolBarY + 71; break;
            default: toolBarActiveY = toolBarY + 59;
        }
        g.fill(tollBarActiveX, toolBarActiveY, tollBarActiveX + toolBarActiveWidth, toolBarActiveY + 1, labelBorderColor);
        g.fill(tollBarActiveX,  toolBarActiveY + 7, tollBarActiveX + toolBarActiveWidth, toolBarActiveY + 8, labelBorderColor);
        g.fill(tollBarActiveX, toolBarActiveY, tollBarActiveX + 1, toolBarActiveY + 8, labelBorderColor);
        g.fill(tollBarActiveX + toolBarActiveWidth - 1, toolBarActiveY, tollBarActiveX + toolBarActiveWidth, toolBarActiveY + 8, labelBorderColor);
    }

    private void renderSegmentBar(GuiGraphics g) {
        int x = this.leftPos + SEG_BTN_X;
        int y = this.topPos + SEG_BTN_Y;

        g.blit(SEG_BUTTON_BASE, x, y, 0, 0, 68, 9, 68, 9);
        if (!isSlim) {
            g.blit(SEG_BUTTON_R, x + 20, y + 1, 0, 0, 13, 7, 13, 7);
        } else {
            g.blit(SEG_BUTTON_S, x + 34, y + 1, 0, 0, 17, 7, 17, 7);
        }
        if (!isGuideVisible) {
            g.blit(SEG_BUTTON_N, x + 52, y + 1, 0, 0, 15, 7, 15, 7);
        }
    }

    private void renderHalfSize(GuiGraphics g, ResourceLocation texture, int x, int y, int w, int h) {
        g.pose().pushPose();
        g.pose().translate(x, y, 0);
        g.pose().scale(0.5F, 0.5F, 1.0f);
        g.blit(texture, 0, 0, 0, 0, w, h, w, h);
        g.pose().popPose();
    }

    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - GUI_W) / 2;
        int y = (this.height - GUI_H) / 2;
        g.fill(0, 0, this.width, this.height, 0x80000000);
        g.blit(GUI_TEXTURE, x, y, GUI_OFFSET_X, GUI_OFFSET_Y, GUI_W, GUI_H, 512, 512);
    }

    private void renderEditor(GuiGraphics g, int mouseX, int mouseY) {
        if (canvas == null) return;

        float scale = currentScale();
        int[] rxy = currentRenderXY();
        int renderW = (int)(64 * scale);
        int renderH = (int)(64 * scale);
        int renderX = rxy[0];
        int renderY = rxy[1];

        int clipX = leftPos + ED_X;
        int clipY = topPos + ED_Y;
        g.enableScissor(clipX, clipY, clipX + ED_W, clipY + ED_H);

        RenderSystem.enableBlend();
        g.blit(canvas.getTextureLocation(), renderX, renderY, 0, 0, renderW, renderH, renderW, renderH);
        RenderSystem.disableBlend();

        if (scale >= 4.0f) drawGrid(g, renderX, renderY, renderW, renderH, scale);

        int[] hoverPx = screenToPixel(mouseX, mouseY, renderX, renderY, scale);
        if (hoverPx != null) {
            int hoverPixelColor = canvas.getPixel(hoverPx[0], hoverPx[1]);
            float brightness = ColorService.getBrightness(hoverPixelColor);
            int hoverColor = brightness >= 0.5 ? 0x55000000 : 0x55ffffff;
            int hx = getRenderingX(hoverPx[0]);
            int hy = getRenderingY(hoverPx[1]);
            if (BRUSH_SIZE == 3) {
                hx -= (int)scale;
                hy -= (int)scale;
            }
            int maxHx = hx + (int)(scale * BRUSH_SIZE);
            int maxHy = hy + (int)(scale * BRUSH_SIZE);
            g.fill(hx, hy, maxHx, maxHy, hoverColor);

            if (canDrawLine()) {
                canvas.calculateLine(hoverPx[0], hoverPx[1]);
                for (int[] line: canvas.getPreviewLinePixels()) {
                    int lineHoverPixelColor = canvas.getPixel(line[0], line[1]);
                    float lineBrightness = ColorService.getBrightness(lineHoverPixelColor);
                    int lineHoverColor = lineBrightness >= 0.5 ? 0x55000000 : 0x55ffffff;
                    int lX = getRenderingX(line[0]);
                    int lY = getRenderingY(line[1]);
                    int maxLX = lX + (int)(scale * 1);
                    int maxLY = lY + (int)(scale * 1);
                    g.fill(lX, lY, maxLX, maxLY, lineHoverColor);
                }
            }
        }

        renderFaceGuidelines(g, renderX, renderY, scale);
        g.disableScissor();
    }

    private int getRenderingX(int original) {
        return currentRenderXY()[0] + (int)(original * currentScale());
    }
    private int getRenderingY(int original) {
        return currentRenderXY()[1] + (int)(original * currentScale());
    }

    private void drawGrid(GuiGraphics g, int rx, int ry, int rw, int rh, float scale) {
        int gridColor = 0x33FFFFFF;
        int gridSubColor = 0x33000000;
        for (int x = 0; x <= canvas.getWidth(); x++) {
            int lx = rx + (int)(x * scale);
            g.fill(lx, ry, lx + 1, ry + rh, gridColor);
            g.fill(lx - 1, ry, lx, ry + rh, gridSubColor);
        }
        for (int y = 0; y <= canvas.getHeight(); y++) {
            int ly = ry + (int)(y * scale);
            g.fill(rx, ly, rx + rw, ly + 1, gridColor);
            g.fill(rx, ly - 1, rx + rw, ly, gridSubColor);
        }
    }

    private void renderPreview(GuiGraphics g, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (canvas != null) {
            Map<PatternType, int[]> pixelMap = buildPreviewPixelMap();
            ResourceLocation skinTex = previewCompositor.composeForPreview(pixelMap);
            SkinLayerRenderLayer.setSkinPreview(skinTex);
        }
        SkinLayerRenderLayer.setUnderwearPreview(previewUnderwear);

        MannequinStylePreviewHelper.setHideArmor(true);
        SkinLayerRenderLayer.setIsForcePreview(true);

        float savedXRot = mc.player.getXRot();
        float savedXRotO = mc.player.xRotO;
        mc.player.setXRot(previewPitch);
        mc.player.xRotO = previewPitch;

        try {
            Quaternionf pose = new Quaternionf()
                    .rotateZ((float) Math.PI)
                    .rotateY((float) Math.toRadians(previewYaw));
            Quaternionf camera = new Quaternionf()
                    .rotateX((float) Math.toRadians(previewPitch));
            int centerX = leftPos + PV_X + PV_W / 2;
            int centerY = topPos + PV_Y + PV_H / 2 + 50;

            InventoryScreen.renderEntityInInventory(
                    g,
                    centerX,
                    centerY,
                    65,
                    new Vector3f(0, 0, 0),
                    pose,
                    null,
                    mc.player
            );
        } finally {
            mc.player.setXRot(savedXRot);
            mc.player.xRotO = savedXRotO;
            SkinLayerRenderLayer.clearPreview();
            MannequinStylePreviewHelper.setHideArmor(false);
            SkinLayerRenderLayer.setIsForcePreview(false);
        }
    }

    private Map<PatternType, int[]> buildPreviewPixelMap() {
        Map<PatternType, int[]> map = new EnumMap<>(PatternType.class);

        int[] pixels = canvas.getPixels();
        for (PatternType type : PatternType.values()) {
            map.put(type, cropPixels(pixels, type));
        }

        return map;
    }

    private void renderRgbLabels(GuiGraphics g) {
        int ly = topPos + ED_Y + ED_H + RGB_Y_OFFSET;
        g.drawString(font, "R", leftPos + 8, ly + 1, 0xFFFFFF, false);
        g.drawString(font, "G", leftPos + 8 + RGB_BOX_W + 6, ly + 1, 0xFFFFFF, false);
        g.drawString(font, "B", leftPos + 8 + (RGB_BOX_W * 2) + 12, ly + 1, 0xFFFFFF, false);
    }

    private void renderRgbBoxes(GuiGraphics g, int mx, int my, float partialTick) {
        rBox.render(g, mx, my, partialTick);
        gBox.render(g, mx, my, partialTick);
        bBox.render(g, mx, my, partialTick);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (palette.mouseClicked(mx, my)) {
            clickedArea = "palette";
            syncRgbBoxes();
            if (!palette.isEraserMode()) {
                colorPicker.setBaseColor(palette.getSelectedColor());
            }
            return true;
        }
        if (hueBar.mousePressed(mx, my)) {
            colorPicker.setBaseColor(hueBar.getSelectedBaseColor());
            return true;
        }
        if (colorPicker.mousePressed(mx, my)) {
            int picked = colorPicker.getSelectedColor();
            palette.setRgb(
                    (picked >> 16) & 0xFF,
                    (picked >>  8) & 0xFF,
                    picked & 0xFF
            );
            syncRgbBoxes();
            return true;
        }
        if (button == 1 && inEditorArea(mx, my) && zoomScale > 1.0f) {
            rightDragStartX = mx;
            rightDragStartY = my;
            return true;
        }
        if (button == 0 && inEditorArea(mx, my)) {
            canvas.snapshot();
            clickedArea = "editor";
            applyBrush(mx, my);
            return true;
        }
        if (button == 0 && inPreviewArea(mx, my)) {
            dragStartX = mx;
            dragStartY = my;
            clickedArea = "preview";
            return true;
        }
        if (inToolbar(mx, my)) {
            clickedArea = "toolbar";
            mouseClickedOnToolBar(mx, my);
            return true;
        }

        if (inBrushSizebar(mx, my)) {
            clickedArea = "brushsizebar";
            mouseClickedOnBrushSizeBar(mx, my);
            return true;
        }
        if (button == 0 && inToggleBox(mx, my)) {
            this.isVisible = !this.isVisible;
        }
        if (button == 0 && inSegmentButton(mx, my)) {
            mouseClickedOnSegBar(mx, my);
            return true;
        }

        clickedArea = null;
        return super.mouseClicked(mx, my, button);
    }

    private void mouseClickedOnToolBar(double mx, double my) {
        if (mx >= (this.leftPos + TOOLBAR_X) && mx <= (this.leftPos + TOOLBAR_X + 10) && my >= (this.topPos + TOOLBAR_Y) && my <= (this.topPos + TOOLBAR_Y + 10)) {
            setToolMode("brush");
        } else if (mx >= (this.leftPos + TOOLBAR_X) && mx <= (this.leftPos + TOOLBAR_X + 10) && my >= (this.topPos + TOOLBAR_Y + 12) && my <= (this.topPos + TOOLBAR_Y + 20)) {
            setToolMode("eraser");
        } else if (mx >= (this.leftPos + TOOLBAR_X) && mx <= (this.leftPos + TOOLBAR_X + 10) && my >= (this.topPos + TOOLBAR_Y + 24) && my <= (this.topPos + TOOLBAR_Y + 34)) {
            setToolMode("bucket");
        } else if (mx >= (this.leftPos + TOOLBAR_X) && mx <= (this.leftPos + TOOLBAR_X + 10) && my >= (this.topPos + TOOLBAR_Y + 36) && my <= (this.topPos + TOOLBAR_Y + 46)) {
            setToolMode("eyedropper");
        }
    }

    private void mouseClickedOnBrushSizeBar(double mx, double my) {
        int startY = this.topPos + TOOLBAR_Y + 60;
        if (mx >= (this.leftPos + TOOLBAR_X) && mx <= (this.leftPos + TOOLBAR_X + 10) && my >= (startY) && my <= (startY + 5)) {
            BRUSH_SIZE = 1;
        } else if (mx >= (this.leftPos + TOOLBAR_X) && mx <= (this.leftPos + TOOLBAR_X + 10) && my >= (startY + 6) && my <= (startY + 11)) {
            BRUSH_SIZE = 2;
        } else if (mx >= (this.leftPos + TOOLBAR_X) && mx <= (this.leftPos + TOOLBAR_X + 10) && my >= (startY + 12) && my <= (startY + 17)) {
            BRUSH_SIZE = 3;
        }
    }

    private void mouseClickedOnSegBar(double mx, double my) {
        int startX = this.leftPos + SEG_BTN_X + 20;
        if (mx >= startX && mx <= startX + 13) {
            isSlim = false;
        } else if (mx >= startX + 14 && mx <= startX + 31) {
            isSlim = true;
        } else if (mx >= startX + 32 && mx <= startX + 46) {
            isGuideVisible = !isGuideVisible;
        }
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (button == 0 && inEditorArea(mx, my) && clickedArea == "editor") { applyBrush(mx, my); return true; }
        if (button == 0 && dragStartX >= 0) {
            previewYaw += (float)(mx - dragStartX) * 1.0f;
            previewPitch = Math.clamp(
                    previewPitch + (float)(my - dragStartY) * 0.5f,
                    -180.0f, 180.0f
            );
            dragStartX = mx;
            dragStartY = my;
            return true;
        }
        if (button == 1 && rightDragStartX >= 0 && canvas != null) {
            panOffsetX += (float)(mx - rightDragStartX);
            panOffsetY += (float)(my - rightDragStartY);
            rightDragStartX = mx;
            rightDragStartY = my;
            return true;
        }
        if (hueBar.mouseDragged(mx, my)) {
            colorPicker.setBaseColor(hueBar.getSelectedBaseColor());
            // パレットにも反映
            int base = hueBar.getSelectedBaseColor();
            palette.setRgb((base >> 16) & 0xFF, (base >> 8) & 0xFF, base & 0xFF);
            syncRgbBoxes();
            return true;
        }
        if (colorPicker.mouseDragged(mx, my)) {
            int picked = colorPicker.getSelectedColor();
            palette.setRgb(
                    (picked >> 16) & 0xFF,
                    (picked >>  8) & 0xFF,
                    picked & 0xFF
            );
            syncRgbBoxes();
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double dx, double dy) {
        if (!inEditorArea(mx, my) || canvas == null) return super.mouseScrolled(mx, my, dx, dy);

        float oldScale = currentScale();
        float minZoom = 1.0f;
        float newZoom = Math.max(minZoom, zoomScale + (dy > 0 ? 0.25f : -0.25f));

        float maxZoom = Math.min(ED_W, ED_H) / 16.0f / fitScale();
        newZoom = Math.min(newZoom, maxZoom);

        float newScale = fitScale() * newZoom;
        float scaleDelta = newScale / oldScale;

        int[] rxy = currentRenderXY();
        panOffsetX = (float)(mx - (mx - rxy[0]) * scaleDelta - (leftPos + ED_X + (ED_W - canvas.getWidth() * newScale) / 2));
        panOffsetY = (float)(my - (my - rxy[1]) * scaleDelta - (topPos + ED_Y + (ED_H - canvas.getHeight() * newScale) / 2));

        zoomScale = newZoom;
        if (zoomScale <= 1.0f) { panOffsetX = 0; panOffsetY = 0; }

        return true;
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (button == 0) { dragStartX = -1; dragStartY = -1; }
        if (button == 1) { rightDragStartX = -1; rightDragStartY = -1; }
        if ("editor".equals(clickedArea) && canvas != null) {
            canvas.commitPendingAction();
        }
        clickedArea = null;
        hueBar.mouseReleased();
        colorPicker.mouseReleased();
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean ctrl = (modifiers & org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL) != 0;
        boolean shift = (modifiers & org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT) != 0;
        if (ctrl && keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_Z) {
            if (shift) canvas.redo(); else canvas.undo();
            hasUnsavedChanges = true;
            return true;
        }
        // 顔の向き
        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            previewYaw -= 10.0f;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            previewYaw += 10.0f;
        } else if (keyCode == GLFW.GLFW_KEY_UP) {
            previewPitch -= 10.0f;
        } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
            previewPitch += 10.0f;
        }

        // エディタ系
        if (keyCode == GLFW.GLFW_KEY_P || keyCode == GLFW.GLFW_KEY_B) {
            TOOL_MODE = "brush";
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_E) {
            TOOL_MODE = "eraser";
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_I) {
            TOOL_MODE = "eyedropper";
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_G) {
            TOOL_MODE = "bucket";
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_1) {
            BRUSH_SIZE = 1;
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_2) {
            BRUSH_SIZE = 2;
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_3) {
            BRUSH_SIZE = 3;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean canDrawLine() {
        return canvas != null && canvas.drawLine() && Screen.hasShiftDown();
    }

    private void applyBrush(double mx, double my) {
        float scale = currentScale();
        int[] rxy = currentRenderXY();
        int renderX = rxy[0];
        int renderY = rxy[1];
        int[] px = screenToPixel((int) mx, (int) my, renderX, renderY, scale);
        if (px == null) return;

        if (!PowderRoomEditableRegions.isEditable(px[0], px[1])) return;

        if (palette.isEraserMode()) {
            if (TOOL_MODE == "bucket") {
                canvas.fill(TRANSPARENT);
            } else {
                canvas.erase(px[0], px[1], BRUSH_SIZE);
            }
        } else if (TOOL_MODE == "eyedropper") {
            int color = canvas.getPixel(px[0], px[1]);
            palette.setSelectedColor(color);
            palette.syncRgbFromColor();
            hueBar.setHueFromColor(color);
            colorPicker.setSelectedColor(color);
            syncRgbBoxes();
            if (beforeEyedropperTool != null) {
                TOOL_MODE = beforeEyedropperTool;
            }
        } else {
            if (TOOL_MODE == "bucket") {
                canvas.fill(px[0], px[1], palette.getSelectedColor());
            } else {
                int color = TOOL_MODE.equals("eraser") ? TRANSPARENT : palette.getSelectedColor();
                if (canDrawLine()) {
                    canvas.commitLine(px[0], px[1], color, BRUSH_SIZE);
                } else {
                    canvas.setPixel(px[0], px[1], color, BRUSH_SIZE);
                }
                canvas.setLastPixel(px[0], px[1]);
            }
        }
        hasUnsavedChanges = true;
    }

    private void setToolMode(String mode) {
        if (canvas != null) {
            // 範囲選択じゃない場合は範囲選択用設定をクリアする
            if (!mode.equals("selection")) {
                canvas.clearSelection();
            }
            // ブラシ・消しゴム以外の場合＆ツール変更の場合は直線引く用の前座標を削除する
            if (!mode.equals("eraser") && !mode.equals("brush")) {
                canvas.clearLastPixel();
            } else if (!Objects.equals(TOOL_MODE, mode)) {
                canvas.clearLastPixel();
            }
        }
        TOOL_MODE = mode;
    }

    private boolean isHeadArea(int px, int py) {
        for (PatternType.CanvasSegment seg : PatternType.HEAD.getSegments()) {
            if (px >= seg.uvX() && px < seg.uvX() + seg.w() && py >= seg.uvY() && py < seg.uvY() + seg.h()) {
                return true;
            }
        }
        return false;
    }

    private void onSave() {
        if (canvas == null) return;
        NetworkManager.sendToServer(new SaveSkinLayerPayload(canvas.getPixels(), this.isVisible));
        hasUnsavedChanges = false;
        onClose();
    }

    private float fitScale() {
        if (canvas == null) return 1.0f;
        return Math.min((float) ED_W / canvas.getWidth(), (float) ED_H / canvas.getHeight());
    }

    private float currentScale() {
        return fitScale() * zoomScale;
    }

    private int[] currentRenderXY() {
        float scale = currentScale();
        int renderW = (int)(canvas.getWidth()  * scale);
        int renderH = (int)(canvas.getHeight() * scale);
        int baseX = leftPos + ED_X + (ED_W - renderW) / 2;
        int baseY = topPos + ED_Y + (ED_H - renderH) / 2;
        return new int[]{
                (int)(baseX + panOffsetX),
                (int)(baseY + panOffsetY)
        };
    }

    private boolean inEditorArea(double mx, double my) {
        return mx >= leftPos + ED_X && mx < leftPos + ED_X + ED_W && my >= topPos + ED_Y && my < topPos + ED_Y + ED_H;
    }
    private boolean inPreviewArea(double mx, double my) {
        return mx >= leftPos + PV_X && mx < leftPos + PV_X + PV_W && my >= topPos + PV_Y && my < topPos + PV_Y + PV_H;
    }
    private boolean inToolbar(double mx, double my) {
        return inBox(mx, my, leftPos + TOOLBAR_X, topPos + TOOLBAR_Y, 16, 46);
    }
    private boolean inBrushSizebar(double mx, double my) {
        return inBox(mx, my, leftPos + TOOLBAR_X, topPos + TOOLBAR_Y + 60, 16, 18);
    }
    private boolean inSegmentButton(double mx, double my) {
        return inBox(mx, my, leftPos + SEG_BTN_X, topPos + SEG_BTN_Y, 68, 9);
    }
    private boolean inToggleBox(double mx, double my) {
        return inBox(mx, my, leftPos + TOGGLE_X, topPos + TOGGLE_Y, TOGGLE_W, TOGGLE_H);
    }
    private boolean inBox(double mx, double my, int bx, int by, int w, int h) {
        return mx >= bx && mx < bx + w && my >= by && my < by + h;
    }

    private int[] screenToPixel(int mx, int my, int rx, int ry, float scale) {
        int px = (int)((mx - rx) / scale);
        int py = (int)((my - ry) / scale);
        if (px < 0 || px >= 64 || py < 0 || py >= 64) return null;
        return new int[]{px, py};
    }
    private int[] cropPixels(int[] full64x64, PatternType type) {
        int canvasW = type.getCanvasW();
        int canvasH = type.getCanvasH();
        int[] canvas = new int[canvasW * canvasH];

        for (PatternType.CanvasSegment seg : type.getSegments()) {
            for (int y = 0; y < seg.h(); y++) {
                for (int x = 0; x < seg.w(); x++) {
                    int srcIdx = (seg.uvY() + y) * 64 + (seg.uvX() + x);
                    int dstIdx = (seg.canvasY() + y) * type.getCanvasW() + (seg.canvasX() + x);
                    if (srcIdx < full64x64.length && dstIdx < canvas.length) {
                        canvas[dstIdx] = full64x64[srcIdx];
                    }
                }
            }
        }
        return canvas;
    }
    private void onRgbEdited() {
        try {
            palette.setRgb(
                    Integer.parseInt(rBox.getValue()),
                    Integer.parseInt(gBox.getValue()),
                    Integer.parseInt(bBox.getValue()));
        } catch (NumberFormatException ignored) {}
    }

    private void syncRgbBoxes() {
        if (palette.isEraserMode()) return;
        int r = palette.getRValue();
        int g = palette.getGValue();
        int b = palette.getBValue();
        rBox.setValue(String.valueOf(r));
        gBox.setValue(String.valueOf(g));
        bBox.setValue(String.valueOf(b));
    }

    @Override
    public void removed() {
        if (canvas != null) canvas.close();
        if (previewCompositor != null) previewCompositor.close();
        if (hueBar != null) hueBar.close();
        if (colorPicker != null) colorPicker.close();
        super.removed();
    }

    @Override
    public boolean isPauseScreen() { return false; }

    private void renderFaceGuidelines(GuiGraphics g, int renderX, int renderY, float scale) {
        if (canvas == null) return;
        if (!this.isGuideVisible) return;
        List<FaceRegion> regions = this.isSlim ? getSlimRegions() : getFaceRegions();

        for (FaceRegion r : regions) {
            int sx = renderX + (int)(r.cx * scale);
            int sy = renderY + (int)(r.cy * scale);
            int sw = (int)(r.cw * scale);
            int sh = (int)(r.ch * scale);

            // 枠線（4辺）
            g.fill(sx,      sy,      sx + sw, sy + 1,      FACE_LINE_COLOR);
            g.fill(sx,      sy + sh, sx + sw, sy + sh + 1, FACE_LINE_COLOR);
            g.fill(sx,      sy,      sx + 1,  sy + sh,     FACE_LINE_COLOR);
            g.fill(sx + sw, sy,      sx + sw + 1, sy + sh, FACE_LINE_COLOR);

            if (sw >= 16 && sh >= 8) {
                g.drawString(font, r.label, sx + 2, sy + 2, FACE_LABEL_COLOR, false);
            }
        }
    }

    private record FaceRegion(int cx, int cy, int cw, int ch, String label) {}

    private List<FaceRegion> getFaceRegions() {
        return List.of(
                    // 胴体スキン
                    new FaceRegion(20,  16,  8,  4, "Body Top"),
                    new FaceRegion(28,  16,  8,  4, "Body Bot"),
                    new FaceRegion(16,  20,  4, 12, "Body R"),
                    new FaceRegion(20,  20,  8, 12, "Body Front"),
                    new FaceRegion(28,  20,  4, 12, "Body L"),
                    new FaceRegion(32,  20,  8, 12, "Body Back"),
                    // 右腕スキン
                    new FaceRegion(44,  16,  4,  4, "R Arm Top"),
                    new FaceRegion(48,  16,  4,  4, "R Arm Bot"),
                    new FaceRegion(40,  20,  4, 12, "R Arm R"),
                    new FaceRegion(44,  20,  4, 12, "R Arm Front"),
                    new FaceRegion(48,  20,  4, 12, "R Arm L"),
                    new FaceRegion(52,  20,  4, 12, "R Arm Back"),
                    // 左腕スキン
                    new FaceRegion(36,  48,  4,  4, "L Arm Top"),
                    new FaceRegion(40,  48,  4,  4, "L Arm Bot"),
                    new FaceRegion(32,  52,  4, 12, "L Arm R"),
                    new FaceRegion(36,  52,  4, 12, "L Arm Front"),
                    new FaceRegion(40,  52,  4, 12, "L Arm L"),
                    new FaceRegion(44,  52,  4, 12, "L Arm Back"),
                    // 右足スキン
                    new FaceRegion( 4,  16,  4,  4, "R Leg Top"),
                    new FaceRegion( 8,  16,  4,  4, "R Leg Bot"),
                    new FaceRegion( 0,  20,  4, 12, "R Leg R"),
                    new FaceRegion( 4,  20,  4, 12, "R Leg Front"),
                    new FaceRegion( 8,  20,  4, 12, "R Leg L"),
                    new FaceRegion(12,  20,  4, 12, "R Leg Back"),
                    // 左足スキン
                    new FaceRegion(20,  48,  4,  4, "L Leg Top"),
                    new FaceRegion(24,  48,  4,  4, "L Leg Bot"),
                    new FaceRegion(16,  52,  4, 12, "L Leg R"),
                    new FaceRegion(20,  52,  4, 12, "L Leg Front"),
                    new FaceRegion(24,  52,  4, 12, "L Leg L"),
                    new FaceRegion(28,  52,  4, 12, "L Leg Back")
        );
    }
    private List<FaceRegion> getSlimRegions() {
        return List.of(
                // 胴体スキン
                new FaceRegion(20,  16,  8,  4, "Body Top"),
                new FaceRegion(28,  16,  8,  4, "Body Bot"),
                new FaceRegion(16,  20,  4, 12, "Body R"),
                new FaceRegion(20,  20,  8, 12, "Body Front"),
                new FaceRegion(28,  20,  4, 12, "Body L"),
                new FaceRegion(32,  20,  8, 12, "Body Back"),
                // 右腕スキン
                new FaceRegion(44,  16,  3,  4, "R Arm Top"),
                new FaceRegion(47,  16,  3,  4, "R Arm Bot"),
                new FaceRegion(40,  20,  4, 12, "R Arm R"),
                new FaceRegion(44,  20,  3, 12, "R Arm Front"),
                new FaceRegion(47,  20,  4, 12, "R Arm L"),
                new FaceRegion(51,  20,  3, 12, "R Arm Back"),
                // 左腕スキン
                new FaceRegion(36,  48,  3,  4, "L Arm Top"),
                new FaceRegion(39,  48,  3,  4, "L Arm Bot"),
                new FaceRegion(32,  52,  4, 12, "L Arm R"),
                new FaceRegion(36,  52,  3, 12, "L Arm Front"),
                new FaceRegion(39,  52,  4, 12, "L Arm L"),
                new FaceRegion(43,  52,  3, 12, "L Arm Back"),
                // 右足スキン
                new FaceRegion( 4,  16,  4,  4, "R Leg Top"),
                new FaceRegion( 8,  16,  4,  4, "R Leg Bot"),
                new FaceRegion( 0,  20,  4, 12, "R Leg R"),
                new FaceRegion( 4,  20,  4, 12, "R Leg Front"),
                new FaceRegion( 8,  20,  4, 12, "R Leg L"),
                new FaceRegion(12,  20,  4, 12, "R Leg Back"),
                // 左足スキン
                new FaceRegion(20,  48,  4,  4, "L Leg Top"),
                new FaceRegion(24,  48,  4,  4, "L Leg Bot"),
                new FaceRegion(16,  52,  4, 12, "L Leg R"),
                new FaceRegion(20,  52,  4, 12, "L Leg Front"),
                new FaceRegion(24,  52,  4, 12, "L Leg L"),
                new FaceRegion(28,  52,  4, 12, "L Leg Back")
        );
    }
}
