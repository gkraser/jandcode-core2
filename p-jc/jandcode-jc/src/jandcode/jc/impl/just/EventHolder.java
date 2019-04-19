package jandcode.jc.impl.just;

import groovy.lang.*;
import jandcode.commons.event.*;
import jandcode.jc.*;

/**
 * Реализация и для ctx и для проекта. Если проект задан в конструкторе,
 * но он автоматом передается в аргументы.
 */
@SuppressWarnings("unchecked")
public class EventHolder implements IEvents {

    private Ctx ctx;
    private Project project;
    private EventBus eventBus = new DefaultEventBus();

    class ClosureWrapper implements EventHandler {

        Closure cls;

        ClosureWrapper(Closure cls) {
            this.cls = cls;
        }

        public void handleEvent(Event e) throws Exception {
            if (cls.getParameterTypes().length == 0) {
                cls.call();
            } else {
                cls.call(e);
            }
        }

    }

    public EventHolder(Ctx ctx, Project project) {
        this.ctx = ctx;
        this.project = project;
    }

    public void onEvent(Class eventClass, Closure handler) {
        if (handler == null) {
            return;
        }
        EventHandler h = new ClosureWrapper(handler);
        onEvent(eventClass, h);
    }

    public <T extends Event> void onEvent(Class<T> eventClass, EventHandler<T> handler) {
        eventBus.onEvent(eventClass, handler);
    }

    public <T extends Event> void unEvent(Class<T> eventClass, EventHandler<T> handler) {
        eventBus.unEvent(eventClass, handler);
    }

    public void fireEvent(Event e) {
        if (e instanceof BaseJcEvent) {
            BaseJcEvent ee = (BaseJcEvent) e;
            if (ee.getProject() == null && this.project != null) {
                ee.setProject(this.project);
            }
            ((BaseJcEvent) e).setCtx(this.ctx);
        }
        eventBus.fireEvent(e);
    }

}
