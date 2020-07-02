<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 17/12/2018
  Time: 16:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Share</title>

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
<s:form action="sharelink">
    <s:select list="files" label="Ficheiros" name="idficheiro"></s:select>
    <s:textfield label="Email" name="text"></s:textfield>
    <s:submit />
</s:form>
</body>
</html>
