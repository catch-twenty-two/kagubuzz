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
   <script src="/static/jquery/update-message.js" type="text/javascript"/></script>
   <script src="/static/jquery/createnewmessage.js" type="text/javascript"/></script>
   <script src="/static/jquery/postoptionclick.js" type="text/javascript"/></script>
   <script src="/static/jquery/textarea-limit.js" type="text/javascript"/></script>
   <kagutags:redactor/>
   <meta charset="utf-8" />
   <title>${event.getTitle()}</title>
   </head>
   <body>
      <div class="wrapper">
         <kagutags:header navactivebutton="discuss-it" headerpic="smallheader.jpg"></kagutags:header>
         <div class="clear-background">
            <c:if test="${discussion.messageType().name() == 'PublicDiscussion'}">
                <div class="buzzinfobox notitle">
               <kagutags:category_bread_crumbs breadcrumbs="${breadcrumbs}" breadcrumb_url="discussions" tags="${discussion.getTags()}" showuserpostoptions="Y" imessage="${discussion}"/>
               </div>
            </c:if>
            <div class="accordion" id="threadgroup">
               <c:if test="${not empty discussion }">
                  <div class="list_padding">
                  <div class="buzzinfobox buzzinfoboxpadding notitle" >
                     <span class="lead black">${discussion.getFirstMessageInThread().getSubject()}</span>
                       <c:if test="${user.isLoggedIn() && discussion.messageType().name() == 'PublicDiscussion'}">
                       <span id="bookmark${discussion.getFirstMessageInThread().getId()}">
                     <button id="subscribe" 
                     class="btn btn-small btn-success pull-right"
                      onclick="postOptionClick('','bookmark','${discussion.getFirstMessageInThread().getId()}'); return false;"
                     title="Receive updates for this conversation" alt="Bookmark">Bookmark</button>  
                     </span>
                    
                     </c:if>    
                      </div>            
                     <div id="attachhere" class="global-margin-top">
                        <kagutags:message_render 
                           showsubject="N"
                           use_redactor="Y"
                           message="${discussion.getFirstMessageInThread()}"
                           imessage="${discussion.getFirstMessageInThread()}"
                           domattachid="attachhere"
                           optionreply="Y"  
                           optionflag="Y" 
                           optionbuzzit="Y"
                           optionedit="Y"/>
                          
                        <c:forEach var="message" items="${discussion.getMessages()}">

                           <c:if test="${not message.isFirstMessage()}">
                              <kagutags:message_render 
                                 message="${message}"
                                 use_redactor="Y"
                                 imessage="${message}"
                                 optionremove="Y"
                                 domattachid="attachhere"
                                 optionreply="Y"  
                                 optionflag="Y" 
                                 optionbuzzit="Y"
                                 optionedit="Y"/>
                           </c:if>

                        </c:forEach>
                     </div>
                  </div>
               </c:if>
            </div>
         </div>
        <kagutags:footer />
      </div>
      <kagutags:type_ahead textareaid="categories" ajaxurl="typeaheadiscusscat"/>
      <kagutags:scroll_to_message/>
            <kagutags:add_this />
      <script type="text/javascript">

      function showUserProfile(userId) {         
        $('#modal_dialog').load('/modal/userprofile/' + userId, function() {$('#modal_dialog').modal('show');});
      }
      
      $(document).ready(function() {
      <c:if test="${new_post}">
        toastr.info("Your new discussion should show up in the listing in a few moments, thanks for using Kagu Buzz.", "New Discussion Posted!");
      </c:if>
      });
   </script>
   </body>
</html>