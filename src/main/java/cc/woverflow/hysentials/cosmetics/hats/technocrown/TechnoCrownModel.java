package cc.woverflow.hysentials.cosmetics.hats.technocrown;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TechnoCrownModel extends ModelBase {
    private final ModelRenderer base;
    private final ModelRenderer cube_r1;
    private final ModelRenderer top;

    public TechnoCrownModel() {
        textureWidth = 32;
        textureHeight = 32;

        base = new ModelRenderer(this);
        base.setRotationPoint(0.0F, 24.0F, 0.0F);
        base.cubeList.add(new ModelBox(base, 11, 13, 4.0F, -2.0F, -4.0F, 1, 2, 9, 0.0F, false));
        base.cubeList.add(new ModelBox(base, 0, 11, -5.0F, -2.0F, -5.0F, 1, 2, 9, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(4.5F, -0.5F, 0.5F);
        base.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, -1.5708F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, 3.5F, -1.5F, 0.5F, 1, 2, 9, 0.0F, false));
        cube_r1.cubeList.add(new ModelBox(cube_r1, 11, 2, -5.5F, -1.5F, -0.5F, 1, 2, 9, 0.0F, false));

        top = new ModelRenderer(this);
        top.setRotationPoint(0.0F, 24.0F, 0.0F);
        top.cubeList.add(new ModelBox(top, 11, 6, -5.0F, -3.0F, -5.0F, 2, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 11, 4, 3.0F, -3.0F, -5.0F, 2, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 19, 0, -2.0F, -3.0F, -5.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 0, 18, -5.0F, -3.0F, -4.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 11, 17, -5.0F, -3.0F, -2.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 3, 17, -5.0F, -3.0F, 1.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 16, 7, -5.0F, -3.0F, 3.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 0, 16, 4.0F, -3.0F, -4.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 16, 1, 4.0F, -3.0F, -2.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 16, 3, 4.0F, -3.0F, 1.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 16, 5, 4.0F, -3.0F, 3.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 14, 16, -5.0F, -3.0F, -4.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 14, 18, 1.0F, -3.0F, -5.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 11, 0, -5.0F, -3.0F, 4.0F, 2, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 3, 15, -2.0F, -3.0F, 4.0F, 1, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 11, 2, 3.0F, -3.0F, 4.0F, 2, 1, 1, 0.0F, false));
        top.cubeList.add(new ModelBox(top, 11, 15, 1.0F, -3.0F, 4.0F, 1, 1, 1, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        base.render(f5);
        top.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
