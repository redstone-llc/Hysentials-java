package llc.redstone.hysentials.cosmetics.kzero;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class HairModel extends ModelBase {
    private final ModelRenderer bone2;
    private final ModelRenderer cube_r1;

    public HairModel() {
        textureWidth = 128;
        textureHeight = 128;

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(0.0F, -5.0F, 8.0F);
        bone2.cubeList.add(new ModelBox(bone2, 0, 64, -1.0F, -1.0F, -4.0F, 2, 3, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 0, 76, -3.0F, -1.0F, -2.0F, 1, 4, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 8, 76, 2.0F, -1.0F, -1.0F, 1, 4, 1, 0.0F, false));
        bone2.cubeList.add(new ModelBox(bone2, 4, 76, 0.0F, 0.0F, 0.0F, 1, 4, 1, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone2.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.5236F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 68, -2.0F, -1.0F, -3.0F, 4, 4, 4, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        bone2.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
