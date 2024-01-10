package cc.woverflow.hysentials.cosmetics.backpack;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CatPackModel extends ModelBase {
    private final ModelRenderer bb_main;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;

    public CatPackModel() {
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

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-0.5F, -3.0F, 7.0F);
        bb_main.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.3927F, 0.3927F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 56, 2.5F, -1.5F, -3.0F, 2, 2, 6, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-0.5F, -3.0F, 7.0F);
        bb_main.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.3927F, -0.3927F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 16, 56, -2.5F, -1.5F, -3.0F, 2, 2, 6, 0.0F, false));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(0.5F, -5.5F, 3.5F);
        bb_main.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 3.1416F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 43, -2.0F, -1.5F, -1.5F, 4, 3, 3, 0.0F, false));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(1.5F, -8.5F, 6.0F);
        bb_main.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 3.1416F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 15, 36, -0.5F, -0.5F, -1.0F, 1, 1, 2, 0.0F, false));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(-0.5F, -8.5F, 6.0F);
        bb_main.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 3.1416F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 21, 36, -0.5F, -0.5F, -1.0F, 1, 1, 2, 0.0F, false));

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(0.5F, -5.0F, 10.5F);
        bb_main.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 3.1416F, 0.0F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 20, 40, -1.5F, -1.0F, -0.5F, 3, 2, 1, 0.0F, false));

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(0.0F, -6.0F, 8.0F);
        bb_main.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 3.1416F, 0.0F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 34, -3.0F, -2.0F, -2.0F, 5, 4, 5, 0.0F, false));
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
