package llc.redstone.hysentials.config.hysentialMods.rank;

import cc.polyfrost.oneconfig.config.annotations.CustomOption;
import cc.polyfrost.oneconfig.config.core.OneColor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CustomOption(id = "rank")
public @interface RankAnnotation {
    String name();
    String description() default "";
    String category();
    String subcategory();
    String defaultNametagColor();
    String defaultChatMessageColor();
}
