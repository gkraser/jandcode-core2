package jandcode.jc;

import jandcode.jc.impl.gsp.*;

/**
 * Скрипт, которым представлен gsp-шаблон
 */
public abstract class GspScript extends GspScriptImpl implements Project, IProjectScript, IGspScript {

    /**
     * Метод представляет собой тело gsp-файла
     */
    protected abstract void onGenerate() throws Exception;

}
