package jandcode.core.web;

/**
 * Интерфейс для классов, которые хотят знать про конекст запроса,
 * который их создал.
 */
public interface IRequestContextLinkSet {

    void setRequestContext(RequestContext requestContext);

}
