<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>

<div class="global-margin-top" style="width: 300px;">
    <hr class="kagu-hr">

    <div class="pull-left"  style="margin: 0px 20px 0px 30px; padding: 3px 0px 0px 0px;">
    <div class="fb-like" data-send="false" data-layout="button_count" data-width="450" data-show-faces="false" data-font="arial"></div>
    </div>
    <div class="pull-left"  style="margin: 3px 20px 0px 0px; padding: 0px;">
    <a data-pin-config="beside" data-pin-do="buttonBookmark" href="//pinterest.com/pin/create/button/"><img src="//assets.pinterest.com/images/PinExt.png" /></a>
    </div>
    <div class="pull-left"  style="margin: 0px 0px 0px 0px; padding: 3px 0px 0px 0px;">
    <a href="https://twitter.com/share" class="twitter-share-button" data-hashtags="kagubuzz">Tweet</a>
    </div>
    <div class="clearfix"></div>

    <hr class="kagu-hr">
</div>

<!-- Twitter -->

<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>

<!-- Facebook -->

<script>
    window.fbAsyncInit = function() {
        FB.init({
            appId : '170483136408892', // App ID
            channelUrl : '//www.kagubuzz.com/channel.html', // Channel File
            status : true, // check login status
            cookie : true, // enable cookies to allow the server to access the session
            xfbml : true
        // parse XFBML
        });
        
        // Additional initialization code such as adding Event Listeners goes here

      };

      // Load the SDK's source Asynchronously
      // Note that the debug version is being actively developed and might 
      // contain some type checks that are overly strict. 
      // Please report such bugs using the bugs tool.
      (function(d, debug){
         var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
         if (d.getElementById(id)) {return;}
         js = d.createElement('script'); js.id = id; js.async = true;
         js.src = "//connect.facebook.net/en_US/all" + (debug ? "/debug" : "") + ".js";
         ref.parentNode.insertBefore(js, ref);
       }(document, /*debug*/ false));
      
</script>
    
<div id="fb-root"></div>
<script>
        (function(d, s, id) {
          var js, fjs = d.getElementsByTagName(s)[0];
          if (d.getElementById(id)) return;
          js = d.createElement(s); js.id = id;
          js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=170483136408892";
          fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));
</script>

<!-- pint rest -->

<script src="//assets.pinterest.com/js/pinit.js"></script>
