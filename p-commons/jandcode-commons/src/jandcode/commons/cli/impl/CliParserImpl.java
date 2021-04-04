package jandcode.commons.cli.impl;

import jandcode.commons.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;

import java.util.*;

public class CliParserImpl implements CliParser {

    private List<String> args;

    public CliParserImpl(List<String> args) {
        this.args = new ArrayList<>();
        if (args != null) {
            this.args.addAll(args);
        }
    }

    public CliParserImpl(String[] args) {
        this(Arrays.asList(args));
    }

    public List<String> getArgs() {
        return args;
    }

    public String extractParam() {
        List<String> lst = getArgs();
        for (int i = 0; i < lst.size(); i++) {
            String s = lst.get(i);
            if (!UtString.empty(s) && !s.startsWith("-")) {
                lst.remove(i);
                return s;
            }
        }
        return null;
    }

    public Map<String, Object> extractOpts(Collection<? extends ICliOpt> opts) {
        Map<String, Object> res = new LinkedHashMap<>();
        List<String> notParsed = new ArrayList<>();

        Map<String, ICliOpt> optByName = new HashMap<>();
        List<ICliOpt> positionalOpt = new ArrayList<>();
        for (ICliOpt opt : opts) {
            if (opt.isPositional()) {
                positionalOpt.add(opt);
            } else {
                for (String nm : opt.getNames()) {
                    optByName.put(nm, opt);
                }
            }
        }

        int i = 0;
        int positionalIdx = 0;
        List<String> lst = getArgs();
        while (i < lst.size()) {
            String s = lst.get(i);
            if (UtString.empty(s)) {
                i++;
                continue;
            }
            // параметры пропускаем
            if (!s.startsWith("-")) {
                // позиционный параметр
                if (positionalIdx < positionalOpt.size()) {
                    // имеются описанные позиционные параметры
                    ICliOpt posOpt = positionalOpt.get(positionalIdx);
                    if (!posOpt.isMulti()) {
                        res.put(posOpt.getKey(), s);
                        positionalIdx++; // переходим на следующий
                    } else {
                        List<String> curValue = (List<String>) res.get(posOpt.getKey());
                        if (curValue == null) {
                            curValue = new ArrayList<>();
                            res.put(posOpt.getKey(), curValue);
                        }
                        curValue.add(s);
                    }
                } else {
                    notParsed.add(s);
                }
                i++;
                continue;
            }
            String name;
            String value = null;

            int a = s.indexOf("=");
            if (a != -1) {
                // опция со знаком '='
                name = s.substring(0, a);
                value = s.substring(a + 1);
            } else {
                name = s;
            }

            ICliOpt opt = optByName.get(name);
            if (opt == null) {
                // не наше
                notParsed.add(s);
                i++;
                continue;
            }

            // наше
            if (value == null) {
                if (opt.hasArg()) {
                    // аргумент нужен, но через '=' не передан
                    if (i == lst.size() - 1) {
                        // более нет аргументов
                        throw new XError("Для аргумента {0} требуется параметр", name);
                    }
                    i++;
                    value = lst.get(i);
                    if (value.startsWith("-")) {
                        // значение аргемента не может начинатся с '-'
                        throw new XError("Для аргумента {0} требуется параметр", name);
                    }
                } else {
                    value = "true";
                }
            }

            if (!opt.hasArg() && !UtCnv.toBoolean(value)) {
                // это случай '-b=false' для опций без аргументов
                // удаляем такие, как будто их и небыло
                res.remove(opt.getKey());
            } else {
                // найденное
                if (opt.isMulti()) {
                    List<String> curValue = (List<String>) res.get(opt.getKey());
                    if (curValue == null) {
                        curValue = new ArrayList<>();
                        res.put(opt.getKey(), curValue);
                    }
                    curValue.add(value);
                } else {
                    res.put(opt.getKey(), value);
                }
            }

            i++;
        }

        // оставляем не распарзенные
        lst.clear();
        lst.addAll(notParsed);

        return res;
    }

    public Map<String, Object> extractOpts(ICliDef desc) {
        return extractOpts(desc.getOpts());
    }

}
