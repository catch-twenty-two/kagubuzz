<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">


<!-- Jquery has to go before bootstrap -->

<kagutags:jquery/>

<kagutags:bootstrap_css/>

<link rel="stylesheet" href="/static/css/main.css" type="text/css" />

<head>
	<meta charset="utf-8" />
	<title>Kagu Buzz - Your Local Community Your Way</title>
</head>

<body>
	<div class="wrapper">
	<kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>

			<div class="buzzinfoboxpadding">
			<h4>TOTAL COUNT OF RECORDS</h4>  
			   <span>Total Users: ${usercount}</span>
			   <span>Total Active Events: ${eventcount}</span>
               <span>Total Active Ads: ${adcount}</span><br><br>
            
            <h4>LIST OF CURRENT USERS</h4>   	   	
			   <c:if test="${not empty listusers}">
                  <table class="table">
                  <tr>
                  <th>Full Name</th><th>Email Address</th><th>Social User</th><th>Last Login</th>
                  </tr>
                  <c:forEach var="user" items="${listusers}">
               		<tr>
               		<td>${user.getFirstName()} ${user.getLastName()}</td>
               		<td>${user.getEmail()}</td>
               		<td>
               		<c:if test="${user.isSocialAccount()}">
               		   ${user.getSocialUserAccount().getProviderId()}
               		</c:if>
                    </td>
               		<td>
               		<fmt:formatDate pattern="hh:mm a, MMMMMMMMM dd, yyyy" value="${formatter.gmtDateToTzDate(user.getLastLogin(), user.getTimeZoneOffset())}" />
               		</td>
               		</tr>	   
                  </c:forEach>
                  </table>
                  <kagutags:pagination/>
               </c:if>   
			</div>
		<kagutags:footer/>
	</div>
</body>

</html>


