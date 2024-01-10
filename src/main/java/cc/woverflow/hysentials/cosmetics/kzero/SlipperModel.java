package cc.woverflow.hysentials.cosmetics.kzero;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class SlipperModel extends ModelBase {
	private final ModelRenderer bone4;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;

	public SlipperModel() {
		textureWidth = 128;
		textureHeight = 128;

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(6.7393F, 22.4867F, 0.5F);
		bone4.cubeList.add(new ModelBox(bone4, 100, 122, -11.2393F, 0.5133F, -3.5F, 9, 1, 5, 0.0F, false));

		cube_r1 = new ModelRenderer(this);
		cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone4.addChild(cube_r1);
		setRotationAngle(cube_r1, 3.1416F, 0.0F, 2.7053F);
		cube_r1.cubeList.add(new ModelBox(cube_r1, 98, 126, 2.25F, -1.5F, 0.5F, 0, 1, 1, 0.0F, false));
		cube_r1.cubeList.add(new ModelBox(cube_r1, 98, 126, 2.25F, -1.5F, -1.5F, 0, 1, 1, 0.0F, false));

		cube_r2 = new ModelRenderer(this);
		cube_r2.setRotationPoint(-11.7393F, 0.0133F, -0.5F);
		bone4.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.4625F);
		cube_r2.cubeList.add(new ModelBox(cube_r2, 98, 126, 0.75F, -0.75F, -1.0F, 0, 1, 1, 0.0F, false));
		cube_r2.cubeList.add(new ModelBox(cube_r2, 98, 126, 0.75F, -0.75F, 1.0F, 0, 1, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone4.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
