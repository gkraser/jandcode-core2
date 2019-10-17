package jandcode.mdoc.flexmark.cm;

import com.vladsch.flexmark.util.ast.*;
import com.vladsch.flexmark.util.sequence.*;

/**
 * Команда
 */
public class CmBlock extends Block {

    private String text = "";

    public CmBlock() {
    }

    public BasedSequence[] getSegments() {
        return new BasedSequence[0];
    }

    public CmBlock(String text) {
        this.text = text;
    }

    /**
     * Текст команды
     */
    public String getText() {
        return text;
    }

}
