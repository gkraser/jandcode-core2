package jandcode.mdoc

import jandcode.commons.vdir.*
import jandcode.core.std.*
import jandcode.mdoc.source.*
import jandcode.mdoc.source.impl.*
import org.junit.jupiter.api.*

public class VDirSourceFileLoader_Test extends CustomMDoc_Test {

    @Test
    public void test1() throws Exception {
        VDir vdir = app.bean(JcDataAppService.class).getVdir();

        System.out.println(vdir.getRoots());

        VDirSourceFileLoader ldr = new VDirSourceFileLoader(vdir, "mdoc/js", "_theme/js", null, Arrays.asList("**/*.md"));
        List<SourceFile> lst = ldr.loadFiles();
        for (SourceFile f : lst) {
            System.out.println(f.getPath());
        }
    }


}
