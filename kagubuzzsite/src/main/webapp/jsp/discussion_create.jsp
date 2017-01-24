<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8" />
      <kagutags:bootstrap_css/>
      <link rel="stylesheet" href="/static/css/bootstrap-tagmanager.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <title>Kagu Buzz - Your Local Community Your Way</title>
   </head>
   <body>
      <kagutags:jquery/>
      <kagutags:bootstrap_js/>
      <script src="/static/jquery/textarea-limit.js" type="text/javascript"/></script>
      <script src="/static/jquery/jquery.validate.js" type="text/javascript"></script>
      <kagutags:redactor/>
      <kagutags:header navactivebutton="ad-it" headerpic="smallheader.jpg"></kagutags:header>
      <div class="wrapper">
         <div class="container-one-column container-outer container-rounded-top container-stacked">
                          <span class="lead title">Posting A New Discussion Topic To The Urban Gardening Group In Berkeley, Ca</span>
               <img class="pull-left" style="margin-right: 10px;" alt="" src="/static/images/add_discussion.png">
               <div class="clearfix"></div>
            <form method="post" id="mainform"  action="/discussion/post">
               <div class="container-inner global-margin-top">
                  <label class="">Subject</label>
                  <input id="subject"
                     class="span4 required"
                     maxlength="100" 
                     type="text" 
                     name="subject" 
                     placeholder=""/>
                     <label>What&lsquo;s on your mind? Type it below.</label>
                  <textarea id="messagebox0" name="message" style="height: 300px;"></textarea>
                                    <hr class="style-one">
                  <div class="global-margin-top">

                     <input type="text" 
                        autocomplete="off" 
                        data-items="3"
                        data-provide="typeahead" 
                        name="tags" 
                        placeholder="Ex: Apples" 
                        style="width:9em;" 
                        class="input-medium tagManager"
                        data-original-title=""/>
                     <button class="btn btn-primary pull-right " onclick="return checkRedactorInput();" type="submit">Post</button>
                     <div class="clearfix"></div>
                  </div>
               </div>
            </form>
<!--             <div class="lead"> -->
<!--                <span id="chars_remaining_messagebox0"></span> -->
<!--             </div> -->
         </div>
         <kagutags:footer/>
      </div>
      <script src="/static/jquery/bootstrap-tagmanager.js" type="text/javascript"/></script>
      <kagutags:type_ahead ajaxurl="/discussions/browse/type_ahead_tags"/>
      <script>
         $(document).ready(function() { 
             $("#mainform").validate({ errorClass: 'kagu-red', submitHandler: function() { checkForm() }, showErrors: function(errorMap, errorList) { this.defaultShowErrors(); }});
         });
         
         $('#messagebox0').redactor({ autoresize: false, resize : false });
         
         monitorRedactorInput(100, 3000, 'messagebox0');
         
         jQuery(".tagManager:eq(0)").tagsManager({
             preventSubmitOnEnter: true,
             typeahead: true,
             typeaheadSource: typeAheadHelper,
             prefilled: 'Urban Gardening',
             blinkBGColor_1: '#FFFF9C',
             blinkBGColor_2: '#CDE69C',
             hiddenTagListName: 'post_tags'
           });
           
         function notifyRemoved(name){}
         function notifyAdded(name){}
      </script>
   </body>
</html>
