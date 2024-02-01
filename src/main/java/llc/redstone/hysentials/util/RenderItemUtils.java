package llc.redstone.hysentials.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.GuiScreenEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class RenderItemUtils {
    public static HashMap<UUID, Consumer<GuiScreenEvent.DrawScreenEvent.Post>> renderMap = new HashMap<>();
}
