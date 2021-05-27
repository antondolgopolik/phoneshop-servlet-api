<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="content" required="true" %>

<a href="?query=${param.query}&sort=${sort}&order=${order}"
   style="color: ${(param.sort eq sort) and (param.order eq order) ? 'red' : 'black'}"
>
    ${content}
</a>