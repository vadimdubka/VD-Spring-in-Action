<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ page session="false" %>
<html>
    <head>
        <title>Spittr</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css" />">
    </head>
    <body>
        <%--DISPLAYING  INTERNATIONALIZED  MESSAGES--%>
        <h2><s:message code="spitter.welcome" text="Welcome (default)"/></h2>

        <%--Old variant--%><%--<a href="<c:url value="/spittles" />">Spittles</a> |--%>
        <%--Using spring tag <s:url> takes a servlet-context-relative URL
        and renders it with  the  servlet  context  path prepended (the same thing does <c:url> tag)--%>
        <a href="<s:url value="/spittles" />">Spittles</a> |

        <%--Url with parameters--%>
        <s:url value="/spittles/show" var="spittlesUrl">
            <s:param name="max" value="60"/>
            <s:param name="count" value="5"/>
        </s:url>
        <a href="${spittlesUrl}">Spittles with params</a> |

        <%--Old variant--%><%--<a href="<c:url value="/spitter/register" />">Register</a>--%>
        <%--Using spring tag you can have <s:url> construct the URL and assign it to a variable to be used later in the template --%>
        <s:url value="/spitter/register" var="registerUrl"/>
        <a href="${registerUrl}">Register</a> |

        <%--URL with replaceable path parameter--%>
        <s:url value="/spitter/{username}" var="spitterUrl">
            <s:param name="username" value="jbauer"/>
        </s:url>
        <a href="${spitterUrl}">User jbauer</a> <br>

        <h2>Thymeleaf examples (only demonstration, don't go through links):</h2>
        <a href="<c:url value="/thymeleaf/home" />">Home page</a> |
        <a href="<c:url value="/spitter/thymeleaf/register" />">Register</a> <br>

        <h2>Multiple files upload</h2>
        <form action="spitter/multipleFileUpload" method="post" enctype="multipart/form-data">
            <table>
                <tr>
                    <td>Select Files</td>
                    <td><input type="file" name="file" multiple="multiple"></td>
                    <td>
                        <button type="submit">Upload</button>
                    </td>
                </tr>
            </table>
        </form>

        <h1>Welcome to User Form</h1>
        <ul>
            <li><a href="<c:url value="/users" />">Users</a></li>
        </ul>

        <hr/>
        <span style="color: red; font-size: 14px;">${msg}</span>
    </body>
</html>