package llc.redstone.hysentials.cosmetics.hats.ponjo;

import llc.redstone.hysentials.utils.animation.Animation;
import llc.redstone.hysentials.utils.animation.Keyframe;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;

public class PonjoHelmetModel extends ModelBase {
    private final ModelRenderer bone;
    private final ModelRenderer top;
    private final ModelRenderer toppart;
    private final ModelRenderer topmoustache;
    private final ModelRenderer topteeth;
    private final ModelRenderer bottom;
    private final ModelRenderer bottompart;
    private final ModelRenderer bottomteeth;

    private final Animation idleTopAnimation;
    private final Animation eatingTopAnimation;


    public PonjoHelmetModel() {
        textureWidth = 64;
        textureHeight = 64;

        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 20.0F, 0.0F);
        setRotationAngle(bone, 0.0F, 1.5708F, 0.0F);


        top = new ModelRenderer(this);
        top.setRotationPoint(-4.0F, 1.0F, 0.0F);
        bone.addChild(top);
        setRotationAngle(top, 0.0F, 0.0F, -0.2618F);


        toppart = new ModelRenderer(this);
        toppart.setRotationPoint(4.0F, 3.0F, 0.0F);
        top.addChild(toppart);


        ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(0.0F, -4.0F, 0.0F);
        toppart.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, -0.48F);
        cube_r1.cubeList.add(new ModelBox(cube_r1, 0, 0, -4.0F, -3.0F, -5.0F, 15, 3, 10, 0.0F, false));

        ModelRenderer cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(-4.75F, -4.25F, 0.0F);
        toppart.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.2618F);
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 21, -1.0F, -1.0F, -5.0F, 3, 1, 1, 0.0F, false));
        cube_r2.cubeList.add(new ModelBox(cube_r2, 0, 8, -1.0F, -1.0F, 4.0F, 3, 1, 1, 0.0F, false));

        ModelRenderer cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(-1.0F, -6.0F, 0.0F);
        toppart.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, -0.48F);
        cube_r3.cubeList.add(new ModelBox(cube_r3, 24, 29, 7.0F, -2.0F, -4.0F, 4, 2, 8, 0.0F, false));

        ModelRenderer cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-9.0F, -1.5F, 0.0F);
        toppart.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, 0.0F, -0.48F);
        cube_r4.cubeList.add(new ModelBox(cube_r4, 0, 26, 5.0F, -3.0F, -4.0F, 8, 3, 8, 0.0F, false));

        ModelRenderer cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(-9.5F, -2.5F, 1.0F);
        toppart.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.0F, -0.48F);
        cube_r5.cubeList.add(new ModelBox(cube_r5, 40, 13, 6.0F, -3.0F, 0.0F, 4, 3, 3, 0.0F, false));
        cube_r5.cubeList.add(new ModelBox(cube_r5, 40, 0, 6.0F, -3.0F, -5.0F, 4, 3, 3, 0.0F, false));

        topmoustache = new ModelRenderer(this);
        topmoustache.setRotationPoint(8.9895F, -5.3686F, 0.0F);
        top.addChild(topmoustache);
        topmoustache.cubeList.add(new ModelBox(topmoustache, 24, 28, 0.2605F, -1.6314F, 5.0F, 2, 2, 1, 0.0F, false));
        topmoustache.cubeList.add(new ModelBox(topmoustache, 0, 29, 0.2605F, -1.6314F, -6.0F, 2, 2, 1, 0.0F, false));

        ModelRenderer cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(-6.9895F, 5.8686F, -4.0F);
        topmoustache.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, -0.48F);
        cube_r6.cubeList.add(new ModelBox(cube_r6, 40, 19, 5.0F, -3.0F, -3.0F, 5, 2, 1, 0.0F, false));
        cube_r6.cubeList.add(new ModelBox(cube_r6, 40, 6, 5.0F, -3.0F, 10.0F, 5, 2, 1, 0.0F, false));

        topteeth = new ModelRenderer(this);
        topteeth.setRotationPoint(4.0F, 3.0F, 0.0F);
        top.addChild(topteeth);


        ModelRenderer cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(0.0F, -4.0F, 0.0F);
        topteeth.addChild(cube_r7);
        setRotationAngle(cube_r7, 0.0F, 0.0F, -0.48F);
        cube_r7.cubeList.add(new ModelBox(cube_r7, 36, 41, 5.0F, 0.0F, 3.0F, 5, 1, 1, 0.0F, false));
        cube_r7.cubeList.add(new ModelBox(cube_r7, 0, 37, 8.0F, 0.0F, -4.0F, 2, 1, 8, 0.0F, false));
        cube_r7.cubeList.add(new ModelBox(cube_r7, 24, 31, 9.0F, 1.0F, -3.0F, 1, 1, 1, 0.0F, false));
        cube_r7.cubeList.add(new ModelBox(cube_r7, 4, 26, 9.0F, 1.0F, 2.0F, 1, 1, 1, 0.0F, false));
        cube_r7.cubeList.add(new ModelBox(cube_r7, 36, 43, 5.0F, 0.0F, -4.0F, 5, 1, 1, 0.0F, false));

        bottom = new ModelRenderer(this);
        bottom.setRotationPoint(0.0F, 4.0F, 0.0F);
        bone.addChild(bottom);


        bottompart = new ModelRenderer(this);
        bottompart.setRotationPoint(0.0F, 0.0F, 0.0F);
        bottom.addChild(bottompart);
        bottompart.cubeList.add(new ModelBox(bottompart, 0, 13, -4.0F, -3.0F, -5.0F, 15, 3, 10, 0.0F, false));
        bottompart.cubeList.add(new ModelBox(bottompart, 40, 26, 8.0F, 0.0F, -2.0F, 3, 1, 4, 0.0F, false));
        bottompart.cubeList.add(new ModelBox(bottompart, 0, 26, 10.0F, 1.0F, -1.0F, 1, 1, 2, 0.0F, false));

        ModelRenderer cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(-5.0F, 0.25F, 7.0F);
        bottompart.addChild(cube_r8);
        setRotationAngle(cube_r8, 0.0F, 0.0F, -0.1745F);
        cube_r8.cubeList.add(new ModelBox(cube_r8, 1, 1, 0.0F, -1.0F, -5.0F, 2, 1, 2, 0.0F, false));
        cube_r8.cubeList.add(new ModelBox(cube_r8, 0, 4, -1.0F, -2.0F, -4.0F, 3, 2, 2, 0.0F, false));
        cube_r8.cubeList.add(new ModelBox(cube_r8, 46, 37, 0.0F, -1.0F, -11.0F, 2, 1, 2, 0.0F, false));
        cube_r8.cubeList.add(new ModelBox(cube_r8, 0, 46, -1.0F, -2.0F, -12.0F, 3, 2, 2, 0.0F, false));

        ModelRenderer cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(-5.0F, -2.5F, 7.0F);
        bottompart.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0F, 0.0F, -0.1745F);
        cube_r9.cubeList.add(new ModelBox(cube_r9, 1, 14, 0.0F, -1.0F, -5.0F, 2, 1, 2, 0.0F, false));
        cube_r9.cubeList.add(new ModelBox(cube_r9, 34, 46, 0.0F, -1.0F, -11.0F, 2, 1, 2, 0.0F, false));

        ModelRenderer cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(-4.75F, -2.0F, 8.0F);
        bottompart.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0F, 0.0F, -0.2618F);
        cube_r10.cubeList.add(new ModelBox(cube_r10, 0, 17, -1.0F, -2.0F, -5.0F, 3, 2, 2, 0.0F, false));
        cube_r10.cubeList.add(new ModelBox(cube_r10, 43, 45, -1.0F, -2.0F, -13.0F, 3, 2, 2, 0.0F, false));

        ModelRenderer cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(-1.5F, -4.0F, 4.75F);
        bottompart.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.0F, 0.0F, -0.3927F);
        cube_r11.cubeList.add(new ModelBox(cube_r11, 40, 31, -2.5F, -1.0F, -0.5F, 5, 4, 1, 0.0F, false));
        cube_r11.cubeList.add(new ModelBox(cube_r11, 24, 41, -2.5F, -1.0F, -10.0F, 5, 4, 1, 0.0F, false));

        bottomteeth = new ModelRenderer(this);
        bottomteeth.setRotationPoint(0.0F, 0.0F, 0.0F);
        bottom.addChild(bottomteeth);
        bottomteeth.cubeList.add(new ModelBox(bottomteeth, 12, 38, 8.0F, -4.0F, -4.0F, 2, 1, 8, 0.0F, false));
        bottomteeth.cubeList.add(new ModelBox(bottomteeth, 0, 32, 9.0F, -5.0F, -4.0F, 1, 1, 1, 0.0F, false));
        bottomteeth.cubeList.add(new ModelBox(bottomteeth, 28, 31, 9.0F, -5.0F, 3.0F, 1, 1, 1, 0.0F, false));
        bottomteeth.cubeList.add(new ModelBox(bottomteeth, 24, 39, 1.0F, -4.0F, -4.0F, 9, 1, 1, 0.0F, false));
        bottomteeth.cubeList.add(new ModelBox(bottomteeth, 24, 26, 1.0F, -4.0F, 3.0F, 9, 1, 1, 0.0F, false));


        idleTopAnimation = new Animation(
            top,
            Arrays.asList(
                Keyframe.create(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                Keyframe.create(0.5, 0.0, 0.0, 0.0, 0.0, 0.0, -7.5),
                Keyframe.create(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                Keyframe.create(1.5, 0.0, 0.0, 0.0, 0.0, 0.0, 7.5),
                Keyframe.create(2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                ),
            Arrays.asList(
                new Animation(topmoustache, Arrays.asList(
                    Keyframe.create(0, 0.0, 0.0, 0.0, 0.0, 0.0, -10.0),
                    Keyframe.create(0.5, 0.0, 0.0, 0.0, 0.0, 0.0, -20.0),
                    Keyframe.create(1.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0),
                    Keyframe.create(2.0, 0.0, 0.0, 0.0, 0.0, 0.0, -10)
                ), null)
            )
        );

        eatingTopAnimation = new Animation(
            top,
            Arrays.asList(
                Keyframe.create(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                Keyframe.create(0.5, 0.0, 0.0, 0.0, 0.0, 0.0, -12.5),
                Keyframe.create(0.625, 0.0, 0.0, 0.0, 0.0, 0.0, 19.0),
                Keyframe.create(1.375, 0.0, 0.0, 0.0, 0.0, 0.0, 19.0),
                Keyframe.create(2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                Keyframe.create(3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                ),
            Arrays.asList(
                new Animation(topmoustache, Arrays.asList(
                    Keyframe.create(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0),
                    Keyframe.create(0.4583, 0.0, 0.0, 0.0, 0.0, 0.0, -25.0),
                    Keyframe.create(0.6667, 0.0, 0.0, 0.0, 0.0, 0.0, 22.5),
                    Keyframe.create(1.0417, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                    Keyframe.create(1.375, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                    Keyframe.create(2.0, 0.0, 0.0, 0.0, 0.0, 0.0, -25.0),
                    Keyframe.create(2.125, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
                    Keyframe.create(3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
                    ), null)
            )
        );
    }

    Long start1 = null;
    int randomCount = 0;
    int count = 0;
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        if (start1 == null) {
            start1 = System.currentTimeMillis();
            randomCount = ((int) (Math.random() * 100)) / (getNearbyPlayers(entity));
        }
        float time = (System.currentTimeMillis() - start1) / 1000.0f;
        //round to 4 decimal places
        time = Math.round(time * 10000.0f) / 10000.0f;
        if (time > 2 && (count != randomCount)) {
            start1 = System.currentTimeMillis();
            count++;
        } else if (time > 3) {
            start1 = System.currentTimeMillis();
            count = 0;
            randomCount = ((int) (Math.random() * 100)) / (getNearbyPlayers(entity));
        }

        if (randomCount != count) {
            idleTopAnimation.apply(time);
        } else {
            eatingTopAnimation.apply(time);
        }
        bone.render(f5);
    }

    private static int getNearbyPlayers(Entity entity) {
        return entity.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, entity.getEntityBoundingBox().expand(10, 10, 10)).size();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
