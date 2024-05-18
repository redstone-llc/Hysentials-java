package llc.redstone.hysentials.config.hysentialmods.page;

import cc.polyfrost.oneconfig.config.annotations.CustomOption;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CustomOption(id = "customPage")
public @interface PageAnnotation {
    String name();
    boolean group() default false;
    String description() default "";
    String category() default "General";
    String subcategory() default "General";
}
