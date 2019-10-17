<%@ page import="jandcode.core.jsa.tst.test.utils.TstRunContext; jandcode.core.jsa.tst.test.utils.*; jandcode.core.web.gsp.*" %>
<jc:page template="./template-run.gsp">
  <%
    BaseGsp th = this
    def runCtx = th.inst(jandcode.core.jsa.tst.test.utils.TstRunContext)

    String m_run = "jandcode/core/jsa/tst/test/runJs.js"
  %>
  <jc:pagePart name="body">
    <jc:linkModule module="${m_run}"/>
    <script>
        require('${m_run}').runModule('${runCtx.main}')
    </script>
  </jc:pagePart>
</jc:page>
