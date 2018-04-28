<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>
<html>
    <head>
        <title>Spitter</title>
        <link rel="stylesheet" type="text/css"
              href="<c:url value="/resources/style.css" />">
    </head>
    <body>
        <h1>Register</h1>
        <%--Notice that the <form> tag doesn’t have an action parameter set. Because of that, when this form is submitted,
        it will be posted back to the same URL path that displayed it. That is, it will be posted back to /spitters/register.--%>
        <%--You set commandName to spitter. Therefore, there must be an object in the model whose key is spitter,
        or else the form won’t be able to render (and you’ll get JSP errors).--%>
        <sf:form method="POST" commandName="spitter" enctype="multipart/form-data">
            <sf:errors path="*" element="div" cssClass="errors"/>

            <%--Its value attribute will be set to the value of the model object’s property specified in the path attribute--%>
            <sf:label path="firstName" cssErrorClass="error">First Name</sf:label>:
            <sf:input path="firstName" cssErrorClass="error"/><br/>

            <sf:label path="lastName" cssErrorClass="error">Last Name</sf:label>:
            <sf:input path="lastName" cssErrorClass="error"/><br/>

            <sf:label path="email" cssErrorClass="error">Email</sf:label>:
            <sf:input path="email" type="email" cssErrorClass="error"/><br/>

            <sf:label path="username" cssErrorClass="error">Username</sf:label>:
            <sf:input path="username" cssErrorClass="error"/>
            <sf:errors path="username" cssClass="errors"/><br/>

            <sf:label path="password" cssErrorClass="error">Password</sf:label>:
            <sf:password path="password" cssErrorClass="error"/><br/>

            <label>Profile Picture</label>:
            <input type="file" name="profilePicture" accept="image/jpeg,image/png,image/gif, image/jpg"/><br/>

            <input type="submit" value="Register"/>
        </sf:form>
    </body>
</html>
