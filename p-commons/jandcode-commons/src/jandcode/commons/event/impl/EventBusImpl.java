package jandcode.commons.event.impl;

import jandcode.commons.error.*;
import jandcode.commons.event.*;

import java.util.*;
import java.util.concurrent.*;

public class EventBusImpl implements EventBus {

    private List<HandlerItem> handlers = new CopyOnWriteArrayList<>();

    class HandlerItem {
        Class cls;
        EventHandler handler;

        public HandlerItem(Class cls, EventHandler handler) {
            this.cls = cls;
            this.handler = handler;
        }

        void handleEvent(Event e) throws Exception {
            if (e.getClass().isAssignableFrom(cls)) {
                handler.handleEvent(e);
            }
        }

        boolean isEqual(Class cls, EventHandler handler) {
            return this.cls == cls && this.handler == handler;
        }
    }

    //////

    public <T extends Event> void onEvent(Class<T> eventClass, EventHandler<T> handler) {
        handlers.add(new HandlerItem(eventClass, handler));
    }

    public <T extends Event> void unEvent(Class<T> eventClass, EventHandler<T> handler) {
        List<HandlerItem> tmp = null;
        for (HandlerItem it : handlers) {
            if (it.isEqual(eventClass, handler)) {
                if (tmp == null) {
                    tmp = new ArrayList<>();
                }
                tmp.add(it);
            }
        }
        if (tmp != null) {
            handlers.removeAll(tmp);
        }
    }

    public void fireEvent(Event e) {
        try {
            for (HandlerItem it : handlers) {
                it.handleEvent(e);
            }
        } catch (Exception e1) {
            throw new XErrorWrap(e1);
        }
    }

}
