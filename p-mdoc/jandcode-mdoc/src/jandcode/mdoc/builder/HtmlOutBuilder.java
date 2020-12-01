package jandcode.mdoc.builder;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.simxml.*;
import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.cm.*;
import jandcode.mdoc.gsp.*;
import jandcode.mdoc.html.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;

import java.util.*;

/**
 * Стандартный html builder.
 * Генерирует многофайловую документацию. Одна статья-один файл.
 */
public class HtmlOutBuilder extends BaseOutBuilder {

    private HtmlTopicTextGeneratorFactory genFact;

    public void configure(Doc doc) throws Exception {
        super.configure(doc);
        //
        BeanFactory bf = getBeanFactory();

        // постпроцессоры
        bf.registerBean(CmPostProcessor.class.getName(), CmPostProcessor.class);
        bf.registerBean(RefPostProcessor.class.getName(), RefPostProcessor.class);
        bf.registerBean(ImgRefPostProcessor.class.getName(), ImgRefPostProcessor.class);

        // сервисы
        bf.registerBean(CmService.class.getName(), CmService.class);

        // команды для CmService описываются в module.cfx (mdoc/cm)
    }

    protected void onBuild() throws Exception {
        // сокращалки
        OutFileHolder outFiles = getOutFiles();

        // содержание
        String tocFile = getDoc().getCfg().getToc();
        if (UtString.empty(tocFile)) {
            // файла с содержанием нет
            MDocLogger.getInst().warn("Не указан файл с содержанием в свойстве документа toc, будет сгенерировано");

            String xmlText = "" +
                    "<root>" +
                    "  <toc-root>" +
                    "    <toc topic='**/*' type='auto'/>" +
                    "  </toc-root>" +
                    "</root>";
            SimXml xml = new SimXmlNode();
            xml.load().fromString(xmlText);
            XmlTocBuilder tb = new XmlTocBuilder(this, xml);
            Toc toc = tb.buildToc();
            setToc(toc);
        } else {
            SourceFile tf = getDoc().getSourceFiles().get(tocFile);
            try {
                SimXml xml = new SimXmlNode();
                xml.load().fromString(tf.getText());
                XmlTocBuilder tb = new XmlTocBuilder(this, xml);
                Toc toc = tb.buildToc(UtFile.path(tf.getPath()));
                setToc(toc);
            } catch (Exception e) {
                throw new XErrorMark(e, "файл: " + tf.getRealPath());
            }
        }

        // помещаем все файлы в выход для начала
        for (SourceFile sourceFile : getDoc().getSourceFiles()) {
            OutFile outFile = new OutFile(sourceFile.getPath(), sourceFile, null);
            outFiles.add(outFile);
        }

        // генерим все статьи
        this.genFact = new HtmlTopicTextGeneratorFactory();
        for (Topic topic : getDoc().getTopics()) {

            HtmlTopicTextGeneratorFactory.Generator generator = this.genFact.createGenerator();

            // html-файл с текстом статьи, но пока без текста
            String path = UtFile.removeExt(topic.getSourceFile().getPath()) + ".html";
            TextSourceFile sourceFile = new TextSourceFile(path, generator);

            // выходной файл
            OutFile outFile = new OutFile(sourceFile.getPath(), sourceFile, topic);
            outFile.setNeed(true);

            // связываем с outFile
            generator.setOutFile(outFile);

            //
            outFiles.add(outFile);
        }

        // теперь имеются все файлы
        List<String> ignoreFileMasks = Arrays.asList(
                "**/*.class",
                "**/*.java",
                "**/*.groovy",
                "**/*.gsp",
                "_theme/**/*.md"
        );
        List<OutFile> tmp = new ArrayList<>(outFiles.getItems());
        for (OutFile f : tmp) {
            String pathSrc = f.getSourceFile().getPath();

            // игнорируемые файлы
            boolean ignored = false;
            for (String m : ignoreFileMasks) {
                if (UtVDir.matchPath(m, pathSrc)) {
                    ignored = true;
                    break;
                }
            }
            if (ignored) {
                continue;
            }

            // файлы темы
            if (UtVDir.matchPath("_theme/**/*", pathSrc)) {
                f.setNeed(true);
                outFiles.move(f, pathSrc.replace("_theme/", "_data/theme/"));
                continue;
            }

        }
        OutFile indexOutFile = outFiles.findByTopicId(getToc().getTopic().getId());
        if (indexOutFile == null) {
            throw new XError("Корневой элемент содержания не найден в выходных файлах, что очень странно");
        }
        outFiles.move(indexOutFile, "index.html");

        // исходные файлы статей тоже выводим
        for (Topic topic : getDoc().getTopics()) {
            String pathSrc = topic.getSourceFile().getPath();
            OutFile f = outFiles.findBySourcePath(pathSrc);
            if (f != null) {
                f.setNeed(true);
                outFiles.move(f, UtVDir.join("_data/source/", pathSrc) + ".txt");
            }
        }

        // содержание в json
        String tocJson_fn = "toc.js";
        TextSourceFile tocJson_sourceFile = new TextSourceFile(tocJson_fn, () -> {
            return new TocJsonUtils(this).makeTocJsStr("mdoc.tocAll", getToc());
        });
        OutFile tocJson_outFile = new OutFile(tocJson_fn, tocJson_sourceFile, null);
        tocJson_outFile.setNeed(true);
        outFiles.add(tocJson_outFile);

        //////
    }

    /**
     * Создать OutFile на лету
     *
     * @param topicFilePath для какого фиктивного пути статьи
     * @param topicContent  текст статьи
     * @return экземпляр OutFile
     */
    public OutFile createOutFile(String topicFilePath, String topicContent) {

        HtmlTopicTextGeneratorFactory.Generator generator = genFact.createGenerator();

        Topic topic = DocUtils.createTopic(getDoc(), topicFilePath, topicContent);

        // html-файл с текстом статьи, но пока без текста
        String path = UtFile.removeExt(topic.getSourceFile().getPath()) + ".html";
        TextSourceFile sourceFile = new TextSourceFile(path, generator);

        // выходной файл
        OutFile outFile = new OutFile(sourceFile.getPath(), sourceFile, topic);

        // связываем с outFile
        generator.setOutFile(outFile);

        return outFile;
    }


    /**
     * Генерируем текст статьи
     */
    private String generateTopicHtml(OutFile outFile, String topicBody) {
        GspTemplateContext ctx = createGspTemplateContext(outFile);
        ctx.setTopicBody(topicBody);
        String contentTemplate = outFile.getTopic().getProps().getString("contentTemplate", "_theme/topic-body.gsp");
        ctx.setContentTemplate(contentTemplate);
        return ctx.generate("_theme/page.gsp");
    }

    class HtmlTopicTextGeneratorFactory {

        List<HtmlPostProcessor> htmlPostProcessors;
        UrlRewriterPostProcessor rwt;

        class Generator implements ITextGenerator {

            OutFile outFile;

            public void setOutFile(OutFile outFile) {
                this.outFile = outFile;
            }

            public String generateText() throws Exception {
                Topic topic = this.outFile.getTopic();

                // обрабатываем html текст тела статьи
                String html = topic.getBody();
                for (HtmlPostProcessor postProcessor : htmlPostProcessors) {
                    html = postProcessor.process(html, this.outFile);
                }

                // генерируем html-файл статьи
                html = generateTopicHtml(this.outFile, html);

                // делаем url-rewrite
                html = rwt.process(html, this.outFile);

                return html;
            }
        }

        public HtmlTopicTextGeneratorFactory() {
            this.htmlPostProcessors = getBeanFactory().impl(HtmlPostProcessor.class);
            rwt = create(UrlRewriterPostProcessor.class);
        }

        Generator createGenerator() {
            return new Generator();
        }

    }

}
