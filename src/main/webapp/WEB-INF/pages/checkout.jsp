<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<%--@elvariable id="paymentMethods" type="com.es.phoneshop.model.order.PaymentMethod[]"--%>
<%--@elvariable id="errors" type="java.util.Map"--%>
<tags:master pageTitle="Checkout">
    <div>
        <p>Checkout</p>
        <c:if test="${not empty errors}">
            <p class="error">Order confirmation failed</p>
        </c:if>
    </div>
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
            <td colspan="2" class="price">
                <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                  currencySymbol="${order.cart.currency.symbol}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">Subtotal</td>
            <td colspan="2" class="price">
                <fmt:formatNumber value="${order.subtotal}" type="currency"
                                  currencySymbol="${order.cart.currency.symbol}"/>
            </td>
        </tr>
    </table>
    <p>Your details</p>
    <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table>
            <tags:checkoutFormTableRow rowName="First name" paramName="firstName" errors="${errors}"/>
            <tags:checkoutFormTableRow rowName="Last name" paramName="lastName" errors="${errors}"/>
            <tags:checkoutFormTableRow rowName="Phone" paramName="phone" errors="${errors}"/>
            <tags:checkoutFormTableRow rowName="Delivery date" paramName="deliveryDate" errors="${errors}"/>
            <tags:checkoutFormTableRow rowName="Delivery address" paramName="deliveryAddress" errors="${errors}"/>
            <tr>
                <td>Payment method<span style="color: red">*</span></td>
                <td>
                    <label>
                        <select name="paymentMethod">
                            <option></option>
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option>${paymentMethod.paymentMethod}</option>
                            </c:forEach>
                        </select>
                    </label>
                    <c:set var="error" value="${errors.get('paymentMethod')}"/>
                    <c:if test="${not empty error}">
                        <p class="error">
                                ${error}
                        </p>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Confirm</button>
        </p>
    </form>
    <tags:footer/>
</tags:master>