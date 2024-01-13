package llc.redstone.hysentials.cosmetics.kzero;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class RobeModel extends ModelBase {
	private final ModelRenderer bone3;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer cube_r5;
	private final ModelRenderer cube_r6;
	private final ModelRenderer cube_r7;
	private final ModelRenderer cube_r8;
	private final ModelRenderer cube_r9;
	private final ModelRenderer cube_r10;
	private final ModelRenderer cube_r11;

	public RobeModel() {
		textureWidth = 128;
		textureHeight = 128;

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(-6.0F, 16.5F, 0.0F);


		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(1.9321F, -0.6831F, 3.6603F);
		bone3.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.0391F, 0.9031F, 0.4569F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 117, 0.0F, -4.5F, -1.0F, 0, 9, 2, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(0.0F, 0.0F, 0.5F);
		bone3.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.5236F, 0.4363F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 4, 117, -0.5F, -5.5F, 0.75F, 0, 9, 2, 0.0F, false));

		cube_r3 = new ModelRenderer(this);
		cube_r3.setRotationPoint(9.9249F, 0.1796F, 2.9181F);
		bone3.addChild(cube_r3);
		setRotationAngle(cube_r3, 3.113F, 0.9888F, 2.695F);
		cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 103, -0.2F, -4.8F, -0.65F, 0, 9, 2, 0.0F, false));

		cube_r4 = new ModelRenderer(this);
		cube_r4.setRotationPoint(11.107F, -0.8129F, -0.0841F);
		bone3.addChild(cube_r4);
		setRotationAngle(cube_r4, 3.1416F, 0.5236F, 2.7053F);
		cube_r4.cubeList.add(new ModelBox(cube_r4, 4, 103, -0.8331F, -4.4011F, -2.4606F, 0, 9, 2, 0.0F, false));

		cube_r5 = new ModelRenderer(this);
		cube_r5.setRotationPoint(11.107F, -0.8129F, -0.0841F);
		bone3.addChild(cube_r5);
		setRotationAngle(cube_r5, -3.1416F, 0.0F, 2.7053F);
		cube_r5.cubeList.add(new ModelBox(cube_r5, 8, 103, -0.4331F, -4.4011F, -0.9159F, 0, 9, 2, 0.0F, false));

		cube_r6 = new ModelRenderer(this);
		cube_r6.setRotationPoint(8.9005F, -0.3418F, -3.0977F);
		bone3.addChild(cube_r6);
		setRotationAngle(cube_r6, -3.1336F, -0.983F, 2.733F);
		cube_r6.cubeList.add(new ModelBox(cube_r6, 16, 103, -0.75F, -4.0F, -1.75F, 0, 9, 2, 0.0F, false));

		cube_r7 = new ModelRenderer(this);
		cube_r7.setRotationPoint(11.107F, -0.8129F, -0.0841F);
		bone3.addChild(cube_r7);
		setRotationAngle(cube_r7, -3.1416F, -0.5236F, 2.7053F);
		cube_r7.cubeList.add(new ModelBox(cube_r7, 12, 103, -0.9172F, -4.4011F, 0.6063F, 0, 9, 2, 0.0F, false));

		cube_r8 = new ModelRenderer(this);
		cube_r8.setRotationPoint(2.5715F, -0.5135F, -2.4578F);
		bone3.addChild(cube_r8);
		setRotationAngle(cube_r8, -0.2435F, -1.2145F, 0.6822F);
		cube_r8.cubeList.add(new ModelBox(cube_r8, 20, 117, -0.6F, -3.8F, -2.45F, 0, 9, 2, 0.0F, false));

		cube_r9 = new ModelRenderer(this);
		cube_r9.setRotationPoint(1.2914F, -0.7365F, -2.2686F);
		bone3.addChild(cube_r9);
		setRotationAngle(cube_r9, -0.1104F, -0.8401F, 0.5359F);
		cube_r9.cubeList.add(new ModelBox(cube_r9, 16, 117, 0.25F, -4.25F, -1.75F, 0, 9, 2, 0.0F, false));

		cube_r10 = new ModelRenderer(this);
		cube_r10.setRotationPoint(0.0F, 0.0F, 0.5F);
		bone3.addChild(cube_r10);
		setRotationAngle(cube_r10, 0.0F, -0.5236F, 0.4363F);
		cube_r10.cubeList.add(new ModelBox(cube_r10, 12, 117, -0.5F, -5.5F, -2.75F, 0, 9, 2, 0.0F, false));

		cube_r11 = new ModelRenderer(this);
		cube_r11.setRotationPoint(0.0F, 0.0F, 0.5F);
		bone3.addChild(cube_r11);
		setRotationAngle(cube_r11, 0.0F, 0.0F, 0.4363F);
		cube_r11.cubeList.add(new ModelBox(cube_r11, 8, 117, 0.0F, -5.5F, -1.0F, 0, 9, 2, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone3.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
