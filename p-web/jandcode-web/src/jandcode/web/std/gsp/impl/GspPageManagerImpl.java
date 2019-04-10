package jandcode.web.std.gsp.impl;

import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.web.gsp.*;
import jandcode.web.std.gsp.*;

import java.util.*;

/**
 * Поддержка для генерации тега jc:page
 */
public class GspPageManagerImpl extends BaseComp implements GspPageManager, IGspContextLinkSet {

    private NamedList<Part> parts = new DefaultNamedList<>();
    private GspContext gspContext;
    private IVariantMap args = new VariantMap();

    class PartItem {
        Gsp gsp;
        ITextBuffer buffer;

        public PartItem(Gsp gsp) {
            this.gsp = gsp;
        }

    }


    class Part extends Named {
        private List<PartItem> items = new ArrayList<>();
        private int indexToAdd = 0;

        public Part(String name) {
            this.setName(name);
        }

        void add(PartItem p) {
            items.add(indexToAdd, p);
            indexToAdd++;
        }

        public void out() {
            for (PartItem it : items) {
                if (it.buffer != null) {
                    gspContext.getCurrentGsp().out(it.buffer);
                } else {
                    it.gsp.outBody();
                }
            }
        }
    }

    private Part find(String name, boolean create) {
        Part p = parts.find(name);
        if (p == null && create) {
            p = new Part(name);
            parts.add(p);
        }
        return p;
    }

    public void setGspContext(GspContext gspContext) {
        this.gspContext = gspContext;
    }

    public void addPart(String name, BaseGsp gsp, boolean deffer) {
        Part p = find(name, true);
        PartItem pi = new PartItem(gsp);
        p.add(pi);
        if (!deffer) {
            pi.buffer = gsp.grabBody();
        }
    }

    public void outPart(String name) {
        Part p = find(name, false);
        if (p == null) {
            return; // игнорируем не найденные, это допустимо
        }
        p.out();
    }

    public boolean hasPart(String name) {
        return find(name, false) != null;
    }

    public void resetAddPart() {
        for (Part part : parts) {
            part.indexToAdd = 0;
        }
    }

    public IVariantMap getArgs() {
        return args;
    }

}
