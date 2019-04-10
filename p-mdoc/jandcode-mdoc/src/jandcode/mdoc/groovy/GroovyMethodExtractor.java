package jandcode.mdoc.groovy;

import jandcode.commons.*;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.control.*;

import java.util.*;

/**
 * Извлекалка тел методов
 */
public class GroovyMethodExtractor {

    class Vis implements GroovyClassVisitor {

        private List<MethodDef> methods;

        public Vis(List<MethodDef> methods) {
            this.methods = methods;
        }

        public void visitClass(ClassNode node) {
        }

        public void visitConstructor(ConstructorNode node) {
        }

        public void visitMethod(MethodNode node) {
            MethodDef md = new MethodDef();
            md.name = node.getName();
            md.startLine = node.getLineNumber() - 1;
            md.stopLine = node.getLastLineNumber() - 1;
            this.methods.add(md);
        }

        public void visitField(FieldNode node) {
        }

        public void visitProperty(PropertyNode node) {
        }

    }

    class MethodDef {
        String name;
        int startLine;
        int stopLine;
    }

    /**
     * Извлекает из текста groovy/java тела методов.
     * Текст должен быть синтаксически правильным для groovy.
     * Т.е. если он на java, то должен быть таким, что бы groovy его понял.
     *
     * @param text текст java/groovy
     * @return ключ - имя метода, значение - тело метода
     */
    public Map<String, String> extractMethodBodys(String text) {
        Map<String, String> res = new HashMap<>();

        CompilationUnit cu = new CompilationUnit();
        cu.addSource("sourceText", text);

        cu.compile(Phases.CONVERSION);

        List<MethodDef> methods = new ArrayList<>();

        CompileUnit ast = cu.getAST();
        for (Object z : ast.getClasses()) {
            ClassNode cn = (ClassNode) z;
            cn.visitContents(new Vis(methods));
        }

        String[] lines = text.split("\n");
        for (MethodDef md : methods) {
            StringBuilder mt = new StringBuilder();
            for (int i = md.startLine; i <= md.stopLine; i++) {
                String s = lines[i];
                if (i == md.startLine) {
                    int a = s.indexOf('{');
                    if (a != -1) {
                        s = s.substring(a + 1);
                    }
                }
                if (i == md.stopLine) {
                    int a = s.lastIndexOf('}');
                    if (a != -1) {
                        s = s.substring(0, a);
                    }
                }
                mt.append(s);
                mt.append("\n");
            }
            String s = UtString.normalizeIndent(mt.toString());
            res.put(md.name, s);
        }

        return res;
    }

}
