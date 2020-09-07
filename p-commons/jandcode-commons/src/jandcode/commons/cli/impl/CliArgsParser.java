package jandcode.commons.cli.impl;


import jandcode.commons.cli.*;

/**
 * Разбор командной строки
 */
public class CliArgsParser {

    public CliArgs parse(String[] args) {
        CliArgsImpl res = new CliArgsImpl(args);
        if (args == null || args.length == 0) {
            return res;
        }
        //
        for (String arg : args) {
            if (arg == null || arg.length() == 0) {
                continue;
            }
            if (arg.startsWith("-")) {
                // option
                String opt = arg.substring(1);
                String value = "";
                int a = opt.indexOf(':');
                if (a != -1) {
                    value = opt.substring(a + 1);
                    opt = opt.substring(0, a);
                }
                res.put(opt, value);
            } else {
                res.getParams().add(arg);
            }
        }
        //
        return res;
    }

}
