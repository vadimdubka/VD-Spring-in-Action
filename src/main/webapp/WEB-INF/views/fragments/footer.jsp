<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="container">
    <hr>
    <footer>
        <p>vadimdubka@gmail.com</p>
    </footer>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

<spring:url var="coreJs" value="/resources/core/js/hello.js"/>
<spring:url var="bootstrapJs" value="/resources/core/js/bootstrap.min.js"/>

<script src="${coreJs}"></script>
<script src="${bootstrapJs}"></script>