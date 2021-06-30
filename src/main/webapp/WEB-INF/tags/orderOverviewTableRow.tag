<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="rowName" required="true" %>
<%@ attribute name="rowValue" required="true" %>

<tr>
    <td>${rowName}</td>
    <td>${rowValue}</td>
</tr>