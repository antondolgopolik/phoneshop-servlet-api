<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentlyViewed" type="com.es.phoneshop.model.recentlyviewed.RecentlyViewed" scope="request"/>
<tags:master pageTitle="Product List">
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
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile"
                         src="${product.imageUrl}"
                         alt="Error">
                </td>
                <td>
                    <a href="./products/${product.id}">${product.description}</a>
                </td>
                <td class="price">
                    <a href="./products/price/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
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
                            <a href="./products/${product.id}">${product.description}</a>
                        </p>
                    </td>
                </c:forEach>
            </tr>
        </table>
    </c:if>
    <tags:footer/>
</tags:master>