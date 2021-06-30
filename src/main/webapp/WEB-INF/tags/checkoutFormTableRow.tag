<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="rowName" required="true" %>
<%@ attribute name="paramName" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>

<tr>
    <td>${rowName}<span class="field-required-span">*</span></td>
    <td>
        <label>
            <input name="${paramName}" value="${param.get(paramName)}">
        </label>
        <c:set var="error" value="${errors.get(paramName)}"/>
        <c:if test="${not empty error}">
            <p class="error">
                    ${error}
            </p>
        </c:if>
    </td>
</tr>