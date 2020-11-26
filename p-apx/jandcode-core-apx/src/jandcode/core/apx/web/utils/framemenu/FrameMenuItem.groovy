package jandcode.core.apx.web.utils.framemenu

import groovy.transform.*

/**
 * Элемент меню фреймов
 */
@CompileStatic
class FrameMenuItem {

    String title
    String path
    String routePath
    String icon
    List<FrameMenuItem> items

    void sort() {
        if (items == null) {
            return
        }
        items.sort { a, b ->
            if (a.folder == b.folder) {
                return a.title <=> b.title
            }
            return a.folder ? -1 : 1
        }
        for (z in items) {
            z.sort()
        }
    }

    void addItem(FrameMenuItem item) {
        if (this.items == null) {
            this.items = []
        }
        this.items.add(item)
    }

    boolean isFolder() {
        return this.items != null
    }

    void setFolder(boolean folder) {
        if (folder) {
            if (this.items == null) {
                this.items = []
            }
        } else {
            this.items = null
        }
    }

    boolean isEmptyFolder() {
        return this.items == null || this.items.size() == 0
    }
}
