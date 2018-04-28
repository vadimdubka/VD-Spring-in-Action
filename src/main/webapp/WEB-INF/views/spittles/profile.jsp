<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
    <head>
        <title>Spitter</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css" />">
    </head>
    <body>
        <h1>Your Profile</h1>
        UserName <c:out value="${spitter.username}"/><br/>
        FirstName <c:out value="${spitter.firstName}"/><br/>
        LastName <c:out value="${spitter.lastName}"/><br/>
        Email <c:out value="${spitter.email}"/>
    </body>

</html>
