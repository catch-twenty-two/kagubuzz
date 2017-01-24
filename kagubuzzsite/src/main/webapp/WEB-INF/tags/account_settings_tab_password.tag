<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<div class="tab-pane fade in" id="passwords">
	<c:choose>
		<c:when test="${!user.isSocialAccount() }">
			<div class="control-group">
				<label class="control-label"><strong>Password</strong></label>
				<div class="controls">
					<input type="password" id="password" autocomplete="off" name="password" placeholder="New Password">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><strong>Re-Enter Password</strong></label>
				<div class="controls">
					<input type="password" name="re_password" autocomplete="off" placeholder="Re-Enter New Password">
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="control-group">
				<label class="control-label"><strong>Password</strong></label>
				<div class="controls">
					<input type="password" id="password" autocomplete="off" name="password"
						placeholder="Signed In With ${formatter.capitalizeFirstLetter(user.getSocialUserAccount().getProviderId())}" disabled>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><strong>Re-Enter Password</strong></label>
				<div class="controls">
					<input type="password" name="re_password"
						placeholder="Signed In With ${formatter.capitalizeFirstLetter(user.getSocialUserAccount().getProviderId())}" disabled>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</div>