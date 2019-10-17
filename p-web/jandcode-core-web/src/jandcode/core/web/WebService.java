package jandcode.core.web;

import jandcode.core.*;
import jandcode.core.web.action.*;
import jandcode.core.web.filter.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.render.*;
import jandcode.core.web.type.*;
import jandcode.core.web.virtfile.*;

import javax.servlet.http.*;

/**
 * Сервис для поддержки web-приложения
 */
public interface WebService extends Comp,
        IActionService, IRenderService, ITypeService, IVirtFileService, IGspService,
        IFilterService {

    /**
     * Связать с сервлетом. Вызывается из сервлета при его инициализации.
     */
    void setHttpServlet(HttpServlet servlet);

    /**
     * Связанный сервлет.
     */
    HttpServlet getHttpServlet();

    /**
     * Обработка запроса. Вызывается из сервлета.
     */
    void handleRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Throwable;

    /**
     * Текущий запрос для потока.
     * Если поток выполняется вне http-запроса, то метод возвращает
     * dummy-request.
     */
    Request getRequest();

}
