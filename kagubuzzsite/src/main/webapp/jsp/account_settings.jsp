<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8" />
      <kagutags:bootstrap_css/>
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <title>Kagu Buzz - Your Local Community Your Way</title>
   </head>
   <body>
      <kagutags:jquery />
      <kagutags:bootstrap_js/>
      <script src="/static/jquery/jquery.form.js" type="text/javascript"></script>
      <script src="/static/jquery/fileuploader.js" type="text/javascript"></script>
      <kagutags:user_info_form_validate 
         form_name="update_account" 
         email="Y" zip_code="Y" 
         last_name="Y" 
         password="Y" 
         password_required="false"
         first_name="Y" 
         re_password="Y" 
         phone_number="Y" 
         submit_handler="submitFormHandler()" 
         error_handler="$('button[data-loading-text]').button('reset')"/>
      <div class="wrapper">
         <kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
         <form class="form-horizontal" id="update_account" method="post" action="updatesettings">
            <div class="buzzinfobox notitle" style="padding:10px;">
               <fieldset>
                  <legend>Your Account Settings</legend>
               </fieldset>
               <div class="pull-left" style="width:100%; min-height: 350px;">
                  <ul id="myTab" class="nav nav-tabs">
                     <li class="active"><a href="#profile" data-toggle="tab">Profile</a></li>
                     <li><a href="#selling_buying" data-toggle="tab">Exchanges</a></li>
                     <li><a href="#passwords" data-toggle="tab">Password</a></li>
                     <li><a href="#following" data-toggle="tab">Following</a></li>
                  </ul>
                  <div id="myTabContent" class="tab-content">
                     <div>
                     </div>
                     <kagutags:account_settings_tab_following/>
                     <kagutags:account_settings_tab_password/>
                     <kagutags:account_settings_tab_profile/>
                     <kagutags:account_settings_tab_ads/>
                  </div>
               </div>
               <div class="clearfix"></div>
               <hr class="kagu-hr">
               <button type="submit" class="btn btn-primary pull-right" data-loading-text="One Moment..." autocomplete="off">Save Changes</button>
               <div class="clearfix"></div>
            </div>
         </form>
         <div >
         </div>
         <div class="clearfix"></div>
         <kagutags:footer />
      </div>
      <script>
      $("a[rel=popover]").popover({trigger:'hover', placement:'bottom'});
             $('button[data-loading-text]').click(function () {
                    $(this).button('loading');
                });
           
     
         function submitFormHandler() {$('#update_account').ajaxSubmit({dataType: 'json', 
             success: function(data) { 
                 $('button[data-loading-text]').button('reset');
                 renderServerNotification(data);
    
             }
            }); return false;
         }
</script>
   </body>
</html>
