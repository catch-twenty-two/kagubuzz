<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">


<!-- Jquery has to go before bootstrap -->

<kagutags:jquery/>

<kagutags:bootstrap_css/>

<!-- ajax form submit -->

<script src="/static/jquery/jquery.form.js" type="text/javascript"/></script>

<link rel="stylesheet" href="/static/css/main.css" type="text/css" />

<head>
	<meta charset="utf-8" />
	<title>Kagu Buzz - Your Local Community Your Way</title>
</head>

<script type="text/javascript">
     $('#add_new_discussion_category').ajaxForm({dataType: 'json', success: renderServerNotification});
     $('#create_defaults').ajaxForm({dataType: 'json', success: renderServerNotification});
     $('#create_defaults').ajaxForm({dataType: 'json', success: renderServerNotification});
     $('#delete_user').ajaxForm({dataType: 'json', success: renderServerNotification});
</script>

<body>
	<div class="wrapper">
	<kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
			<div class="buzzinfoboxpadding">
			<h2>Welcome to the Admin Console!</h2>
			<span><strong>CREATE CATEGORIES</strong></span>
			<br><br>
			<form id="add_new_discussion_category" class="form-inline" action="addnewcategory" method="POST">
                <input id="parent_discussion_category" type="text" class="input-large" placeholder="Parent Category" name="parentCategory">               
                <input type="text" class="input-large" placeholder="New Category" name="newCateogry">
                <button type="submit" class="btn">Save Category</button>
             </form>
             <form id="create_defaults" class="form-inline" action="setdefaultcategories" method="POST">
                <button type="submit" class="btn">Create Default Categories</button>
             </form>
             <span><strong>DELETE A CATEGORY</strong></span>
              <br><br>
              <form id="delete_category" class="form-inline" action="deletecategory" method="POST">
                <input id="discussion_category" type="text" class="input-large" placeholder="Category" name="category">
                <button type="submit" class="btn">Delete</button>
             </form>
             <span><strong>DELETE A USER</strong></span>
             <br><br>
             <form id="delete_user" class="form-inline" action="deleteuser" method="POST">
                <input id="discussion_category" type="text" class="input-large" placeholder="Delete User" name="id">
                <button type="submit" class="btn">Delete</button>
             </form>
             <span><strong>VIEW DATABASE RECORDS SUCH AS CURRENT USERS AND TOTAL COUNT INFORMATION</strong></span>
           	 <br><br>
           	 <form id="list_users" class="form-inline" action="listusers" method="GET">
                 <button type="submit" class="btn">DISPLAY DATA</button>
             </form>
			</div>   
		<kagutags:footer/>
	</div>
</body>

</html>

<kagutags:type_ahead textareaid="parent_discussion_category" textareaid1="discussion_category" ajaxurl="typeaheadiscusscat"/>
