package jandcode.jc.std;

/**
 * Интерфейс для проектов, которые хотят внедрить код в процесс prepare.
 */
public interface IPrepareProject {

    /**
     * Метод вызывается в команде prepare после того, как все обработчики
     * событий {@link PrepareProject.Event_Prepare} отработали.
     */
    void prepareProject();

}
