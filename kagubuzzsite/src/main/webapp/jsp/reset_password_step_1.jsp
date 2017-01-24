<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
   <head>
      <kagutags:bootstrap_css />
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/bootstrapjumbotron.css" type="text/css" />
      <meta charset="utf-8" />
      <title>Kagu Buzz Resetting Your Password</title>
   </head>
   <body>
      <kagutags:jquery />
      <kagutags:bootstrap_js />
      <script src="/static/jquery/jquery.validate.js" type="text/javascript"></script>
      <div class="wrapper">
       <kagutags:user_info_form_validate form_name="reset_password" first_name="Y" password="Y" password_required="true" re_password="Y"
      zip_code="Y" email="Y" submit_handler="getBetaTestCode()" />
         <kagutags:header navactivebutton="" headerpic="smallheader.jpg"></kagutags:header>
         <kagutags:infoboxtop cssclass="buzzinfoboxtitle homemessagebox" collapsed="flase" collapseable="true" title="" />
         <div class="buzzinfoboxpadding">
            <form id="reset_password" action="/reset_password_step_2">
               <input name="securityCode" value="${reset_user.getSecurityCode()}" type="hidden" />
               <input name="id" value="${reset_user.getId()}" type="hidden" />
               <fieldset>
                  <legend>Hey ${reset_user.getFirstName()}, Let's Reset Your Password</legend>
                  <div class="pull-left" style="margin-right: 10px;">
                     <input type="password" id="password" autocomplete="off" name="password" placeholder="New Password">
                     <br><br>
                     <input type="password" name="re_password" autocomplete="off" placeholder="Re-Enter New Password">
                    <br><br><br>
                        <button type="submit" class="btn">Submit</button>

                  </div>
                  <div class="pull-left">${recaptcha}</div>
               </fieldset>
            </form>
         </div>
         <kagutags:infoboxbottom />
         <kagutags:footer />
      </div>
      <script type="text/javascript">
         $("#resetpassword").validate();         
         <c:if test="${not empty captcha_error}">
             toastr.error('${captcha_error}', 'Uh, Oh! RE-Captcha Error');
         </c:if>
      </script>
   </body>
</html>
