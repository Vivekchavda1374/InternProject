<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDateTime" %>

<html>
<head>
    <title>JSP Syntax Demo</title>
</head>

<body>

<h2>Basic JSP Scriptlet</h2>

<%
    out.println("Your IP address is " + request.getRemoteAddr() + "<br>");
    out.println("Your remote user is " + request.getRemoteUser() + "<br>");
%>

<h2>Hello World</h2>

<hr>

<h3>JSP Expression</h3>
<p>Today's date: <%= LocalDateTime.now() %></p>

<hr>

<h3>JSP Declaration & Variables</h3>

<%!
int a, b, c;
%>

<%
    a = 10;
    b = 20;
    c = 30;
%>

<p>Values: a=<%= a %>, b=<%= b %>, c=<%= c %></p>

<hr>

<h3>IF–ELSE Example</h3>

<%!
    int day = 7;
%>

<%
    if (day == 1 || day == 7) {
%>
<p>Today is weekend</p>
<%
} else {
%>
<p>Today is not weekend</p>
<%
    }
%>

<hr>

<h3>SWITCH–CASE Example</h3>

<%
    switch (day) {
        case 0:
            out.println("It's Sunday.");
            break;
        case 1:
            out.println("It's Monday.");
            break;
        case 2:
            out.println("It's Tuesday.");
            break;
        case 3:
            out.println("It's Wednesday.");
            break;
        case 4:
            out.println("It's Thursday.");
            break;
        case 5:
            out.println("It's Friday.");
            break;
        default:
            out.println("It's Saturday.");
    }
%>

<hr>

<h3>FOR LOOP Example</h3>

<%!
    int fontSize;
%>

<%
    for (fontSize = 1; fontSize <= 5; fontSize++) {
%>
<font color="green" size="<%= fontSize %>">
    JSP Tutorial
</font><br>
<%
    }
%>

</body>
</html>
