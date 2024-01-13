package llc.redstone.hysentials.cosmetics.backpack;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BackpackModel extends ModelBase {
    private final ModelRenderer bb_main;

    public BackpackModel() {
        textureWidth = 64;
        textureHeight = 64;

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -10.0F, -1.7F, 8, 10, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 19, 4.0F, -4.0F, -1.7F, 1, 4, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 14, 14, -5.0F, -4.0F, -1.7F, 1, 4, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 14, -3.0F, -4.0F, 2.3F, 6, 4, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 18, 22, -2.0F, -11.0F, -0.7F, 4, 1, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 2, 2.0F, -12.0F, -1.7F, 2, 2, 0, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -4.0F, -12.0F, -1.7F, 2, 2, 0, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 2, 19, 2.0F, -12.0F, -6.0F, 2, 0, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 16, 14, -4.0F, -12.0F, -6.0F, 2, 0, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 14, 22, -4.0F, -12.0F, -6.0F, 2, 12, 0, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 10, 19, 2.0F, -12.0F, -6.0F, 2, 12, 0, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 16, 0, -4.0F, 0.0F, -6.0F, 2, 0, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 10, 14, 2.0F, 0.0F, -6.0F, 2, 0, 4, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bb_main.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
