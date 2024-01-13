package llc.redstone.hysentials.cosmetics.hats.blackcat;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;

public class BlackCatModel extends ModelBase {
    private final ModelRenderer group2;
    private final ModelRenderer group3;
    private final ModelRenderer group;
    private final ModelRenderer bone;
    private final ModelRenderer bb_main;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;

    private RenderPlayer renderPlayer;


    public void init (RenderPlayer renderPlayer) {
        this.renderPlayer = renderPlayer;
    }
    public BlackCatModel() {
        textureWidth = 64;
        textureHeight = 64;

        group2 = new ModelRenderer(this);
        group2.setRotationPoint(-6.0F, 23.0F, 1.0F);


        group3 = new ModelRenderer(this);
        group3.setRotationPoint(-10.8164F, 23.0F, 15.2409F);


        group = new ModelRenderer(this);
        group.setRotationPoint(0.0F, 24.0F, 0.0F);


        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 24.0F, 0.0F);


        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -2.0F, -3.0F, -6.0F, 4, 3, 12, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 5, 5, -2.0F, -5.5F, -5.5F, 1, 1, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 1, 4, 1.0F, -5.5F, -5.5F, 1, 1, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 0, -1.5F, -3.0F, -11.0F, 3, 2, 1, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 15, -2.5F, -5.0F, -10.0F, 5, 4, 5, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 32, -2.0F, -6.0F, -9.5F, 4, 1, 4, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 37, -1.0F, -8.0F, -8.5F, 2, 2, 2, 0.0F, false));
        bb_main.cubeList.add(new ModelBox(bb_main, 0, 41, -0.5F, -9.0F, -8.0F, 1, 1, 1, 0.0F, false));

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -3.0F, 8.0F);
        bb_main.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.3927F, 0.0F, 0.0F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 24, 23, -0.5F, 0.5F, -2.0F, 1, 1, 6, 0.0F, false));

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-2.8164F, -1.0F, 7.2409F);
        bb_main.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.4363F, -0.3927F, 0.0F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 14, 18, 0.0F, 0.0F, -3.0F, 2, 2, 6, 0.0F, false));

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(2.8164F, -1.0F, 7.2409F);
        bb_main.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.4363F, 0.3927F, 0.0F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 20, 0, -2.0F, 0.0F, -3.0F, 2, 2, 6, 0.0F, false));

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(2.0F, -1.0F, -7.0F);
        bb_main.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.4363F, -0.3927F, 0.0F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 24, -1.0F, 0.0F, -3.0F, 2, 2, 6, 0.0F, false));

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(-2.0F, -1.0F, -7.0F);
        bb_main.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.4363F, 0.3927F, 0.0F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 24, 15, -1.0F, 0.0F, -3.0F, 2, 2, 6, 0.0F, false));
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        group2.render(f5);
        group3.render(f5);
        group.render(f5);
        bone.render(f5);
        bb_main.render(f5);
    }


    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        copyModelAngles(group2, renderPlayer.getMainModel().bipedHead);
        copyModelAngles(group3, renderPlayer.getMainModel().bipedHead);
        copyModelAngles(group, renderPlayer.getMainModel().bipedHead);
        copyModelAngles(bone, renderPlayer.getMainModel().bipedHead);
        copyModelAngles(bb_main, renderPlayer.getMainModel().bipedHead);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;


    }
}
