package jandcode.commons.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;

/**
 * Раскрытие подстановок ${x} в строках.
 */
public class SubstVarParser extends TextParser implements ISubstVar {

    protected StringBuilder res = new StringBuilder();
    protected char startVar1 = '$';
    protected char startVar2 = '{';
    protected char stopVar = '}';
    protected ISubstVar handler = this;
    protected StringBuilder varBuffer = new StringBuilder(32);

    public SubstVarParser() {
    }

    public SubstVarParser(char startVar1, char startVar2, char stopVar, ISubstVar handler) {
        this.startVar1 = startVar1;
        this.startVar2 = startVar2;
        this.stopVar = stopVar;
        this.handler = handler;
    }

    public String onSubstVar(String v) {
        return "";
    }

    /**
     * Результат разбора
     */
    public String getResult() {
        return res.toString();
    }

    //////

    protected void onParse() throws Exception {
        res.setLength(0);
        while (true) {
            char c = next();
            if (c == EOF) {
                break;
            }
            if (c == '\\') {
                char c2 = next();
                if (c2 == startVar1) {
                    res.append(c2);
                } else if (c2 == '\\') {
                    res.append(c);
                } else {
                    res.append(c);
                    push(c2);
                }
            } else if (c == startVar1) {
                char c2 = next();
                if (c2 == startVar2) {
                    String s = grabUntil(stopVar);
                    s = handler.onSubstVar(s);
                    res.append(s);
                } else {
                    push(c2);
                    res.append(c);
                }
            } else {
                res.append(c);
            }
        }
    }

    private String grabUntil(char terminate) throws Exception {
        varBuffer.setLength(0);
        while (true) {
            char c = next();
            if (c != terminate && c != EOF) {
                varBuffer.append(c);
            } else {
                break;
            }
        }
        return varBuffer.toString();
    }

}
