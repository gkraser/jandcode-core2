package jandcode.jc;

/**
 * Сервис для ctx.
 * Сервисы существуют в одном экземпляре и создаются (или возвращаются) по запросу:
 * ctx.bean(CLASS)
 */
public interface ICtxService {

    /**
     * Контекст
     */
    Ctx getCtx();


}
