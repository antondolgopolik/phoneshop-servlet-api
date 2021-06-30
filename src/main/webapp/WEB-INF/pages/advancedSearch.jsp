<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<%--@elvariable id="searchModes" type="com.es.phoneshop.model.advancedSearch.SearchMode[]"--%>
<%--@elvariable id="status" type="java.lang.String"--%>
<%--@elvariable id="errors" type="java.util.Map"--%>
<tags:master pageTitle="Product List">
    <div>
        <p>Advanced Search</p>
        <c:if test="${not empty status}">
            <p class="error">${status}</p>
        </c:if>
    </div>

    <form action="${pageContext.servletContext.contextPath}/products/advancedSearch">
        <table>
            <tags:advancedSearchFormTableRow rowName="Description" paramName="query" errors="${errors}"/>
            <tr>
                <td>Search mode</td>
                <td>
                    <label>
                        <select name="searchMode">
                            <c:if test="${not empty param.searchMode}">
                                <option>${param.searchMode}</option>
                            </c:if>
                            <c:forEach var="searchMode" items="${searchModes}">
                                <c:if test="${not (searchMode.searchMode eq param.searchMode)}">
                                    <option>${searchMode.searchMode}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </label>
                    <c:set var="error" value="${errors.get('searchMode')}"/>
                    <c:if test="${not empty error}">
                        <p class="error">
                                ${error}
                        </p>
                    </c:if>
                </td>
            </tr>
            <tags:advancedSearchFormTableRow rowName="Min price" paramName="minPrice" errors="${errors}"/>
            <tags:advancedSearchFormTableRow rowName="Max price" paramName="maxPrice" errors="${errors}"/>
        </table>
        <p>
            <button>Search</button>
        </p>
    </form>
    <p>Products</p>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td class="price">Price</td>
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
            </tr>
        </c:forEach>
    </table>
    <tags:footer/>
</tags:master>