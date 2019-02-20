package jandcode.jc;

import jandcode.commons.event.*;

/**
 * Базовый предок для событий jc
 */
public abstract class BaseJcEvent implements Event {

    private Ctx ctx;
    private Project project;

    public Ctx getCtx() {
        return ctx;
    }

    public void setCtx(Ctx ctx) {
        this.ctx = ctx;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
