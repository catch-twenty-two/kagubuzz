<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<kagutags:message_render
    use_redactor="${use_redactor}"
	imessage="${message}"
	message="${message}"
	ajaxurl="${ajaxurl}"
	domattachid="${domattachid}"
	optionreply="Y"
	optionremove="${option_remove}"
	optionedit="${optionedit}" 
	optionflag="Y" 
	optionbuzzit="Y"/>