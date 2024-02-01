package llc.redstone.hysentials.mixin;

import cc.polyfrost.oneconfig.libs.checker.units.qual.A;
import net.minecraft.util.ChatComponentText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ChatComponentText.class)
public interface ChatCompontentTextAccessor {
    @Accessor("text")
    String getText();
    @Accessor("text")
    void setText(String text);
}
