package cc.woverflow.hysentials.pets.cubit;

import cc.woverflow.hysentials.util.BUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class CubitModel extends ModelBase {
    private final ModelRenderer head;
    private final ModelRenderer bill;
    private final ModelRenderer chin;
    private final ModelRenderer body;
    private final ModelRenderer cubit;
    private final ModelRenderer left_wing;
    private final ModelRenderer right_wing;
    private final ModelRenderer cubit2;
    private final ModelRenderer left_leg;
    private final ModelRenderer right_leg;

    public CubitModel() {
        textureWidth = 32;
        textureHeight = 32;

        head = new ModelRenderer(this);
        head.setRotationPoint(0.0F, 15.0F, -4.0F);


        bill = new ModelRenderer(this);
        bill.setRotationPoint(0.0F, 15.0F, -4.0F);


        chin = new ModelRenderer(this);
        chin.setRotationPoint(0.0F, 15.0F, -4.0F);


        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 16.0F, 0.0F);
        setRotationAngle(body, 1.5708F, 0.0F, 0.0F);


        cubit = new ModelRenderer(this);
        cubit.setRotationPoint(0.0F, 0.0F, 0.0F);
        body.addChild(cubit);
        cubit.cubeList.add(new ModelBox(cubit, 0, 0, -3.0F, -3.0F, -4.0F, 7, 7, 7, 0.0F, false));
        cubit.cubeList.add(new ModelBox(cubit, 0, 0, 0.0F, 0.0F, 3.0F, 1, 1, 2, 0.0F, false));
        cubit.cubeList.add(new ModelBox(cubit, 0, 21, -1.0F, -1.0F, 5.0F, 3, 3, 3, 0.0F, false));

        left_wing = new ModelRenderer(this);
        left_wing.setRotationPoint(4.0F, 16.0F, -3.0F);
        setRotationAngle(left_wing, 1.5708F, 0.0F, 0.0F);
        left_wing.cubeList.add(new ModelBox(left_wing, 12, 14, 0.0F, 2.0F, -3.0F, 1, 2, 5, 0.0F, false));
        left_wing.cubeList.add(new ModelBox(left_wing, 21, 0, 0.0F, 1.0F, -4.0F, 1, 4, 1, 0.0F, false));
        left_wing.cubeList.add(new ModelBox(left_wing, 21, 5, 0.0F, 1.0F, -5.0F, 1, 1, 1, 0.0F, false));
        left_wing.cubeList.add(new ModelBox(left_wing, 0, 5, 0.0F, 4.0F, -5.0F, 1, 1, 1, 0.0F, false));

        right_wing = new ModelRenderer(this);
        right_wing.setRotationPoint(-4.0F, 13.0F, 0.0F);


        cubit2 = new ModelRenderer(this);
        cubit2.setRotationPoint(8.0F, 16.0F, 0.0F);
        cubit2.cubeList.add(new ModelBox(cubit2, 0, 14, -12.0F, -2.0F, -4.0F, 1, 4, 1, 0.0F, false));
        cubit2.cubeList.add(new ModelBox(cubit2, 3, 4, -12.0F, 1.0F, -5.0F, 1, 1, 1, 0.0F, false));
        cubit2.cubeList.add(new ModelBox(cubit2, 0, 3, -12.0F, -2.0F, -5.0F, 1, 1, 1, 0.0F, false));
        cubit2.cubeList.add(new ModelBox(cubit2, 0, 14, -12.0F, -1.0F, -3.0F, 1, 2, 5, 0.0F, false));

        left_leg = new ModelRenderer(this);
        left_leg.setRotationPoint(1.0F, 19.0F, -2.0F);
        setRotationAngle(left_leg, 1.5708F, 0.0F, 0.0F);
        left_leg.cubeList.add(new ModelBox(left_leg, 7, 14, 0.0F, 1.0F, -4.0F, 2, 2, 3, 0.0F, false));

        right_leg = new ModelRenderer(this);
        right_leg.setRotationPoint(-2.0F, 19.0F, -3.0F);
        setRotationAngle(right_leg, 1.5708F, 0.0F, 0.0F);
        right_leg.cubeList.add(new ModelBox(right_leg, 19, 14, 0.0F, 2.0F, -4.0F, 2, 2, 3, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        head.render(f5);
        bill.render(f5);
        chin.render(f5);
        body.render(f5);
        left_wing.render(f5);
        right_wing.render(f5);
        cubit2.render(f5);
        left_leg.render(f5);
        right_leg.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    //Add an animation of the left wing to rotate 360 degrees on the Y axis in 2 seconds
    float wingRotation = 0.0F;
    boolean animationStarted = false;
    int animationDelayTicks = 20;

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        EntityCubit cubit = (EntityCubit) entitylivingbaseIn;
        if (!animationStarted) {
            animationDelayTicks--;

            if (animationDelayTicks <= 0) {
                animationStarted = true;
            }
        }
        if (animationStarted) {
            wingRotation += 0.015F;
            if (wingRotation >= Math.PI * 4F) {
                wingRotation = 0.0F;
                animationDelayTicks = BUtils.randomInt(4000, 12000);
                animationStarted = false;
            }
        }
        setRotationAngle(cubit2, wingRotation, 0.0F, 0.0F);
        float legRotation = (MathHelper.cos((float) (limbSwing * 0.6662F)) * limbSwingAmount * 1.4F) / 2;
        setRotationAngle(left_leg, legRotation + 1.5708F, 0.0F, 0.0F);
        setRotationAngle(right_leg, -legRotation + 1.5708F, 0.0F, 0.0F);

    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3,
                                  float par4, float par5, float par6, Entity par7Entity) {

        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);

        body.rotateAngleX = par5 / (180F / (float) Math.PI) + 1.5708F;
        body.rotateAngleY = par4 / (180F / (float) Math.PI);
    }
}
