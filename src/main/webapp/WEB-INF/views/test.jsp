<%--
  Created by IntelliJ IDEA.
  User: xixi
  Date: 2015/12/17
  Time: 22:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title></title>
</head>
<body>
<P>this test page</P>
  <%--<form method="post" action="http://7xpayt.com1.z0.glb.clouddn.com "--%>

<form method="post" action="http://up.qiniu.com"
        enctype="multipart/form-data">
   <%-- <input name="key" type="hidden" value="<resource_key>">--%>
    <input name="token" type="hidden" value="${token}">
    <input name="file" type="file" />
<%--
    <input name="crc32" type="hidden" value="true"/>
    <input name="accept" type="hidden" />
--%>

    <input type="submit">
  </form>

</body>
</html>
