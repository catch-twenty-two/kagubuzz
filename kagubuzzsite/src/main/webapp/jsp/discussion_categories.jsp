<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">


<!-- Jquery has to go before bootstrap -->

<kagutags:jquery />

   <kagutags:bootstrap_css/>
   <kagutags:bootstrap_js/>

<link rel="stylesheet" href="/static/css/main.css" type="text/css" />
<link rel="stylesheet" href="/static/css/bootstrap-tagmanager.css" type="text/css" />

<script src="/static/jquery/bootstrap-tagmanager.js" type="text/javascript"></script>
<head>
<meta charset="utf-8" />
<title>Kagu Buzz - Your Local Community Your Way</title>
</head>

<body>
	<div class="wrapper">
		<kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
		<div class="main-content">
			<fieldset>
				<legend>Browse By A Discussion Category</legend>
				<div class="buzzinfoboxpadding clear-background">
					<c:forEach var="discussion_category" items="${discussion_categories}">
						<a href="/discussions/browse/category/${discussion_category.getId()}/${discussion_category.getFriendlyURL()}" class="span2">${discussion_category.getName()}</a>
					</c:forEach>
					<div class="clearfix"></div>
				</div>
				<br>
				<legend>Or Click A Tag Below To Narrow Your Search In a Category</legend>
			</fieldset>
			<div id="tagscontainer" style="min-height: 275px">
				<c:forEach var="discussion_tag" items="${discussion_tags}">
					<a href="javascript:void(null)" onclick="notifyAdded('${discussion_tag.getName()}'); return false;" class="myTagBreadCrumb">${discussion_tag.getName()}</a>
				</c:forEach>
			</div>
			<div class="clearfix"></div>
			<hr class="kagu-hr">
			<div style="text-align: center; background: none;">
				<button onclick="getNextTagGroup()" class="btn btn-warning" data-loading-text="One Moment..." autocomplete="off" type="button">Next Group Of Tags</button>
			</div>
		</div>
		<div class="side-bar">
			<img alt="" src="/static/images/kagutitlesmall.png" />
			<hr class="kagu-hr">
			<h4>Discussion Tags and Categories</h4>
            <c:if test="${user.isLoggedIn()}">
                <hr class="kagu-hr"> 
                <a href="/discussion/create" class="btn btn-block btn-danger" type="button">Start A New Discussion</a>
            </c:if> 
			<hr class="kagu-hr">
			<h5>
				<a href="/discussions/browse">Browse Discussions</a>
			</h5>
			<kagutags:discussion_sidebar />
		</div>
		<kagutags:footer />
	</div>
	<script type="text/javascript">
	 $('button[data-loading-text]').click(function () {
		    $(this).button('loading');
	});
	 
    var offset = 1;

    function getNextTagGroup() {

        $.ajax({
            type : "POST",
            url : '/discussions/browse/next_tag_group',
            dataType : "html",
            data : {
                offset : offset
            },
            success : function(data) {
            	$('button[data-loading-text]').button('reset');
                if (data == 'false') {
                    offset = 0;
                    return;
                }
                offset++;
                $('#tagscontainer').children().remove();
                $('#tagscontainer').css('opacity', 0);
                $('#tagscontainer').html(data);
                $('#tagscontainer').animate({
                    opacity : 1
                }, 'slow');
            }
        });
    }
</script>
</body>

</html>



