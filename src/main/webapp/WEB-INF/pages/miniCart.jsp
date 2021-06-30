<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<fmt:formatNumber var="totalQuantity" value="${cart.totalQuantity}"/>
<fmt:formatNumber var="totalCost" value="${cart.totalCost}" type="currency"
                  currencySymbol="${cart.currency.symbol}"/>
<a href="${pageContext.servletContext.contextPath}/cart" class="mini-cart">
    Cart: ${totalQuantity} qty, ${totalCost}
</a>
