<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
    <title>${pageTitle}</title>
    <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body>
<div class="navigation-bar">
    <a href="${pageContext.servletContext.contextPath}" class="logo">
        <img src="${pageContext.servletContext.contextPath}/images/logo.svg" alt="logo">
        PhoneShop
    </a>
    <jsp:include page="/cart/miniCart"/>
</div>
<main>
    <jsp:doBody/>
</main>
</body>
</html>