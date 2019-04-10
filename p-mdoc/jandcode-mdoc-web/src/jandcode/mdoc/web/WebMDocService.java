package jandcode.mdoc.web;

import jandcode.core.*;
import jandcode.mdoc.builder.*;

import java.util.*;

/**
 * Сервис поддержки работы web-приложения mdoc
 */
public interface WebMDocService extends Comp {

    /**
     * Регистрация builder с указаным именем
     */
    void registerBuilder(String name, OutBuilder builder);

    /**
     * Получить builder по имени
     */
    OutBuilder getBuilder(String name);

    /**
     * Регистрация приложения-документа
     */
    void registerApp(String name);

    List<String> getRegisterApps();
}
