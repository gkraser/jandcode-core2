package jandcode.mdoc

import jandcode.mdoc.cm.snippet.*
import org.junit.jupiter.api.*

class XmlSnippet_Test extends CustomMDoc_Test {

    String text1 = """
<?xml version="1.0" encoding="utf-8"?>
<root>

    <!-- = root -->
    <web>
        <mount-module/>

        <action name="root"
                template="jandcode/core/samples/docapx1/main/js/main.gsp"/>

        <!-- = about:root/web/module@name=ert/hel -->
        <action name="about"
                template="jandcode/core/samples/docapx1/main/js/pages/about.gsp"/>

        <!-- = -->
    </web>
    <!-- = -->

    <x-include path="backend/index.cfx"/>

</root>
"""

    @Test
    public void test1() throws Exception {
        XmlSnippet z = new XmlSnippet()
        z.configure(text1, "xml")
        //
        for (p in z.getParts()) {
            utils.delim("${p.getName()} (${p.getLang()})")
            println p.getText()
        }
    }


}
