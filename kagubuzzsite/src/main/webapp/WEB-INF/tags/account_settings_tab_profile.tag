<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<div class="tab-pane fade in active" id="profile">
	<div class="pull-left" >
		<ul class="thumbnails" title="Click here, or on the box below, to add your picture">
			<kagutags:image_uploader captiontitle="Profile Picture" height="100" width="100" imagename="avatarimage"
				defaultimageurl="fileServerServlet?type=GetAvatarByUserId&amp;id=${user.getId()}" />
		</ul>
	</div>
	<div class="buzzinfoboxpadding">
		<fieldset>
			<div class="pull-left">
				<div class="control-group">
					<label class="control-label"><strong>First Name</strong></label>
					<div class="controls">
						<input type="text" name="first_name" value="${user.getFirstName()}" placeholder="First Name">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"><strong>Last Name</strong></label>
					<div class="controls">
						<input type="text" name="last_name" value="${user.getLastName()}" placeholder="Last Name">
					</div>
				</div>
			</div>
			<div class="pull-left">
				<div class="control-group">
					<label class="control-label"><strong>Email</strong></label>
					<div class="controls">
					<c:choose>
                        <c:when test="${!user.isSocialAccount() }">
						      <input type="email" name="email" value="${user.getEmail()}">
						</c:when>
						<c:otherwise>
						<input  type="email" name="email"  
                            placeholder="Signed In With ${formatter.capitalizeFirstLetter(user.getSocialUserAccount().getProviderId())}" disabled>
						</c:otherwise>
				    </c:choose>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"><strong>Zip Code</strong></label>
					<div class="controls">
						<input type="text" name="zip_code" value="${user.getZipCode()}" placeholder="Zip Code">
					</div>
				</div>
			</div>
			<div class="clearfix"></div>
			<div class="control-group">
				<div class="controls">
					<label class="checkbox inline"> <input type="checkbox" name="on_screen_notifications" ${formatter.isChecked(user.showNotifications())}> On Screen Notifications
					</label>
				</div>
				<div class="controls">
					<label class="checkbox inline"> <input type="checkbox" name="email_notifications" ${formatter.isChecked(user.getEmailNotifications())}> Email Notifications
					</label>
				</div>
			</div>
		</fieldset>
	</div>
</div>