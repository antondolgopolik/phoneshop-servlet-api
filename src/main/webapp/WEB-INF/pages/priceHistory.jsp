<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Price History
    </p>

    <table>
        <c:forEach var="priceHistoryRecord" items="${product.priceHistory}">
            <tr>
                <td>${priceHistoryRecord.date}</td>
                <td class="price">
                    <fmt:formatNumber value="${priceHistoryRecord.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
    <tags:footer/>
</tags:master>