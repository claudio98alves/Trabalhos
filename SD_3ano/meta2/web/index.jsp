<%--
  Created by IntelliJ IDEA.
  User: diogo
  Date: 13/12/2018
  Time: 14:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="display" uri="/struts-tags" %>
<html>
<title>Menu</title>
<script type="text/javascript">

    var websocket = null;

    window.onload = function(){
        connect('wss://' + window.location.host + '/ws/' + document.getElementById('idUser').value );
        if(document.getElementById('url').value !== ""){
            if (window.confirm('If you click "ok" you would be redirected to Drobox Connection'))
            {
                window.location.href=document.getElementById('url').value;
            }
        }
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
    <s:hidden id="url" value="%{#session.url}"/>
    <s:hidden id="token" value="%{#session.USER_API_TOKEN}"/>
</div>
  <a href="<s:url action="execute" >
            <s:param name="option">search music</s:param>
        </s:url>">search music</a><br>

  <a href="<s:url action="execute" >
            <s:param name="option">show all</s:param>
        </s:url>">show all</a><br>

  <a href="<s:url action="logout" >
            <s:param name="option">log out</s:param>
        </s:url>">log out</a><br>

    <a href="<s:url action="connect" >
            <s:param name="option">connectwithdropbox</s:param>
        </s:url>">connect with dropbox</a><br>
    <a href="<s:url action="playmusic" >
            <s:param name="option">playmusic</s:param>
        </s:url>">Play a music</a><br>
    <a href="<s:url action="linktofilemusic" >
            <s:param name="option"></s:param>
        </s:url>">Link file to a music</a><br>
    <a href="<s:url action="sharefile" >
            <s:param name="option"></s:param>
        </s:url>">Share a file</a><br>


  <c:choose>
      <c:when test="${session.permission == 'true'}">
          <a> ---- Editor Permission ----</a><br>

          <a href="<s:url action="execute" >
                    <s:param name="option">artist</s:param>
                </s:url>">Insert Artist</a><br>

          <a href="<s:url action="execute" >
                    <s:param name="option">album</s:param>
                </s:url>">Insert Album</a><br>

          <a href="<s:url action="execute" >
                    <s:param name="option">music</s:param>
                </s:url>">Insert Music</a><br>

          <a href="<s:url action="execute" >
                    <s:param name="option">promote</s:param>
        </s:url>">promote</a><br>
      </c:when>
  </c:choose>

  </body>
</html>
