package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.datetime.*
import jandcode.commons.named.*
import jandcode.jc.*

/**
 * Слоты для деплоя.
 * Работает как набор уникальных каталогов, в которые можно разворачивать приложение или
 * просто распаковывать zip-файлы.
 */
class DeploySlots extends ProjectScript {

    public static final XDateTimeFormatter dateFmt = UtDateTime.createFormatter("yyyy-MM-dd--HH-mm-ss--SSS")

    String rootDir = "_deploy"
    private NamedList<Slot> _slots


    class Slot implements INamed {

        String name

        /**
         * Корневой каталог слота
         */
        String slotPath

        /**
         * Рабочий каталог слота.
         * Например - если в слот распаковать zip, и он имеет
         * один корневой каталог, то рабочий каталог установится на это подкаталог.
         */
        String workPath

        Slot(String slotPath) {
            this.slotPath = slotPath
            this.name = UtFile.filename(this.slotPath)
            this.workPath = null
        }

        String getName() {
            return name
        }

        String getWorkPath() {
            if (this.workPath == null) {
                this.workPath = resolveWorkPath()
            }
            return this.workPath
        }

        String resolveWorkPath() {
            File fd = new File(this.getSlotPath())
            File[] fls = fd.listFiles()
            if (fls.length == 1 && fls[0].isDirectory()) {
                return fls[0].getAbsolutePath()
            }
            return this.getSlotPath()
        }

    }

    /**
     * Список всех слотов
     */
    NamedList<Slot> getSlots() {
        if (_slots == null) {
            _slots = loadSlots()
        }
        return _slots
    }

    private NamedList<Slot> loadSlots() {
        String d = wd(rootDir)
        if (!UtFile.exists(d)) {
            UtFile.mkdirs(d)
        }
        NamedList<Slot> res = new DefaultNamedList<>()
        File f = new File(d)
        for (f1 in f.listFiles()) {
            def slot = new Slot(f1.getAbsolutePath())
            res.add(slot)
        }
        res.sort()
        return res
    }

    /**
     * Создать новый слот
     */
    Slot createSlot() {
        XDateTime dt = UtDateTime.now()
        String dirName = dt.toString(dateFmt)
        String destDir = wd("${rootDir}/${dirName}")
        UtFile.mkdirs(destDir)
        _slots = null
        return new Slot(destDir)
    }

    /**
     * Создать новый слот и распаковать туда zip
     */
    Slot unpakZip(String zipFilename) {
        Slot slot = createSlot()
        ant.unzip(src: zipFilename, dest: slot.slotPath)
        slot.workPath = null // сбрасываем, определится автоматом
        return slot
    }

    void removeSlot(Slot slot) {
        if (UtFile.exists(slot.slotPath)) {
            _slots = null
            ant.delete(dir: slot.slotPath)
        }
    }

    /**
     * Удалить старые слоты.
     * @param countLastLeave сколько последних оставить
     */
    void removeOldSlots(int countLastLeave) {
        if (countLastLeave < 1) {
            countLastLeave = 1
        }
        def lst = getSlots()
        while (lst.size() > countLastLeave) {
            removeSlot(lst[0])
            lst.remove(0)
        }
    }

}
