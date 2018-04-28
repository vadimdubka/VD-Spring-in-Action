<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <title>Spring MVC Form Handling Example</title>
    <spring:url value="/resources/core/css/hello.css" var="coreCss"/>
    <spring:url value="/resources/core/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>
    <link href="${coreCss}" rel="stylesheet"/>
</head>

<spring:url var="urlHome" value="/"/>
<spring:url var="urlAddUser" value="/users/add"/>

<nav class="navbar navbar-inverse ">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="${urlHome}">See Users</a>
        </div>
        <div id="navbar">
            <ul class="nav navbar-nav navbar-right">
                <li class="active"><a href="${urlAddUser}">Add User</a></li>
            </ul>
        </div>
    </div>
</nav>