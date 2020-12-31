package jandcode.jc;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CmOptDefs {
    CmOptDef[] value();
}
