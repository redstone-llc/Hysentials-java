package cc.woverflow.hysentials.cosmetics.miya;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class MiyaModel extends ModelBase {
    private final ModelRenderer group2;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer group3;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer group;
    private final ModelRenderer bone;
    private final ModelRenderer cube_r5;

    public MiyaModel() {
        textureWidth = 64;
        textureHeight = 64;

        group2 = new ModelRenderer(this);
        group2.setRotationPoint(-6.0F, 23.0F, 1.0F);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(8.0F, -4.0F, -8.0F);
        group2.addChild(cube_r1);
        setRotationAngle(cube_r1, 1.5708F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 24, -1.9F, 1.0F, -5.0F, 2, 2, 6, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(4.0F, -4.0F, -8.0F);
        group2.addChild(cube_r2);
        setRotationAngle(cube_r2, 1.5708F, 0.0F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 24, 15, -0.1F, 1.0F, -5.0F, 2, 2, 6, 0.0F, false));

        group3 = new ModelRenderer(this);
        group3.setRotationPoint(-10.8164F, 23.0F, 15.2409F);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(8.8164F, -4.0F, -9.2409F);
        group3.addChild(cube_r3);
        setRotationAngle(cube_r3, -1.5708F, 0.0F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 14, 18, -0.1F, 0.0F, -1.0F, 2, 2, 6, 0.0F, false));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(11.8164F, -1.0F, -10.2409F);
        group3.addChild(cube_r4);
        setRotationAngle(cube_r4, -1.5708F, 0.0F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 20, 0, -0.9F, -1.0F, -4.0F, 2, 2, 6, 0.0F, false));

        group = new ModelRenderer(this);
        group.setRotationPoint(0.0F, 16.0F, -7.5F);
        group.cubeList.add(new ModelBox(group, 4, 4, -2.0F, -2.0F, 0.5F, 1, 1, 2, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 0, 3, 1.0F, -2.0F, 0.5F, 1, 1, 2, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 0, 0, -1.5F, 1.0F, -3.5F, 3, 2, 1, 0.0F, false));
        group.cubeList.add(new ModelBox(group, 0, 15, -2.5F, -1.0F, -2.5F, 5, 4, 5, 0.0F, false));

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);
        bone.cubeList.add(new ModelBox(bone, 0, 0, -2.0F, -7.0F, -6.0F, 4, 3, 12, 0.0F, false));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(0.0F, -7.0F, 8.0F);
        bone.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.3927F, 0.0F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 24, 23, -0.5F, 0.5F, -2.0F, 1, 1, 6, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        group2.render(f5);
        group3.render(f5);
        group.render(f5);
        bone.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
        EntityMiya entityhamster = (EntityMiya) par1EntityLiving;

        cube_r1.rotateAngleX = 1.5708F + MathHelper.cos(par2 * 0.6662F) * 1.4F * par3;
        cube_r2.rotateAngleX = 1.5708F + MathHelper.cos(par2 * 0.6662F + (float) Math.PI) * 1.4F * par3;
        cube_r3.rotateAngleX = -1.5708F + MathHelper.cos(par2 * 0.6662F + (float) Math.PI) * 1.4F * par3;
        cube_r4.rotateAngleX = -1.5708F + MathHelper.cos(par2 * 0.6662F) * 1.4F * par3;

    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3,
                                  float par4, float par5, float par6, Entity par7Entity) {

        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);

        group.rotateAngleX = par5 / (180F / (float) Math.PI);
//        group.rotateAngleY = par4 / (180F / (float) Math.PI);
    }
}
