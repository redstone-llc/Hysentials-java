package llc.redstone.hysentials.cosmetics.wings.tdarth;

import llc.redstone.hysentials.utils.animation.Animation;
import llc.redstone.hysentials.utils.animation.Keyframe;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import java.util.Arrays;

public class TdarthModel extends ModelBase {
    private final ModelRenderer all;
    private final ModelRenderer right;
    private final ModelRenderer rr;
    private final ModelRenderer cube_r1;
    private final ModelRenderer ri;
    private final ModelRenderer cube_r2;
    private final ModelRenderer left;
    private final ModelRenderer ll;
    private final ModelRenderer cube_r3;
    private final ModelRenderer li;
    private final ModelRenderer cube_r4;
    private final ModelRenderer bb_main;
    private final ModelRenderer cube_r5;

    private final Animation rightAnimation;
    private final Animation leftAnimation;

    public TdarthModel() {
        textureWidth = 64;
        textureHeight = 64;

        all = new ModelRenderer(this);
        all.setRotationPoint(0.0F, 24.0F, 0.0F);


        right = new ModelRenderer(this);
        right.setRotationPoint(-3.0F, -21.0F, 2.0F);
        all.addChild(right);


        rr = new ModelRenderer(this);
        rr.setRotationPoint(-8.0F, 0.0F, 0.0F);
        right.addChild(rr);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(-7.0F, 0.0F, -1.0F);
        rr.addChild(cube_r1);
        setRotationAngle(cube_r1, 1.5708F, 0.0F, 3.1416F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -7.0F, -6.0F, 0.0F, 14, 12, 0, 0.0F, false));

        ri = new ModelRenderer(this);
        ri.setRotationPoint(0.0F, 0.0F, 0.0F);
        right.addChild(ri);


        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-4.0F, 0.0F, -1.0F);
        ri.addChild(cube_r2);
        setRotationAngle(cube_r2, 1.5708F, 0.0F, 3.1416F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 24, -4.0F, -6.0F, 0.0F, 8, 12, 0, 0.0F, false));

        left = new ModelRenderer(this);
        left.setRotationPoint(3.0F, -21.0F, 2.0F);
        all.addChild(left);


        ll = new ModelRenderer(this);
        ll.setRotationPoint(8.0F, 0.0F, 0.0F);
        left.addChild(ll);


        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(7.0F, 0.0F, -1.0F);
        ll.addChild(cube_r3);
        setRotationAngle(cube_r3, 1.5708F, 0.0F, 3.1416F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 0, 12, -7.0F, -6.0F, 0.0F, 14, 12, 0, 0.0F, false));

        li = new ModelRenderer(this);
        li.setRotationPoint(0.0F, 0.0F, 0.0F);
        left.addChild(li);


        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(4.0F, 0.0F, -1.0F);
        li.addChild(cube_r4);
        setRotationAngle(cube_r4, 1.5708F, 0.0F, 3.1416F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 16, 24, -4.0F, -6.0F, 0.0F, 8, 12, 0, 0.0F, false));

        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);


        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(0.0F, -21.0F, 2.0F);
        bb_main.addChild(cube_r5);
        setRotationAngle(cube_r5, -3.1416F, 0.0F, 3.1416F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 28, 0, -3.0F, -1.0F, -0.5F, 2, 2, 1, 0.0F, false));
        cube_r5.cubeList.add(new ModelBox(cube_r5, 28, 3, 1.0F, -1.0F, -0.5F, 2, 2, 1, 0.0F, false));

        rightAnimation = new Animation(
            right,
            Arrays.asList(
                Keyframe.create(0.0, 0.0, 0.0, 0.0, -45.0, 0.0, -35.0),
                Keyframe.create(1.0, 0.0, 0.0, 0.0, -45.0, 0.0, 35.0),
                Keyframe.create(2.0, 0.0, 0.0, 0.0, -45.0, 0.0, -35.0)
            ),
            Arrays.asList(
                new Animation(rr, Arrays.asList(
                    Keyframe.create(0.1667, 0.0, 0.0, 0.0, 0.0, 0.0, -30.0),
                    Keyframe.create(1.1667, 0.0, 0.0, 0.0, 0.0, 0.0, 30.0),
                    Keyframe.create(2.1667, 0.0, 0.0, 0.0, 0.0, 0.0, -30.0)
                ), null)
            )
        );

        leftAnimation = new Animation(
            left,
            Arrays.asList(
                Keyframe.create(0.0, 0.0, 0.0, 0.0, -45.0, 0.0, 35.0),
                Keyframe.create(1.0, 0.0, 0.0, 0.0, -45.0, 0.0, -35.0),
                Keyframe.create(2.0, 0.0, 0.0, 0.0, -45.0, 0.0, 35.0)
            ),
            Arrays.asList(
                new Animation(ll, Arrays.asList(
                    Keyframe.create(0.1667, 0.0, 0.0, 0.0, 0.0, 0.0, 30.0),
                    Keyframe.create(1.1667, 0.0, 0.0, 0.0, 0.0, 0.0, -30.0),
                    Keyframe.create(2.1667, 0.0, 0.0, 0.0, 0.0, 0.0, 30.0)
                ), null)
            )
        );
    }

    Long start1 = null;
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (start1 == null) {
            start1 = System.currentTimeMillis();
        }
        float time = (System.currentTimeMillis() - start1) / 1000.0f;
        //round to 4 decimal places
        time = Math.round(time * 10000.0f) / 10000.0f;
        if (time > 2.1667) {
            start1 = System.currentTimeMillis();
        }

        rightAnimation.apply(time);
        leftAnimation.apply(time);

        all.render(f5);
        bb_main.render(f5);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
