package jandcode.jc;

import jandcode.jc.impl.*;

/**
 * Базовый класс для реализации скриптов для проектов.
 * Перекрываемые методы:
 * onCreate - выполняется при создании объекта через create и при включении скрипта
 * через include, onInclude - при включении скрипта
 * через include.
 */
public abstract class ProjectScript extends ProjectScriptImpl implements Project, IProjectScript {
}
