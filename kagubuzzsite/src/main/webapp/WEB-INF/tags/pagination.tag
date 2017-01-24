<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="query_options" required="false" type="java.lang.String" %>

<c:if test="${no_of_pages gt 1}">
<div class="pagination pagination-centered">
<%--For displaying Previous link except for the 1st page --%>
    <ul>
        <c:choose>
             <c:when test="${page != 0}">
                 <li><a href="?page=${page - 1}${query_options}">&laquo;</a></li>
             </c:when>
             <c:otherwise>
                 <li  class="disabled"><a href="javascript:void(null)" onclick="return false;">&laquo;</a></li>
             </c:otherwise>
        </c:choose>
        
    <%--For displaying Page numbers.
    The when condition does not display a link for the current page--%>
        
            <c:forEach begin="0" end="${no_of_pages - 1}" var="i">
                <c:choose>
                    <c:when test="${page eq i}">
                        <li class="disabled"><a href="javascript:void(null)" onclick="return false;">${i + 1}</a></li>
                    </c:when>
                    <c:otherwise>
                          <li><a href="?page=${i}${query_options}">${i + 1}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

    <%--For displaying Next link --%>
            <c:choose>
             <c:when test="${page + 1 lt no_of_pages}">
                 <li><a href="?page=${page + 1}${query_options}">&raquo;</a></li>
             </c:when>
             <c:otherwise>
                 <li  class="disabled"><a href="javascript:void(null)" onclick="return false;">&raquo;</a></li>
             </c:otherwise>
        </c:choose>     
     </ul>
     </div>
</c:if>