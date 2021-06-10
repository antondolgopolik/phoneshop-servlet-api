<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<%--@elvariable id="result" type="java.lang.Boolean"--%>
<%--@elvariable id="quantityError" type="java.lang.String"--%>
<tags:master pageTitle="Product Details">
    <form method="post" action="./${product.id}">
        <div>
            <p>
                    ${cart}
            </p>
            <c:if test="${not empty result}">
                <p class="${result ? "success" : "error"}">
                        ${result ? "Product addition to cart succeeded" : "Product addition to cart failed"}
                </p>
            </c:if>
            <c:if test="${not empty param.result}">
                <p class="${param.result eq "true" ? "success" : "error"}">
                        ${param.result eq "true" ? "Product addition to cart succeeded" : "Product addition to cart failed"}
                </p>
            </c:if>
        </div>
        <p>
            Product Details
        </p>
        <table>
            <tr>
                <td>Code</td>
                <td>${product.code}</td>
            </tr>
            <tr>
                <td>Description</td>
                <td>${product.description}</td>
            </tr>
            <tr>
                <td>Price</td>
                <td>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>${product.stock}</td>
            </tr>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}"
                         alt="Error">
                </td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <label>
                        <input name="quantity" value="${not empty quantityError ? param.quantity : 1}">
                    </label>
                    <c:if test="${not empty quantityError}">
                        <p class="error">
                                ${quantityError}
                        </p>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>
    <tags:footer/>
</tags:master>