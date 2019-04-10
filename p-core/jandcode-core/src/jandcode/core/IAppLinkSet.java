package jandcode.core;

/**
 * Интерфейс для объектов, которые желают знать, какое приложение их
 * создало.
 */
public interface IAppLinkSet {

    void setApp(App app);

}
