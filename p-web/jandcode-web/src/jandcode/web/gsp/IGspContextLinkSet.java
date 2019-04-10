package jandcode.web.gsp;

/**
 * Интерфейс для классов, которые хотят знать про контекст, который их создал.
 */
public interface IGspContextLinkSet {

    void setGspContext(GspContext gspContext);

}
