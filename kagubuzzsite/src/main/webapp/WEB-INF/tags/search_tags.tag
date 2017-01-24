<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>

<%@ attribute name="ajaxurl" required="true" type="java.lang.String" description="Typeahead url"%>
<div style="min-height: 130px;">
<kagutags:type_ahead ajaxurl="${ajaxurl}"/>

<input type="text" 
        autocomplete="off" 
        data-items="6" 
        data-provide="typeahead" 
        name="tags" placeholder="Type an Interest" 
        style="width:9em;" 
        class="input-medium tagManager" 
        data-original-title=""/>
</div>  
 
 <script src="/static/jquery/user-tags.js" type="text/javascript"></script>
<script type="text/javascript">
userTagsInit()
</script>
