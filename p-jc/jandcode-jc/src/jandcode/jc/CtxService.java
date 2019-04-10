package jandcode.jc;

import jandcode.jc.impl.*;

/**
 * Базовый класс для реализации сервисов cxt.
 * Необходимо перекрыть метод onCreate, который будет выполнен при создании
 * экземпляра сервиса.
 */
public abstract class CtxService extends CtxServiceImpl implements ICtxService {
}
