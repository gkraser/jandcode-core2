package jandcode.xcore;

/**
 * Интерфейс для объектов, которые имеют собственное хранилище бинов.
 */
public interface IBeanFactoryOwner {

    /**
     * Ссылка на хранилище
     */
    BeanFactory getBeanFactory();

}
