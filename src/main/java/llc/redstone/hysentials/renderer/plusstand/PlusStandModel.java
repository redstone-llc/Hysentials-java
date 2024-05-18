package llc.redstone.hysentials.renderer.plusstand;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class PlusStandModel extends ModelBase {
	private final ModelRenderer bone3;
	private final ModelRenderer bone2;
	private final ModelRenderer bone;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer bone4;

	public PlusStandModel() {
		textureWidth = 512;
		textureHeight = 512;

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 156, 140, 33.0F, -61.0F, -23.0F, 6, 61, 6, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 108, 140, 33.0F, -72.0F, 16.0F, 6, 72, 6, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 0, 164, -39.0F, -61.0F, -23.0F, 6, 61, 6, 0.0F, false));
		bone2.cubeList.add(new ModelBox(bone2, 132, 140, -39.0F, -72.0F, 16.0F, 6, 72, 6, 0.0F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, -30.3F, -28.65F);
		

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.2758F, 0.0F, 0.0F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 128, -45.0F, -5.7F, 1.45F, 90, 12, 0, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, -17.4087F, 23.381F);
		bone.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.2758F, 0.0F, 0.0F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 0, -45.0F, 4.7087F, -25.731F, 90, 0, 54, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(0.0F, -14.7F, 51.85F);
		bone.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.2758F, 0.0F, 0.0F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 116, -45.0F, -5.65F, 1.6F, 90, 12, 0, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(-45.0F, -13.7F, 28.65F);
		bone.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.2758F, 0.0F, 0.0F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 86, 90.0F, -0.3F, -29.8F, 0, 12, 54, 0.0F, false));
		cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 98, 0.0F, -0.3F, -29.8F, 0, 12, 54, 0.0F, false));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(0.0F, 24.0F, 0.0F);
		
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone3.render(f5);
		bone2.render(f5);
		bone.render(f5);
		bone4.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
