<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<%--@elvariable id="errors" type="java.util.Map"--%>
<tags:master pageTitle="Cart">
    <div>
        <p>
                ${cart}
        </p>
        <c:if test="${not empty errors}">
            <p class="error">Cart update failed</p>
        </c:if>
        <c:if test="${not empty param.status}">
            <p class="success">${param.status}</p>
        </c:if>
    </div>
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td class="price">Price</td>
                <td>Stock</td>
                <td class="quantity">Quantity</td>
            </tr>
            </thead>
            <c:forEach var="cartItem" items="${cart.items}" varStatus="status">
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
                    <td>
                            ${cartItem.product.stock}
                    </td>
                    <td class="quantity">
                        <c:set var="error" value="${errors.get(cartItem.product.id)}"/>
                        <fmt:formatNumber var="quantity" value="${cartItem.quantity}"/>
                        <label>
                            <input name="quantity"
                                   value="${not empty error ? paramValues.quantity[status.index] : quantity}"
                                   class="quantity"
                            >
                            <input type="hidden" name="id" value="${cartItem.product.id}">
                        </label>
                        <c:if test="${not empty error}">
                            <p class="error">
                                    ${error}
                            </p>
                        </c:if>
                    </td>
                    <td>
                        <button form="deleteCartItem"
                                formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${cartItem.product.id}">
                            Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="2">Total cost</td>
                <td class="price">
                    <fmt:formatNumber value="${cart.totalCost}" type="currency"
                                      currencySymbol="${cart.currency.symbol}"/>
                </td>
                <td colspan="2">Total quantity</td>
                <td class="quantity">
                    <fmt:formatNumber value="${cart.totalQuantity}"/>
                </td>
            </tr>
        </table>
        <p>
            <c:if test="${not empty cart.items}">
                <button>Update</button>
            </c:if>
            <c:if test="${empty cart.items}">
                <button disabled>Update</button>
            </c:if>
        </p>
    </form>
    <form id="deleteCartItem" method="post"></form>
    <form action="${pageContext.servletContext.contextPath}/checkout">
        <c:if test="${not empty cart.items}">
            <button>Checkout</button>
        </c:if>
        <c:if test="${empty cart.items}">
            <button disabled>Checkout</button>
        </c:if>
    </form>
    <tags:footer/>
</tags:master>