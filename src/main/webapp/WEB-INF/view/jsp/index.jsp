<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Walkie Talkie</title>
</head>
<body>
<form action="<c:url value="/add_profile"/>" enctype="multipart/form-data" method="post">
    <table class="center">
        <tr>
            <td>User Name:</td>
            <td><input type="text" title="userName"></td>
        </tr>
        <tr>
            <td>First Name:</td>
            <td><input type="text" title="firstName"/></td>
        </tr>
        <tr>
            <td>Last Name:</td>
            <td><input type="text" title="lastName"/></td>
        </tr>
        <tr>
            <td>Email:</td>
            <td><input type="text" title="email"/></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="text" title="password"/></td>
        </tr>
        <tr>
            <td>Employee Photo:</td>
            <td><input type="file" accept=".jpg" title="file"/></td>
        </tr>
    </table>
    <input type="submit" class="button" value="Add"/>
</form>
</body>
</html>
