package jandcode.core.dbm.verdb.impl;

import jandcode.commons.*;
import jandcode.core.dbm.verdb.*;

public class VerdbVersionImpl implements VerdbVersion {

    private Long v1;
    private Long v2;
    private Long v3;

    public VerdbVersionImpl(long v1, long v2, long v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public VerdbVersionImpl(String ver) {
        this.v1 = 0L;
        this.v2 = 0L;
        this.v3 = 0L;
        if (UtString.empty(ver)) {
            return;
        }
        String[] ar = ver.split("\\.");
        if (ar.length > 0) {
            this.v1 = UtCnv.toLong(ar[0]);
        }
        if (ar.length > 1) {
            this.v2 = UtCnv.toLong(ar[1]);
        }
        if (ar.length > 2) {
            this.v3 = UtCnv.toLong(ar[2]);
        }
    }

    public String toString() {
        return getText();
    }

    public String getText() {
        return "" + v1 + "." + v2 + "." + v3;
    }

    public long getV1() {
        return v1;
    }

    public long getV2() {
        return v2;
    }

    public long getV3() {
        return v3;
    }

    public int compareTo(VerdbVersion o) {
        VerdbVersionImpl o1 = (VerdbVersionImpl) o;

        int z1 = v1.compareTo(o1.v1);
        if (z1 == 0) {
            z1 = v2.compareTo(o1.v2);
            if (z1 == 0) {
                z1 = v3.compareTo(o1.v3);
            }
        }

        return z1;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VerdbVersion v1 = (VerdbVersion) o;
        return compareTo(v1) == 0;
    }

    public VerdbVersion with(long v1, long v2, long v3) {
        return new VerdbVersionImpl(
                v1 < 0 ? this.v1 : v1,
                v2 < 0 ? this.v2 : v2,
                v3 < 0 ? this.v3 : v3
        );
    }
}
