package llc.redstone.hysentials.config.hysentialMods.replace;

import cc.polyfrost.oneconfig.config.annotations.CustomOption;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CustomOption(id = "replace")
public @interface ReplaceAnnotation {
    String name();
    String description() default "";
    String category();
    @Nullable String subcategory();
}
