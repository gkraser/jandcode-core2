package jandcode.mdoc.builder;

import jandcode.commons.*;
import jandcode.mdoc.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

public class RefResolver extends BaseOutBuilderMember {

    /**
     * Распознование ссылки
     *
     * @param s       текст ссылки
     * @param outFile внутри какого файла встретилась ссылка
     * @return null, если ссылка не распознана
     */
    public Ref resolveRef(String s, OutFile outFile) {
        Topic topic = outFile.getTopic();
        if (topic == null) {
            return null;
        }

        if (s == null) {
            return null;
        }
        s = s.trim();
        if (UtString.empty(s)) {
            return null;
        }

        Ref ref = resolveRefInternal(s, outFile);
        if (ref == null) {
            // не найдена
            if (!s.startsWith("/") && !s.contains("./")) {
                // пробуем абсолютную найти
                return resolveRefInternal("/" + s, outFile);
            }
        }
        return ref;
    }

    /**
     * Распознование ссылки
     *
     * @param s       текст ссылки
     * @param outFile внутри какого файла встретилась ссылка
     * @return null, если ссылка не распознана
     */
    protected Ref resolveRefInternal(String s, OutFile outFile) {
        OutBuilder builder = getOutBuilder();

        Topic topic = outFile.getTopic();
        Doc doc = builder.getDoc();

        if (s.startsWith("#")) {
            // ссылка на секцию внутри статьи
            return new Ref(topic, s.substring(1));
        }

        // выявляем секцию
        String topicSection = null;
        int a = s.indexOf('#');
        if (a != -1) {
            topicSection = s.substring(a + 1);
            s = s.substring(0, a);
        }

        if (s.startsWith("/")) {
            // абсолютный путь
            s = UtVDir.normalize(s);
        } else {
            // относительный
            if (!s.startsWith("./")) {
                s = "./" + s;
            }
            String topicDir = UtFile.path(topic.getSourceFile().getPath());
            s = UtVDir.expandRelPath(topicDir, s);
        }

        // пустой путь не нужен
        if (UtString.empty(s)) {
            return null;
        }

        //
        String ext = UtFile.ext(s);

        if (UtString.empty(ext)) {
            // расширения нет, ищем статью
            Topic destTopic = doc.getTopics().find(s);
            if (destTopic == null) {
                return null;
            }
            return new Ref(destTopic, topicSection);
        } else {
            // расширение есть, ищем файл
            SourceFile f = doc.getSourceFiles().find(s);
            if (f == null) {
                return null;
            }
            // файл есть. Статья ли это?
            Topic destTopic = doc.getTopics().find(f.getPath());
            if (destTopic != null) {
                return new Ref(destTopic, topicSection);
            } else {
                return new Ref(f);
            }
        }

    }

    /**
     * Распознование ссылки для include-файлов.
     * Такие файлы запрашиваются из текста статьи, например для include или
     * еще какой обработки.
     * Если файл не начинается с '/', то он ищется сначала
     * в _inc/TOPICFILENAME, _inc/TOPICFILENAME (с заменой '-' на '_')
     * затем в _inc. Если не найжен, то ресолвится как в
     * {@link RefResolver#resolveRef(java.lang.String, OutFile)}
     *
     * @param s       текст ссылки
     * @param outFile внутри какого файла встретилась ссылка
     * @return null, если ссылка не распознана
     */
    public Ref resolveRefInc(String s, OutFile outFile) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (UtString.empty(s)) {
            return null;
        }
        Ref ref = null;

        if (!s.startsWith("/") && outFile.getTopic() != null) {
            SourceFile topicFile = outFile.getTopic().getSourceFile();
            String tfName = UtFile.removeExt(UtFile.filename(topicFile.getPath()));

            String s1 = UtVDir.join("_inc", tfName, s);
            ref = resolveRef(s1, outFile);
            if (ref == null) {
                s1 = UtVDir.join("_inc", tfName.replace("-", "_"), s);
                ref = resolveRef(s1, outFile);
                if (ref == null) {
                    s1 = UtVDir.join("_inc", s);
                    ref = resolveRef(s1, outFile);
                }
            }
        }

        if (ref == null) {
            return resolveRef(s, outFile);
        } else {
            return ref;
        }
    }

    /**
     * Локальная ли ссылка, т.е. может ли потенциально указыватт куда-то
     * внутри документа.
     *
     * @param s текст ссылки
     * @return true - локальная
     */
    public boolean isRefLocal(String s) {
        if (s == null) {
            return false;
        }
        return !s.contains(":");
    }
}
