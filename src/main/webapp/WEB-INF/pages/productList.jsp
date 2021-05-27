<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
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
                <td>${product.description}</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>