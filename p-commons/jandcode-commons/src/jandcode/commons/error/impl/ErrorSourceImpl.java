package jandcode.commons.error.impl;


import jandcode.commons.error.*;

public class ErrorSourceImpl implements ErrorSource {

    private String sourceName;
    private int lineNum;
    private String lineText;
    private String lineTextPrepared;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public String getLineText() {
        return lineText;
    }

    public void setLineText(String lineText) {
        this.lineText = lineText;
    }

    public String getLineTextPrepared() {
        return lineTextPrepared;
    }

    public void setLineTextPrepared(String lineTextPrepared) {
        this.lineTextPrepared = lineTextPrepared;
    }

}
