<%--
  Created by IntelliJ IDEA.
  User: diogo
  Date: 14/12/2018
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Show details</title>
    <script type="text/javascript">

        var websocket = null;

        window.onload = function(){
            connect('wss://' + window.location.host + '/ws/' + document.getElementById('idUser').value );
        }

        function connect(host) { // connect to the host websocket
            if ('WebSocket' in window)
                websocket = new WebSocket(host);
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
                writeToHistory('Get a real browser which supports WebSocket.');
                return;
            }

            console.log(websocket);

            websocket.onopen    = onOpen; // set the 4 event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }

        function onOpen(event) {
            console.log(document.getElementById('idUser').value);
        }

        function onClose(event) {
            console.log("Bye");
        }

        function onMessage(message) {
            console.log(message.data);
            if((message.data).includes("Promoted")){
                document.getElementById('updatePermission').value = "true";
                alert("You have been promoted to editor");
                window.location = "/updateNow.action";
            }
            if((message.data).includes("Rate")){
                var up = document.getElementById('details');
                up.innerHTML = "";
                var line = document.createElement('p');
                line.style.wordWrap = 'break-word';
                line.innerHTML = message.data;
                up.appendChild(line);
                up.scrollTop = up.scrollHeight;
                return;
            }
            writeToHistory(message.data);
        }

        function doSend(message) {
            websocket.send(message); // send the message to the server
        }

        function onError(event) {
            console.log(event);
            console.log("Erro");
        }

        function writeToHistory(text) {
            console.log(text);
            var history = document.getElementById('history');
            var line = document.createElement('p');
            line.style.wordWrap = 'break-word';
            line.innerHTML = text;
            history.appendChild(line);
            history.scrollTop = history.scrollHeight;
        }

    </script>
</head>
<body>
<noscript>JavaScript must be enabled for WebSockets to work.</noscript>
<div>
    <div id="container"><div id="history"></div></div>
    <s:hidden id="idUser" value="%{#session.user.idUser}"/>
    <s:hidden id="updatePermission" value="%{#session.permission}"/>
</div>
<table>
    <s:div id="details">
        <s:iterator value="showD">
            <s:property></s:property><br>
        </s:iterator>
    </s:div>
    <c:choose>
        <c:when test="${session.permission == 'true'}">
            <s:form action="remove">
                <s:submit value="Remove"></s:submit>
            </s:form>
            <s:form action="execute">
                <s:submit value="edit" name="option"></s:submit>
            </s:form>
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${session.select == 'album'}">
            <s:form action="writereview">
                <s:textarea label="Review" name="review" cols="40" rows="10"/>
                <s:textfield label="Rate" name="rate"></s:textfield>
                <s:submit />
            </s:form>
        </c:when>
    </c:choose>
</table>
</body>
</html>
