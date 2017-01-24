<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ attribute name="imagename" required="true" type="java.lang.String"%>
<%@ attribute name="defaultimageurl" required="true" type="java.lang.String"%>
<%@ attribute name="width" required="true" type="java.lang.String"%>
<%@ attribute name="height" required="true" type="java.lang.String"%>
<%@ attribute name="captiontitle" required="false" type="java.lang.String"%>


<li id="imageuploader${imagename}" style="margin-bottom:0px; text-align: center;">
	<c:if test="${not empty captiontitle}">
		<span id="caption_title_${imagename}" class="label">${captiontitle}</span>
	</c:if>
	<p>
	<input id="indexedfilename${imagename}" name="${imagename}" value="" type="hidden"/>
	<div style="margin-bottom: 5px;">
		<div class="thumbnail" >
			<img style="width:${width}px; height:${height}px;" id="image${imagename}" src="${defaultimageurl}" alt="${captiontitle}">
		</div>
	</div>
    <div class="clearfix"></div>
	<div id="uploadprogressbar${imagename}" class="progress">
		<div id="uploadprogress${imagename}" class="bar"></div>
	</div>
</li>

<script>
var uploader = new qq.FileUploaderBasic({
	button: document.getElementById('imageuploader' + '${imagename}'),
    action: "/uploadservlet",
    allowedExtensions: ["jpg","jpeg"],
    sizeLimit: 1024*1024*3,
    debug: true,
    onSubmit: function(id, fileName){
    	$('#uploadprogressbar' + '${imagename}').addClass('active');
    	$('#uploadprogressbar' + '${imagename}').addClass('progress-striped');
    	$('#uploadprogress' + '${imagename}').width('0%');
    },
    onProgress: function(id, fileName,uploadedBytes,totalBytes) {
    	$('#uploadprogress' + '${imagename}').width((uploadedBytes/totalBytes)*100 + '%');
	},
    onComplete: function(id, fileName, response) {
    	$('#uploadprogressbar' + '${imagename}').removeClass('active');
    	$('#uploadprogressbar' + '${imagename}').removeClass('progress-striped');
    	$('#image' + '${imagename}').fadeTo(2000,.01, function(){
    	$('#image' + '${imagename}').attr('src', response.thumbnailfilename);
    	$('#image' + '${imagename}').fadeTo(2000,1);});
    	$('#indexedfilename' + '${imagename}').val(response.indexedfilename);
    }
})
</script>