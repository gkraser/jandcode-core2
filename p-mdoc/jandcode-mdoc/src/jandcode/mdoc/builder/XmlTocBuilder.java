package jandcode.mdoc.builder;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.simxml.*;
import jandcode.mdoc.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;
import jandcode.mdoc.topic.impl.*;

import java.util.*;

/**
 * Построение из xml
 */
public class XmlTocBuilder {

    private OutBuilder outBuilder;
    private SimXml xml;
    private Toc root;
    private Set<String> usedTopics = new HashSet<>();
    private List<Include> includes = new ArrayList<>();
    private int dummyCnt;
    private boolean createDummyTopics = true;
    private String basePath = "";

    public XmlTocBuilder(OutBuilder outBuilder, SimXml xml) {
        this.outBuilder = outBuilder;
        this.xml = xml;
    }

    public XmlTocBuilder(OutBuilder outBuilder, SimXml xml, boolean createDummyTopics) {
        this(outBuilder, xml);
        this.createDummyTopics = createDummyTopics;
    }

    public Toc buildToc() {
        return buildToc("");
    }

    public Toc buildToc(String basePath) {
        if (UtString.empty(basePath)) {
            this.basePath = "";
        } else {
            this.basePath = UtVDir.normalize(basePath);
        }
        this.root = createToc();
        this.root.setName("root");

        parseXml();
        removeEmptyToc(this.root);
        checkTocTopics(this.root, true);

        validateNotUsed();

        return root;
    }

    ////// internal

    class Include {
        private SimXml xml;
        private Toc ownerToc;
        private Toc afterToc;

        Include(SimXml xml, Toc ownerToc, Toc afterToc) {
            this.xml = xml;
            this.ownerToc = ownerToc;
            this.afterToc = afterToc;
        }

        /**
         * Добавить элемент в ownerToc после afterToc
         */
        void insert(Toc toc) {
            toc.setOwner(ownerToc);
            if (afterToc == null) {
                ownerToc.getChilds().add(toc);
            } else {
                int idx = ownerToc.getChilds().indexOf(afterToc);
                if (idx == -1) {
                    ownerToc.getChilds().add(toc);
                    afterToc = null;  // он не найден, далее искать безполезно
                } else {
                    ownerToc.getChilds().add(idx + 1, toc);
                    afterToc = toc;  // остальное будем вставлять после нового
                }
            }
        }

    }

    /**
     * Создать пустой элемент
     */
    private Toc createToc() {
        return new TocImpl();
    }

    /**
     * Пометить статью, как использованнуюв содержании
     */
    private void markUsed(Topic topic) {
        this.usedTopics.add(topic.getId());
    }

    private boolean isUsed(Topic topic) {
        return this.usedTopics.contains(topic.getId());
    }

    /**
     * Получить статью и пометить как использованную
     */
    private Topic getTopic(String id) {
        if (UtVDir.isRelPath(id)) {
            id = UtVDir.expandRelPath(this.basePath, id);
        }

        Topic topic = outBuilder.getDoc().getTopics().find(id);
        if (topic == null) {
            throw new XError("Статья не найдена: {0}", id);
        }
        if (isUsed(topic)) {
            throw new XError("Статья уже ипользовалась: {0}", topic.getId());
        }
        markUsed(topic);
        return topic;
    }

    private String getDummyTopicFileName() {
        dummyCnt++;
        return "x/x" + dummyCnt + ".md";
    }

    private void checkTocTopics(Toc root, boolean checkSelf) {
        if (!createDummyTopics) {
            return;
        }
        if (checkSelf && root.getTopic() == null) {
            String title = root.getTitle();
            if (UtString.empty(title)) {
                title = (root.getName().equals("") ? "no-name" : root.getName()) + " (folder)";
            }
            Topic newTopic = DocUtils.addTopic(
                    outBuilder.getDoc(),
                    getDummyTopicFileName(),
                    "# " + title
            );
            markUsed(newTopic);
            root.setTopic(newTopic);
        }
        for (Toc ch : root.getChilds()) {
            checkTocTopics(ch, true);
        }
    }

    /**
     * Удалить те, которые не имеют статей и не имеют дочерних.
     */
    private void removeEmptyToc(Toc root) {
        List<Toc> forDelete = new ArrayList<>();
        for (Toc ch : root.getChilds()) {
            if (ch.getTopic() == null && ch.getChilds().size() == 0) {
                forDelete.add(ch);
            }
        }
        if (forDelete.size() > 0) {
            for (Toc ch : forDelete) {
                root.getChilds().remove(ch);
            }
        }
    }

    private void validateNotUsed() {
        for (Topic t : outBuilder.getDoc().getTopics()) {
            if (!isUsed(t)) {
                MDocLogger.getInst().warn("Статья " + t.getSourceFile().getPath() + " не попала в содержание");
            }
        }
    }

    ////// xml

    private void parseXml() {

        // ищем toc-root
        SimXml xTocRoot = null;
        for (SimXml x : xml.getChilds()) {
            if (x.hasName("toc-root")) {
                if (xTocRoot != null) {
                    throw new XError("Должен быть только один элемент toc-root");
                }
                xTocRoot = x;
            } else {
                throw new XError("Недопустимый элемент: {0}", x.getName());
            }
        }
        if (xTocRoot == null) {
            throw new XError("Не найден элемент: toc-root");
        }

        // загружаем
        applyXml(this.root, xTocRoot);

        // include
        handleIncludes();
    }

    private void applyXml(Toc toc, SimXml x) {

        String s;

        // статья
        s = x.getString("topic");
        if (!UtString.empty(s)) {
            Topic topic = getTopic(s);
            toc.setTopic(topic);
        }

        // title
        s = x.getString("title");
        if (!UtString.empty(s)) {
            toc.setTitle(s);
        }

        // titleShort
        s = x.getString("titleShort");
        if (!UtString.empty(s)) {
            toc.setTitle(s);
        }

        Toc last = null;
        for (SimXml x1 : x.getChilds()) {
            if (x1.hasName("toc")) {
                String p_topic = x1.getString("topic");
                String p_title = x1.getString("title");
                if (UtString.empty(p_topic) && UtString.empty(p_title)) {
                    throw new XError("Нужно указать атрибут topic или title (или то и другое)");
                }
                if (UtVDir.isRelPath(p_topic)) {
                    // относительный путь
                    Topic ownTopic = toc.getTopic();
                    if (ownTopic != null) {
                        p_topic = UtVDir.expandRelPath(UtFile.path(ownTopic.getId()), p_topic);
                    } else {
                        p_topic = UtVDir.expandRelPath(this.basePath, p_topic);
                    }
                    x1.setValue("topic", p_topic);
                }
                if (p_topic.indexOf("*") == -1) {
                    // явный дочерний, возможно без статьи, только с заголовком
                    Toc t1 = createToc();
                    toc.addChild(t1);
                    last = t1;
                    applyXml(t1, x1);
                } else {
                    // набор дочерних
                    if (x1.hasChilds()) {
                        throw new XError("toc с маской не должен иметь дочерних");
                    }
                    Include inc = new Include(x1, toc, last);
                    this.includes.add(inc);
                }

            } else if (x1.hasName("include")) {
                String p_tocFile = x1.getString("toc");
                if (UtString.empty(p_tocFile)) {
                    throw new XError("Нужно указать атрибут toc для include");
                }
                SourceFile tocFile = outBuilder.getDoc().getSourceFiles().find(p_tocFile);
                if (tocFile == null) {
                    throw new XError("Не найден файл: {0}", p_tocFile);
                }
                SimXml tocXml = new SimXmlNode();
                try {
                    tocXml.load().fromString(tocFile.getText(), p_tocFile);
                } catch (Exception e) {
                    throw new XErrorWrap(e);
                }
                String saveBasePath = this.basePath;
                this.basePath = UtFile.path(tocFile.getPath());
                try {
                    for (SimXml ch : tocXml.getChilds()) {
                        Toc t1 = createToc();
                        toc.addChild(t1);
                        applyXml(t1, ch);
                    }
                } catch (Exception e) {
                    throw new XErrorMark(e, "файл: " + p_tocFile);
                } finally {
                    this.basePath = saveBasePath;
                }

            } else {
                throw new XError("Недопустимый элемент: {0}", x1.getName());
            }
        }

    }

    private void handleIncludes() {
        for (Include inc : this.includes) {
            handleInclude(inc);
        }
    }

    private void handleInclude(Include inc) {
        String p_mask = inc.xml.getString("topic");
        if (UtString.empty(p_mask)) {
            return;
        }

        String p_type = inc.xml.getString("type", "auto");

        // выбираем статьи, которые удовлетворяют маске и не использовались еще
        List<Topic> topics = new ArrayList<>();
        for (Topic t : outBuilder.getDoc().getTopics()) {
            if (MDocConsts.INDEX.equals(t.getId())) {
                if (p_type.equals("auto")) {
                    continue; //index в корне игнорируем для auto, к нему особое отношение
                }
            }
            if (UtVDir.matchPath(p_mask, t.getId())) {
                if (!isUsed(t)) {
                    markUsed(t);
                    topics.add(t);
                }
            }
        }

        if (topics.size() == 0) {
            return; // так вполне может быть
        }

        if (p_type.equals("plain")) {
            handleInclude_plain(inc, topics);

        } else if (p_type.equals("files")) {
            handleInclude_files(inc, topics);

        } else if (p_type.equals("auto")) {
            handleInclude_auto(inc, topics);

        } else {
            throw new XError("Значение атрибута неправильное: type={0}", p_type);
        }
    }

    /**
     * Список дочерних без папок
     */
    private void handleInclude_plain(Include inc, List<Topic> topics) {
        topics.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        for (Topic t : topics) {
            Toc toc = createToc();
            toc.setTopic(t);
            inc.insert(toc);
        }
    }

    /**
     * Авто содержание по файлам
     */
    private void handleInclude_files(Include inc, List<Topic> topics) {
        //
        Toc tmpRoot = createToc();
        for (Topic t : topics) {
            Toc toc = createToc();
            toc.setTopic(t);

            String tocPath = t.getId();

            String[] ar = tocPath.split("/");
            Toc cur = tmpRoot;
            for (int i = 0; i < ar.length; i++) {
                cur = findTocByName(cur, ar[i]);
            }
            cur.setTopic(t);
        }

        // сжимаем
        compactToc(tmpRoot);

        // расстановка отсутсвующих статей
        checkTocTopics(tmpRoot, false);

        sortTocAsFiles(tmpRoot);

        //
        for (Toc t : tmpRoot.getChilds()) {
            inc.insert(t);
        }
    }

    /**
     * Авто содержание по файлам
     */
    private void handleInclude_auto(Include inc, List<Topic> topics) {
        //
        Toc tmpRoot = createToc();
        for (Topic t : topics) {
            Toc toc = createToc();
            toc.setTopic(t);

            String path = UtFile.path(t.getId());
            String filename = UtFile.filename(t.getId());
            String tocPath = t.getId();

            if (MDocConsts.INDEX.equals(filename)) {
                tocPath = path;
            }

            String[] ar = tocPath.split("/");
            Toc cur = tmpRoot;
            for (int i = 0; i < ar.length; i++) {
                cur = findTocByName(cur, ar[i]);
            }
            if (cur.getTopic() != null) {
                MDocLogger.getInst().warn("Дублирование статьи: " + t.getSourceFile().getPath() + " и " + cur.getTopic().getSourceFile().getPath());
                continue;
            }
            cur.setTopic(t);
        }

        // сжимаем
        compactToc(tmpRoot);

        // расстановка отсутсвующих статей
        checkTocTopics(tmpRoot, false);

        sortTocByTitle(tmpRoot);

        //
        for (Toc t : tmpRoot.getChilds()) {
            inc.insert(t);
        }
    }

    /**
     * Сделать содержание более компактным.
     * Убираем элементы верхнего уровня без статей.
     */
    private void compactToc(Toc root) {
        Toc cur = root;
        while (true) {
            if (cur.getChilds().size() == 1 && cur.getChilds().get(0).getTopic() == null) {
                cur = cur.getChilds().get(0);
            } else {
                break;
            }
        }
        if (root == cur) {
            return; // нечего сжимать
        }
        root.getChilds().clear();
        for (Toc t : cur.getChilds()) {
            t.setOwner(root);
            root.getChilds().add(t);
        }
    }

    /**
     * Найти по имени и создать, если нету
     */
    private Toc findTocByName(Toc toc, String name) {
        for (Toc t : toc.getChilds()) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        // autocreate
        Toc t = createToc();
        t.setOwner(toc);
        t.setName(name);
        toc.getChilds().add(t);
        return t;
    }

    private void sortTocByTitle(Toc toc) {
        if (toc.getChilds().size() > 0) {
            toc.getChilds().sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
            //
            for (Toc ch : toc.getChilds()) {
                sortTocByTitle(ch);
            }
        }
    }

    private void sortTocAsFiles(Toc toc) {
        if (toc.getChilds().size() > 0) {
            toc.getChilds().sort((a, b) -> {
                // папки вверх
                String sa = a.getName();
                String sb = b.getName();
                if (a.getTopic() == null) {
                    sa = "__" + sa;
                }
                if (b.getTopic() == null) {
                    sb = "__" + sb;
                }
                return sa.compareToIgnoreCase(sb);
            });
            //
            for (Toc ch : toc.getChilds()) {
                sortTocAsFiles(ch);
            }
        }
    }
}
