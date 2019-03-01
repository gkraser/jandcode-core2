package jc._inc

import jandcode.jc.*
import jandcode.jc.std.*

class EmptyJavaProject extends ProjectScript {
    void onInclude() {
        include(CreateProject)
        include(JavaProject)
    }
}