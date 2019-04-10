package jandcode.commons.conf.impl;

import jandcode.commons.conf.*;

import java.util.*;

public class ConfOriginImpl implements ConfOrigin {

    private Pos first;
    private List<Pos> nexts;
    private Map<String, Pos> props;

    class Pos {
        String fn;
        int lineNum;

        public Pos(String fn, int lineNum) {
            this.fn = fn;
            this.lineNum = lineNum;
        }
    }

    public void addPos(String fn, int lineNum) {
        Pos pos = new Pos(fn, lineNum);
        if (this.first == null) {
            this.first = pos;
        } else {
            if (this.nexts == null) {
                this.nexts = new ArrayList<>(1);
            }
            this.nexts.add(pos);
        }
    }

    public void addPosProp(String prop, String fn, int lineNum) {
        Pos pos = new Pos(fn, lineNum);
        if (this.props == null) {
            this.props = new HashMap<>();
        }
        this.props.put(prop, pos);
    }

    public String toString() {
        if (first == null) {
            return "unknown";
        }
        StringBuilder sb = new StringBuilder();
        buildToString(sb, first);
        if (nexts != null) {
            for (Pos p : nexts) {
                buildToString(sb, p);
            }
        }
        return sb.toString();
    }

    private void buildToString(StringBuilder sb, Pos pos) {
        if (sb.length() != 0) {
            sb.append("\n");
        }
        sb.append(pos.fn);
        if (pos.lineNum > 0) {
            sb.append(":");
            sb.append(pos.lineNum);
        }
    }
}
