package io.github.talelin.latticy.common.interceptor;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PageDefault {

    @Min(value = 0, message = "{page.number.min}")
    long count() default 10;

    @Min(value = 1, message = "{page.count.min}")
    @Max(value = 30, message = "{page.count.max}")
    long page() default 0;

}
