<%@ page import="jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  /*

    Имитация action для получения json с задержками и генерацией ошибок

    Параметры:

    cnt - сколько записей возвращать
    pause - пауза перед ответов в msec
    error - true - генерировать ошибку после указанной паузы
  
   */
  BaseGsp th = this

  int cnt = th.request.params.getInt("cnt", 5)
  int pause = th.request.params.getInt("pause", 0)
  boolean error = th.request.params.getBoolean("error")

  def a = []
  for (i in 1..cnt) {
    a.add([id: i, text: "text-${i}"])
  }

  if (pause > 0) {
    Thread.currentThread().sleep(pause)
  }

  if (error) {
    throw new RuntimeException("error!")
  }

  out(UtJson.toJson(a))
%>
