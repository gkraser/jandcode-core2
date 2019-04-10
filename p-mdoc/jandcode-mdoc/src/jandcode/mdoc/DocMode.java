package jandcode.mdoc;

/**
 * Режимы, в которых работает документ
 */
public class DocMode {

    private boolean serve;
    private boolean debug;

    /**
     * Работает web-сервер
     */
    public boolean isServe() {
        return serve;
    }

    public void setServe(boolean serve) {
        this.serve = serve;
    }

    /**
     * Отладочный режим
     */
    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * isServe() && isDebug()
     */
    public boolean isServeDebug() {
        return isServe() && isDebug();
    }

}
