package jandcode.jc.impl.just;

import groovy.util.*;
import jandcode.jc.*;
import org.apache.tools.ant.*;

import java.util.*;

/**
 * Кеш AntBuilder
 */
public class AntHolder implements IAnts {

    private Ctx ctx;
    private HashMap<String, AntBuilder> items = new HashMap<String, AntBuilder>();

    public AntHolder(Ctx ctx) {
        this.ctx = ctx;
    }

    /**
     * Возвращает кешированный экземпляр AntBuilder для указанного базового каталога
     */
    public AntBuilder getAnt(String basedir) {
        AntBuilder a = items.get(basedir);
        if (a == null) {
            a = new AntBuilder();
            a.getProject().setBasedir(basedir);
            // remove all prev loggers
            for (Object o : a.getProject().getBuildListeners()) {
                a.getProject().removeBuildListener((BuildListener) o);
            }
            // add new
            AntLogger logger = new AntLogger();
            if (ctx.getLog().isVerbose()) {
                logger.setMessageOutputLevel(org.apache.tools.ant.Project.MSG_INFO);
            } else {
                logger.setMessageOutputLevel(org.apache.tools.ant.Project.MSG_WARN);
            }
            logger.setOutputPrintStream(System.out);
            logger.setErrorPrintStream(System.err);
            a.getProject().addBuildListener(logger);
            //
            items.put(basedir, a);
        }
        return a;
    }

    /**
     * AntBuilder для этого каталога
     */
    public AntBuilder getAnt(Dir dir) {
        return getAnt(dir.getPath());
    }

}