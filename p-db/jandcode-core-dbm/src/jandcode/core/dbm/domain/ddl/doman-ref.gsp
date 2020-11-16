<%@ page import="jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.FieldDb; jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.FieldDb; jandcode.core.dbm.domain.db.*" %>
<%

  // генерация foreign key универсальная

  def ut = new jandcode.core.dbm.domain.db.DomainDbUtils(th)

  for (def d : ut.getDomainsMy()) {
    for (def f : d.fields) {
      def refDomain = ut.getDbRefDomain(f)
      if (refDomain == null) continue
      def fd = f.bean(jandcode.core.dbm.domain.db.FieldDb)
      def gname = ut.makeShortIdn("fk_${d.dbTableName}_${f.name}")
      def refCascade = fd.refCascade ? " on delete cascade" : ""
%>
--@${gname}
alter table ${d.name} add constraint ${gname}
foreign key(${f.name}) references ${refDomain.dbTableName}(id)${refCascade}
~~
<%
    }
  }
%>