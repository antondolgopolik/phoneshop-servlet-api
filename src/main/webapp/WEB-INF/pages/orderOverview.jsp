<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order overview">
    <p>
        Order ${order.id}
    </p>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td class="price">Price</td>
            <td class="quantity">Quantity</td>
        </tr>
        </thead>
        <c:forEach var="cartItem" items="${order.cart.items}" varStatus="status">
            <tr>
                <td>
                    <img class="product-tile"
                         src="${cartItem.product.imageUrl}"
                         alt="Error">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
                            ${cartItem.product.description}
                    </a>
                </td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/price/${cartItem.product.id}">
                        <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                          currencySymbol="${cartItem.product.currency.symbol}"/>
                    </a>
                </td>
                <td class="quantity">
                    <fmt:formatNumber value="${cartItem.quantity}"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2">Total cost</td>
            <td colspan="2" class="price">
                <fmt:formatNumber value="${order.cart.totalCost}" type="currency"
                                  currencySymbol="${order.cart.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">Delivery cost</td>
            <td colspan="2" class="quantity">
                <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                  currencySymbol="${order.cart.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">Subtotal</td>
            <td colspan="2" class="quantity">
                <fmt:formatNumber value="${order.subtotal}" type="currency"
                                  currencySymbol="${order.cart.currency.symbol}"/>
            </td>
        </tr>
    </table>
    <p>Your details</p>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table>
            <tags:orderOverviewTableRow rowName="First name" rowValue="${order.firstName}"/>
            <tags:orderOverviewTableRow rowName="Last name" rowValue="${order.lastName}"/>
            <tags:orderOverviewTableRow rowName="Phone" rowValue="${order.phone}"/>
            <tags:orderOverviewTableRow rowName="Delivery date" rowValue="${order.deliveryDate}"/>
            <tags:orderOverviewTableRow rowName="Delivery address" rowValue="${order.deliveryAddress}"/>
            <tags:orderOverviewTableRow rowName="Payment method" rowValue="${order.paymentMethod.paymentMethod}"/>
        </table>
    </form>
    <tags:footer/>
</tags:master>