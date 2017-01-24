<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="form_name" required="true" type="java.lang.String"%>
<%@ attribute name="first_name" required="false" type="java.lang.String"%>
<%@ attribute name="last_name" required="false" type="java.lang.String"%>
<%@ attribute name="zip_code" required="false" type="java.lang.String"%> 
<%@ attribute name="phone_number" required="false" type="java.lang.String"%> 
<%@ attribute name="password" required="false" type="java.lang.String"%>
<%@ attribute name="password_required" required="false" type="java.lang.String"%>
<%@ attribute name="re_password" required="false" type="java.lang.String"%>
<%@ attribute name="email" required="false" type="java.lang.String"%>
<%@ attribute name="submit_handler" required="false" type="java.lang.String"%>
<%@ attribute name="error_handler" required="false" type="java.lang.String"%>
<script src="/static/jquery/jquery.validate.js" type="text/javascript"></script>
<script src="/static/jquery/additional-methods.js" type="text/javascript"></script>
<script type="text/javascript">
   $(document).ready(function()  {
	   
     
   $("#${form_name}").validate({
    ignore: "",
    errorClass: 'textsmall kagu-red',
       rules: {
           <c:if test="${not empty phone_number}">
           phone_number: {
               phoneUS: true
           },
           </c:if>
           <c:if test="${not empty zip_code}">
           zip_code: {
               required: true,
               minlength: 5,
               maxlength: 5,
               digits: true
           },
        </c:if>
           <c:if test="${not empty first_name}">
           first_name: {
               required: true,
               minlength:2,
               maxlength:12
           },
           </c:if>
        <c:if test="${not empty last_name}">
           last_name: {
               required: false,
               minlength:2,
               maxlength:15
           },
           </c:if>
           <c:if test="${not empty password}">
            password: {
               required: '${password_required}',
               minlength:5,
               maxlength:22
           },
           </c:if>
           <c:if test="${not empty re_password}">
           re_password: {
              required: false,
              equalTo: "#password",
              minlength:5,
              maxlength:22
          },
          </c:if>
           <c:if test="${not empty email}">
            email: {
               required: true,
               maxlength:35,
               email:true
           },
           </c:if>
         },
         <c:if test="${not empty error_handler}">
         invalidHandler: function(event, validator) {
        	 ${error_handler}
         },
        </c:if>
          <c:if test="${not empty submit_handler}">
         submitHandler: function(form) {
         if (${submit_handler})  
            form.submit();
         }
        </c:if>     
    });
   });
</script>