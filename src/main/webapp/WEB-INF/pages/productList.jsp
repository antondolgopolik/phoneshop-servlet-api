<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentlyViewed" type="com.es.phoneshop.model.recentlyviewed.RecentlyViewed" scope="request"/>
<%--@elvariable id="quantityError" type="java.lang.String"--%>
<tags:master pageTitle="Product List">
    <div>
        <p>
                ${cart}
        </p>
        <c:if test="${not empty quantityError}">
            <p class="error">Product addition to cart failed</p>
        </c:if>
        <c:if test="${not empty param.status}">
            <p class="success">${param.status}</p>
        </c:if>
    </div>

    <p>
        Product List
    </p>

    <form>
        <label>
            <input name="query" value="${param.query}">
        </label>
        <button>Search</button>
    </form>

    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="des" content="\/"/>
                <tags:sortLink sort="description" order="asc" content="/\\"/>
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="des" content="\/"/>
                <tags:sortLink sort="price" order="asc" content="/\\"/>
            </td>
            <td class="quantity">Quantity</td>
            <td>Add to cart</td>
        </tr>
        </thead>

        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile"
                         src="${product.imageUrl}"
                         alt="Product image">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}
                    </a>
                </td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/products/price/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td class="quantity">
                    <form id="addToCart${product.id}" method="post"
                          action="${pageContext.servletContext.contextPath}/products"
                    >
                        <c:set var="error" value="${(not empty quantityError) and (product.id == param.id)}"/>
                        <label>
                            <input name="quantity" value="${error ? param.quantity : 1}" class="quantity">
                            <input type="hidden" name="id" value="${product.id}">
                        </label>
                        <c:if test="${error}">
                            <p class="error">
                                    ${quantityError}
                            </p>
                        </c:if>
                    </form>
                </td>
                <td>
                    <button form="addToCart${product.id}">Add to cart</button>
                </td>
            </tr>
        </c:forEach>
    </table>

    <c:if test="${recentlyViewed.products.size() > 0}">
        <p>
            Recently viewed
        </p>
        <table>
            <tr>
                <c:forEach var="product" items="${recentlyViewed.products}">
                    <td>
                        <img class="product-tile"
                             src="${product.imageUrl}"
                             alt="Error">
                        <p>
                            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                    ${product.description}
                            </a>
                        </p>
                    </td>
                </c:forEach>
            </tr>
        </table>
    </c:if>
    <tags:footer/>
</tags:master>