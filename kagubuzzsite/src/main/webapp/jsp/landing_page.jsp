<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8" />
      <title>Kagu Buzz - Your Local Community Your Way</title>
      <kagutags:bootstrap_css />
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/login.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/featurette.css" type="text/css" />
      <style type="text/css">
         .validate_ignore {
         }
         .modal {
         width: 480px;
         }
      </style>
      <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
      <!--[if lt IE 9]>
      <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
      <![endif]-->
   </head>
   <body>
      <kagutags:jquery />
      <kagutags:bootstrap_js/>
     <script type="text/javascript" src="https://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
      <kagutags:user_info_form_validate 
         form_name="create_account" 
         first_name="Y" 
         password="Y" 
         password_required="true" 
         re_password="Y"
         zip_code="Y" 
         email="Y" 
         submit_handler="getCaptcha(form)" 
         error_handler="$('button[data-loading-text]').button('reset')"  />
      <kagutags:user_info_form_validate 
         form_name="login_form" 
         email="Y" 
         password="Y" 
         password_required="true" 
         submit_handler="login('login_email','login_password')"
         error_handler="$('button[data-loading-text]').button('reset')" />
      <div class="landing-wrapper">
           
      <kagutags:header navactivebutton="home" dont_ask_zip="Y" headerpic="smallheader.jpg"></kagutags:header>         
      <img style="margin: 15px 0px 0px 100px;" src="/static/images/landingpage.png"/>     
      <div style="margin-top: 10px; text-align: center;" class="lead"><a href="#1000_posts" class="darkgrey"><img  src="/static/images/postbuttonicon.png" /> One Thousand Posts - Help Us Reach Our Goal!</a></div>
      <div class="main-content" style="margin-left: 200px;">
         <div class="signup-wrapper pull-left">
            <a href="#welcome_anchor" class="lead darkgrey pull-left"><img src="/static/images/green_circle.png" /><span>hat's Kagu Buzz?</span>  </a>                                                                 
            <img src="/static/images/signuptopper.png" alt="grass"/>
            <kagutags:infoboxtop cssclass="buzzinfoboxtitle" collapsed="flase" collapseable="false" title='' />
            <span class="lead">Create An Account</span>
            <hr class="kagu-hr">
            <form method="POST" id="create_account" action="/createaccount" style="margin: 5px">
               <fieldset>
                  <input type="text" id="first_name"  name="first_name" maxlength="12" placeholder="First Name"> 
                  <input type="text" id="email" name="email" maxlength="35" placeholder="Email"> 
                  <input type="password" id="password" autocomplete="off" name="password" maxlength="22" placeholder="Password"> 
                  <input type="password" id="re_password" autocomplete="off" name="re_password" maxlength="22" placeholder="Re-enter Password"> 
                  <input type="text" id="zip_code" name="zip_code" maxlength="5" placeholder="Zip Code"> 
                  <label class="muted checkbox">
                  <span><small>I agree to the <a href="/tos"> Terms Of Use</a></small></span>
                  <input type="checkbox" class="checkbox inline" id="agree" name="agree" required> 
                  </label>
                  <input type="hidden" id="recaptcha_challenge_field_submit" name="recaptcha_challenge_field_submit" autocomplete="off"> 
                  <input type="hidden" id="recaptcha_response_field_submit" name="recaptcha_response_field_submit" autocomplete="off"> 
                  <hr class="kagu-hr">
                  <button type="submit" class="btn pull-right" data-loading-text="One Moment..." autocomplete="off">Create Account</button>
               </fieldset>
            </form>
            <kagutags:infoboxbottom />
         </div>
         <div class="five-px-padding pull-left">
            <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <br> <span
               class="lead">OR</span>
         </div>
         <div class="signup-wrapper pull-left">
            <a href="#getting_started" class="lead darkgrey pull-left"> <img  src="/static/images/pink_circle.png" />hat's Inside? </a>
               
            <img src="/static/images/landingfiller.png" /> 
            <kagutags:infoboxtop cssclass="buzzinfoboxtitle" collapsed="flase" collapseable="false" title='' />
            <span class="lead">Sign In With</span>
            <hr class="kagu-hr">
            <form id="login_form" style="margin: 5px 5px 0px 5px">
                <fieldset>
                    <input id="login_email" name="email" type="text" placeholder="Email" maxlength="35" /> 
                    <label id="login_error"></label>
                    <input id="login_password" name="password" type="password" placeholder="Password" maxlength="22" />
                    <button id="login" class="btn" type="submit" data-loading-text="One Moment..." autocomplete="off">Login</button>
                    <div style="margin-top: 10px"><small class="pull-rght"><a href="/reset_password_request" >I Forgot My Kagu Buzz Password</a></small></div>
               </fieldset>
            </form>
            <hr class="kagu-hr">
            <div class="pull-left">
               <form action="/signin/facebook" id="sign_in_facebook" method="POST" style="margin: 0px 5px 5px 5px">
               	  <input type="hidden" name="scope" value="email" />  
                  <button class="btn" onclick="signInWithSocialAccount('sign_in_facebook'); return false;" type="submit" data-loading-text="One Moment..." autocomplete="off">
                  <i class="icon-facebook"></i> Facebook
                  </button>
               </form>
            </div>
            <div class="pull-right">
               <form action="/signin/google" id="sign_in_google" method="POST" style="margin: 0px 5px 5px 5px">
                  <input type="hidden" name="scope"
                     value="https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo#email https://www.googleapis.com/auth/plus.me" />
                  <button class="btn pull-left" onclick="signInWithSocialAccount('sign_in_google'); return false;" type="submit" data-loading-text="One Moment..." autocomplete="off">
                  <i class="icon-google-plus"></i> Google+
                  </button>
               </form>
            </div>
            <kagutags:infoboxbottom />
            <kagutags:social_media/>
            <img class="pull-right" src="/static/images/cloudgroup.png" />               
         </div>
         <div class="clearfix"></div>         
      </div>     
      <div class="clearfix"></div>     
      <img   class="pull-right" style="margin-bottom: -5px" src="/static/images/filler2.png" />
      <div id="welcome_anchor" class="clearfix"></div>
      <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='' />
      <div style="background-image: url(static/images/landing_image_1.jpg); padding: 10px; margin: -10px 0px -5px 0px;">
         <img class="pull-right" src="/static/images/cloudgroup.png" />
         <h2 class="featurette-heading">
            Welcome to Kagu Buzz. <br> <br> <span class="text-warning">What's Kagu Buzz? </span>
         </h2>
         <p class="lead" >
            <br> <span><strong>Let&rsquo;s answer that with a question. </strong></span> 
            <br>
            <br>
            What do you use or where do you go
            to find out what&#8217;s happening in your local community? While you're thinking about your answer, hundreds of events may be going on
            within five miles of where you live. These unique events, like garage sales, book readings, plays at the community center, or trivia
            night at the local bar, are occurring right now, and you probably aren't aware of them.
            <br />
            <br />
            What about when you want to buy or sell something? Do you go to a local website with flaky sellers or buyers who may not show up? Or do you use a large worldwide website and hope the item eventually gets to your door? Kagu Buzz connects you with others in your community who want to buy, sell, or exchange items or services without any headaches.
            <br />
            <br />
            What about information or recommendations? Kagu Buzz also provides a unique and comprehensive discussion area, so you can chat with others in your community about local events, current events, or whatever you&#39;d like to discuss.
            <br />
            <br />
            Kagu Buzz puts what's happening and available in your local community in one place, helping you find out about what's going on around you.
            <br> <br>
            <strong>So, what's Kagu Buzz?</strong> It's you, your neighbor, your local coffee shop, and everyone that makes up your community interacting to make it
            your community - your way.
            <br> <br> <i class="kagu-red">Have Fun!</i><br> The Team at Kagu Buzz 
            <img src="/static/images/beetrail.png" />
            <img id="getting_started"   style="margin: -18px 0px 0px 0px;" class="pull-right" src="/static/images/landingfiller.png" alt="filler"/>
         </p>
      </div>
      <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='' />
      <h2 class="featurette-heading">What&rsquo;s inside?</h2>
      <p class="lead"><strong>Why Kagu Buzz is different.</strong></p>
      <div class="media">
         <a class="pull-left" href="/ads/browse"> <img class="media-object" src="/static/images/findit.png" alt="ads">
         </a>
         <div class="media-body">
            <h4 class="media-heading">Find It</h4>
            Click here to see items for sale, as well as services offered or requested. Unlike other sites, at Kagu Buzz we 
            use a special user rating system that allows you to make an informed decision before moving forward with an exchange. 
            If you&rsquo;re interested in an item, you make an offer through the site. You  reveal only the information that you choose,
             or none at all. Buyers and sellers are able to set a preferred neutral meeting location, which becomes visible to 
              both parties only if the offer is accepted. <strong>All these features combined help protect you from weirdos, 
              spam, scams and flakes. </strong> 
          
         </div>
      </div>
      <div class="media">
         <a class="pull-left" href="/events/browse"> <img class="media-object" src="/static/images/attendit.png" alt="events">
         </a>
         <div class="media-body">
            <h4 class="media-heading">Attend It</h4>
            Click here to find events posted by members in your local area, along with event listings you've added through Event it. Kagu Buzz event posts have more information and details than you&rsquo;ll find on other sites, where often all you discover is where and when the event takes place. Come to Kagu Buzz to find out more details, such as what the event is, the price, background on the venue, and more.                
         </div>
      </div>
      <div class="media">
         <a class="pull-left" href="/discussions/browse/categories"> <img class="media-object" src="/static/images/discussion.png" alt="discussions">
         </a>
         <div class="media-body" id="1000_posts">
            <h4 class="media-heading">Discuss It</h4>
            Click here to start a discussion or ask a question (such as &ldquo;Where&rsquo;s the best pizza in Oakland&rdquo;?) or respond to a discussion topic. Unlike other sites, discussion posts require more than just a few words. Use Kagu Buzz when you want discussions with a lot more than just sentiments such as &ldquo;I agree, dude.&rdquo; 
         </div>
      </div>
      <c:if test="${show_campaign}">
      <br><br>
       <kagutags:infoboxtitle cssclass="buzzinfoboxtitle title-simple" title='' />
      <h2 class="featurette-heading">One-Thousand Posts Campaign</h2>
      <p class="lead"><strong>Help us reach our goal of one-thousand for sale posts!</strong></p>
      <div class="media">
         <a class="pull-left" href="/ad/create"> <img class="media-object" src="/static/images/postbutton.png" alt="ads">
         </a>
         <div class="media-body">
            <p class="lead">
                After almost two years of work, we're finally launching Kagu Buzz beta! Help us celebrate the launch by posting something
                for sale. Instead of a Kickstarter campaign, we're doing a 'One-Thousand Posts' campaign and are attempting to get to one-thousand
                for sale posts by December 15th. Can we do it? With your help, we think we can!  I mean, come on, it's not
                 that many posts, is it?<br> <br>  

                Thank you so much for your help!<br>         
                The Team at Kagu Buzz - Jimmy, Marvin, Thone and Rachel</p></div>
                <h1> Ad IT Posts So Far: ${ad_count}, Days Remaining: ${days_remaining}</h1>
        </div>
        </c:if>
      <kagutags:footer />
      </div>
      <script>
         $(document).ready(function() {  
           <c:if test="${not empty captcha_error}">
             renderServerNotification(${captcha_error});
           </c:if>
              $('button[data-loading-text]').click(function () { $(this).button('loading'); });    
         });
      </script>
   </body>
</html>
