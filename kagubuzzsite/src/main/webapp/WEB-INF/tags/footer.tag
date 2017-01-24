<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<footer class="linkcolor">
   <c:if test="${not user.isLoggedIn() || show_log_in}">
      <div class="navbar navbar-fixed-bottom">
         <div class="navbar-inner">
            <div class="container" style="width:960px;">
               <script src="/static/jquery/login.js" type="text/javascript"/></script>                  
               <script src="/static/jquery/jquery.validate.js" type="text/javascript"/></script>
               <script src="/static/jquery/jquery.form.js" type="text/javascript"/></script>
               <kagutags:user_info_form_validate 
                  form_name="login_form_footer"
                  email="Y"
                  password="Y"
                  password_required="true"
                  submit_handler="login('login_email_footer','login_password_footer')"/>
               <form id="login_form_footer" class="navbar-form pull-right">
                  <input id="login_email_footer" name="email" type="text" placeholder="Email"  maxlength="35" />
                  <input id="login_password_footer" name="password" type="password" placeholder="Password" maxlength="22" />
                  <button id="login" class="btn" type="submit">Login</button>
               </form>
               <span class="brand pull-left">Sign in with</span>                                            
               <form class="navbar-form  pull-left" id="sign_in_facebook_footer" action="/signin/facebook" method="POST">
                  <input type="hidden" name="scope" value="email" />                                                
                  <button class="btn" onclick="signInWithSocialAccount('sign_in_facebook_footer'); return false;" type="submit"><i class="icon-facebook"></i> Facebook</button>                                            
               </form>
               <span class="brand  pull-left"></span>   
               <form class="navbar-form  pull-left" id="sign_in_google_footer" action="/signin/google" method="POST">
                  <input type="hidden" name="scope" value="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo#email https://www.googleapis.com/auth/plus.me" />
                  <button class="btn pull-left" onclick="return signInWithSocialAccount('sign_in_google_footer');" type="submit"><i class="icon-google-plus"></i> Google+</button>                                              
               </form>
               <div style="text-align:center; margin-left:25px;" class="brand pull-left">OR</div>
            </div>
         </div>
      </div>
   </c:if>
      <div class="clearfix"></div>
         <div class="container-outer container-rounded">
         <div class="container-inner  global-margin-top global-margin-bottom" >
<!--       <img class="pull-right" style="margin-bottom: -5px" src="/static/images/filler2.png" /> -->
<!--       <div class="clearfix"></div> -->
<%--       <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='' /> --%>
         <ul class="footer-links" style="margin-left: 80px;">
            <li ><a href="/about">About</a></li>
            <li><a href="/help">Help</a></li>
            <li><a href="/tos">Terms Of Use</a></li>
            <li><a href="/privacy">Privacy</a></li>
            <li><a href="/events/browse">Events</a></li>
            <li><a href="/ads/browse/for_sale">For Sale</a></li>
            <li><a href="/ads/browse/community_services">Services</a></li>
            <li><a href="/discussions/browse/categories">Discussions</a></li>
         </ul>
        <div class="clearfix"></div>
      </div>
   </div>
   <div style="min-height:70px"></div>
</footer>
