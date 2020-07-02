<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 17/12/2018
  Time: 17:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Link to file</title>
</head>
<body>
<s:form action="insertDropbox">
    <s:select list="musicas" label="Musicas" name="idmusic"></s:select><br><br>
    <s:select list="files" label="Ficheiros" name="idficheiro"></s:select>
    <s:submit />
</s:form>
</body>
</html>
