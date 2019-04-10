package jandcode.mdoc


import org.junit.jupiter.api.*

import java.util.regex.*

class Builder_Test extends CustomMDoc_Test {

    @Test
    public void doc1() throws Exception {
        utils.logOn()
        mdoc.buildDoc("doc1")
    }

    @Test
    public void real1() throws Exception {
        utils.logOn()
        mdoc.buildDoc("real1")
    }

    @Test
    public void doc1_refs() throws Exception {
        def builder = mdoc.buildDoc("doc1")
        String s = builder.outFiles.findByPath("refs/refs.html").sourceFile.text
        Pattern p = Pattern.compile("BREADCRUMB(.*?)PART2", Pattern.DOTALL | Pattern.MULTILINE)
        Matcher m = p.matcher(s)
        if (m.find()) {
            println m.group(1)
        } else {
            println s
        }
    }

    @Test
    public void doc1_wrapDoc1() throws Exception {
        def builder = mdoc.buildDoc("doc1")
        utils.delim("ORIGINAL")
        mdoc.printDoc(builder.originalDoc)
        utils.delim("BUILDER")
        mdoc.printDoc(builder.doc)
    }

    @Test
    public void doc1_cm_demo1() throws Exception {
        def builder = mdoc.buildDoc("doc1")
        String s = builder.outFiles.findByPath("cm/demo1.html").sourceFile.text
        Pattern p = Pattern.compile("BEGIN(.*?)END", Pattern.DOTALL | Pattern.MULTILINE)
        Matcher m = p.matcher(s)
        if (m.find()) {
            println m.group(1)
        } else {
            println s
        }
    }

    @Test
    public void doc1_code_gen11() throws Exception {
        def builder = mdoc.buildDoc("doc1")
        String s = builder.outFiles.findByPath("cm/code-gen1.html").sourceFile.text
        Pattern p = Pattern.compile("BEGIN(.*?)END", Pattern.DOTALL | Pattern.MULTILINE)
        Matcher m = p.matcher(s)
        if (m.find()) {
            println m.group(1)
        } else {
            println s
        }
    }

    @Test
    public void doc1_refs_local1() throws Exception {
        def builder = mdoc.buildDoc("doc1")
        String s = builder.outFiles.findByPath("refs/refs-local1.html").sourceFile.text
        Pattern p = Pattern.compile("PART1(.*?)PART2", Pattern.DOTALL | Pattern.MULTILINE)
        Matcher m = p.matcher(s)
        if (m.find()) {
            println m.group(1)
        } else {
            println s
        }
    }

}
