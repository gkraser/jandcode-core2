package jandcode.web;

/**
 * Интерфейс запускалки web-приложения для {@link WebRunMain}
 */
public interface IWebRunner {

    void setPort(int port);

    void setContext(String context);

    void start() throws Exception;

}
