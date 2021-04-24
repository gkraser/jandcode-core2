package jandcode.commons.groovy;

import groovy.lang.*;
import jandcode.commons.*;
import jandcode.commons.groovy.impl.json.*;

public class StaticInit {

    static {

        // json

        UtJson.getJsonEngine().addGsonBuilderIniter(gsonBuilder -> {
            gsonBuilder.registerTypeHierarchyAdapter(GString.class, new GStringAdapter());
        });

    }

}
