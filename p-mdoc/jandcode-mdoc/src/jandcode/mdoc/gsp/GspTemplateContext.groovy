package jandcode.mdoc.gsp

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.mdoc.*
import jandcode.mdoc.builder.*
import jandcode.mdoc.groovy.*
import jandcode.mdoc.topic.*

/**
 * Контекст генерации шаблона
 */
class GspTemplateContext extends BaseGspTemplateContext {

    private OutBuilder builder
    private String topicBody
    private String pageTitle
    private String contentTemplate
    private OutFile outFile

    GspTemplateContext(GroovyFactory factory, OutFile outFile, OutBuilder builder) {
        this.setFactory(factory)
        this.outFile = outFile
        this.builder = builder
    }

    ////// env

    /**
     * builder
     */
    OutBuilder getBuilder() {
        return builder
    }

    /**
     * Куда выводим
     */
    OutFile getOutFile() {
        return outFile
    }

    /**
     * Документ
     */
    Doc getDoc() {
        return builder.getDoc()
    }

    /**
     * Статья. Может отсутсвовать, если генерится не статья
     */
    Topic getTopic() {
        return outFile.getTopic()
    }

    /**
     * Тело статьи в виде подготовленного для вывода html
     */
    String getTopicBody() {
        if (topicBody == null) {
            if (topic == null) {
                return ""
            } else {
                return topic.getBody()
            }
        }
        return topicBody
    }

    void setTopicBody(String topicBody) {
        this.topicBody = topicBody
    }

    /**
     * Заголовок страницы
     */
    String getPageTitle() {
        if (!UtString.empty(pageTitle)) {
            return pageTitle
        }
        if (topic != null) {
            return topic.getTitle()
        }
        return ""
    }

    void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle
    }

    /**
     * Заголовок документа
     */
    String getDocTitle() {
        return doc.getCfg().getTitle()
    }

    /**
     * Формирование ссылки-маркера, который будет заменен на реальную ссылку
     * при окончательной генерации
     * @param path куда(файл,статья), возможно с section
     * @return текст ссылки
     */
    String ref(String path) {
        return MDocConsts.MARK_REF_PREFIX + path + MDocConsts.MARK_REF_SUFFIX;
    }

    /**
     * Формирование ссылки-маркера на элемент содержания
     */
    String ref(Toc toc) {
        if (toc == null) {
            return ""
        }

        Topic topic = toc.getTopic()

        String s = ""

        if (topic != null) {
            s = topic.getId()
        }

        if (!UtString.empty(toc.getSection())) {
            s = s + "#" + toc.getSection()
        }

        return ref(s)
    }

    ////// gsp

    /**
     * Шаблон для вывода контента страницы
     */
    String getContentTemplate() {
        return contentTemplate
    }

    void setContentTemplate(String contentTemplate) {
        this.contentTemplate = contentTemplate
    }

    ////// system

    String generate(String path) {
        if (UtString.empty(path)) {
            throw new XError("Template path not defined")
        }
        // шаблоны помещаем в список зависимых
        getOutFile().addDependFile(path)

        return super.generate(path)
    }
}
