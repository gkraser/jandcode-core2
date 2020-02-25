package jandcode.commons.cli.impl;

import jandcode.commons.cli.*;
import jandcode.commons.variant.*;

import java.util.*;

public class CliArgsImpl extends VariantMap implements CliArgs {

    private String[] args;
    private List<String> params = new ArrayList<>();

    public CliArgsImpl(String[] args) {
        this.args = args;
        if (this.args == null) {
            this.args = new String[]{};
        }
    }

    public String[] getArgs() {
        return args;
    }

    public List<String> getParams() {
        return params;
    }

}
