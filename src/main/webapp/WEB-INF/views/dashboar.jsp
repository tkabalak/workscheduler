<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="../../files/jquery/jquery.js"/>
        <script>
            $(".delete").click(function () {
                $("p").remove();
                
            });
        </script>
    </head>
    <body>
        <h1>Task list for: ${user_role}</h1>
        <table border="1">
            <thead>
                <tr>
                    <th><spring:message code="id_message"/></th>
                    <th><spring:message code="title_message"/></th>
                    <th><spring:message code="description_message"/></th>
                    <th><spring:message code="expire_date_message"/></th>
                    <th><spring:message code="add_date_message"/></th>
                    <th><spring:message code="progress_message"/></th>
                    <th><spring:message code="progress_message"/></th>
                    <th><spring:message code="action_message"/></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${tasks}" var="task">
                    <tr>
                        <td>${task.id}</td>
                        <td>${task.name}</td>
                        <td>${task.description}</td>
                        <td>${task.expireDate}</td>
                        <td>${task.addDate}</td>
                        <td>${task.progres}</td>
                        <td><button id="delete"><spring:message code="delete_button"/></button></td>
                        <td><spring:message code="edit_button"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
