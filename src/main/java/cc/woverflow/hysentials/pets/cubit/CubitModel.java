package cc.woverflow.hysentials.pets.cubit;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class CubitModel extends ModelBase
{
    //fields
    ModelRenderer cube;
    ModelRenderer cube1;
    ModelRenderer cube2;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape9;
    ModelRenderer Shape10;

    public CubitModel()
    {
        textureWidth = 32;
        textureHeight = 32;

        cube = new ModelRenderer(this, 0, 0);
        cube.addBox(0F, 0F, 0F, 7, 7, 7);
        cube.setRotationPoint(-4F, 4F, -5F);
        cube.setTextureSize(32, 32);
        cube.mirror = true;
        setRotation(cube, 1.570796F, 0F, 0F);
        cube1 = new ModelRenderer(this, 0, 0);
        cube1.addBox(0F, 0F, 0F, 1, 1, 2);
        cube1.setRotationPoint(-1F, -3F, -2F);
        cube1.setTextureSize(32, 32);
        cube1.mirror = true;
        setRotation(cube1, 1.570796F, 0F, 0F);
        cube2 = new ModelRenderer(this, 0, 21);
        cube2.addBox(0F, 0F, 0F, 3, 3, 3);
        cube2.setRotationPoint(-2F, -8F, -3F);
        cube2.setTextureSize(32, 32);
        cube2.mirror = true;
        setRotation(cube2, 0F, 0F, 0F);
        Shape5 = new ModelRenderer(this, 0, 14);
        Shape5.addBox(0F, 0F, 0F, 1, 2, 5);
        Shape5.setRotationPoint(3F, 4F, -2F);
        Shape5.setTextureSize(32, 32);
        Shape5.mirror = true;
        setRotation(Shape5, 1.570796F, 0F, 0F);
        Shape6 = new ModelRenderer(this, 21, 0);
        Shape6.addBox(0F, 0F, 0F, 1, 4, 1);
        Shape6.setRotationPoint(3F, 5F, -3F);
        Shape6.setTextureSize(32, 32);
        Shape6.mirror = true;
        setRotation(Shape6, 1.570796F, 0F, 0F);
        Shape7 = new ModelRenderer(this, 21, 5);
        Shape7.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape7.setRotationPoint(3F, 6F, -3F);
        Shape7.setTextureSize(32, 32);
        Shape7.mirror = true;
        setRotation(Shape7, 1.570796F, 0F, 0F);
        Shape8 = new ModelRenderer(this, 0, 5);
        Shape8.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape8.setRotationPoint(3F, 6F, 0F);
        Shape8.setTextureSize(32, 32);
        Shape8.mirror = true;
        setRotation(Shape8, 1.570796F, 0F, 0F);
        Shape1 = new ModelRenderer(this, 12, 14);
        Shape1.addBox(0F, 0F, 0F, 1, 2, 5);
        Shape1.setRotationPoint(-5F, -1F, -6F);
        Shape1.setTextureSize(32, 32);
        Shape1.mirror = true;
        setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new ModelRenderer(this, 21, 0);
        Shape2.addBox(0F, 0F, 0F, 1, 4, 1);
        Shape2.setRotationPoint(-5F, -2F, -7F);
        Shape2.setTextureSize(32, 32);
        Shape2.mirror = true;
        setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new ModelRenderer(this, 21, 5);
        Shape3.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape3.setRotationPoint(-5F, -2F, -8F);
        Shape3.setTextureSize(32, 32);
        Shape3.mirror = true;
        setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new ModelRenderer(this, 0, 5);
        Shape4.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape4.setRotationPoint(-5F, 1F, -8F);
        Shape4.setTextureSize(32, 32);
        Shape4.mirror = true;
        setRotation(Shape4, 0F, 0F, 0F);
        Shape9 = new ModelRenderer(this, 7, 14);
        Shape9.addBox(0F, 0F, 0F, 2, 2, 3);
        Shape9.setRotationPoint(-3F, 7F, -3F);
        Shape9.setTextureSize(32, 32);
        Shape9.mirror = true;
        setRotation(Shape9, 1.570796F, 0F, 0F);
        Shape10 = new ModelRenderer(this, 19, 14);
        Shape10.addBox(0F, 0F, 0F, 2, 2, 3);
        Shape10.setRotationPoint(0F, 7F, -3F);
        Shape10.setTextureSize(32, 32);
        Shape10.mirror = true;
        setRotation(Shape10, 1.570796F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 2);
        GlStateManager.translate(0, 0.3, 0);
        cube.render(f5);
        cube1.render(f5);
        cube2.render(f5);
        Shape5.render(f5);
        Shape6.render(f5);
        Shape7.render(f5);
        Shape8.render(f5);
        Shape1.render(f5);
        Shape2.render(f5);
        Shape3.render(f5);
        Shape4.render(f5);
        Shape9.render(f5);
        Shape10.render(f5);

        GlStateManager.popMatrix();
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
        Shape9.setRotationPoint(-2.4F, 19F, 5.0F);
        Shape9.rotateAngleX = ((float) Math.PI * 3F / 2F);
        Shape10.setRotationPoint(0.4F, 19F, 5.0F);
        Shape10.rotateAngleX = ((float) Math.PI * 3F / 2F);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
