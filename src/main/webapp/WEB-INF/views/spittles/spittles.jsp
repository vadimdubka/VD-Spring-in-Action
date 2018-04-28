<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Spittles</title>
    </head>
    <body>
        <c:forEach var="spittle" items="${spittleList}">
            <li id="spittle_<c:out value="spittle.id"/>">
                <div class="spittleMessage">
                    <c:out value="${spittle.message}"/>
                </div>
                <div>
                    <span class="spittleTime"><c:out value="${spittle.time}"/></span>
                    <span class="spittleLocation">
                        (<c:out value="${spittle.latitude}"/>,
                        <c:out value="${spittle.longitude}"/>)</span>
                </div>
            </li>
        </c:forEach>
        <div class="spittleView">
            <p>Single spittle:</p>
            <div class="spittleMessage"><c:out value="${spittle.message}" /></div>
            <div>
                <span class="spittleTime"><c:out value="${spittle.time}" /></span>
            </div>
        </div>
    </body>
</html>
