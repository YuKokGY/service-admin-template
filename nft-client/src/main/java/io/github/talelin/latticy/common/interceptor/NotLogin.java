package io.github.talelin.latticy.common.interceptor;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotLogin {

}
