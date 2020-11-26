<%@ page import="jandcode.core.apx.web.utils.framemenu.*; jandcode.core.jsa.jsmodule.*; jandcode.core.jsa.utils.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  /*
      Генерация javascript определения routes и menu для папки с vue-фреймами.
      Используется только внутри генератора js-модуля типа mainmenu.js.gsp.

      Параметры:
      
        path
          путь до папки с фреймами. Если путь относительный,
          то он рассматривается относительно корневой gsp

   */

  BaseGsp th = this

  GspArgsUtils ar = new GspArgsUtils(th)
  String path = ar.getString("path", true);
  if (UtVDir.isRelPath(path)) {
    path = th.context.rootGsp.path(path)
  }

  def b = new FrameMenuBuilder(th.app)
  b.addFolder(path)

  String json_routes = UtJson.toJson(b.routes)
  String json_menu = UtJson.toJson(b.menu)

  if (th.app.env.dev) {
    def mb = th.inst(JsModuleBuilder)
    for (f in b.usingFiles) {
      mb.addModifyDepend(f.path)
    }
  }
%>
module.exports = {
  routes: ${json_routes},
  menu: ${json_menu}
}
