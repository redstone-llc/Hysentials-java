package llc.redstone.hysentials.config.hysentialmods.icons;

import cc.polyfrost.oneconfig.config.annotations.CustomOption;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CustomOption(id = "icons")
public @interface IconsAnnotation {
    String name();
    String description() default "";
    String category();
    @Nullable String subcategory();
}
