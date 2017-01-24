<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8" />
      <kagutags:bootstrap_css/>
      <title>Kagu Buzz - Your Local Community Your Way</title>
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/eventadd.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/timepicker.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/datepicker.css" type="text/css" />
      <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
      <!--[if lt IE 9]>
      <script src="https://html5shim.googlecode.com/svn/trunk/html5.js"></script>
      <![endif]-->
   </head>

   <body>
      <kagutags:jquery />
      <kagutags:bootstrap_js/>
      <script src="/static/jquery/bootstrap-timepicker.js" type="text/javascript"></script>
      <script src="/static/jquery/bootstrap-datepicker.js" type="text/javascript"></script>
      <script src="/static/jquery/textarea-limit.js" type="text/javascript"/></script>
      <script src="/static/jquery/jquery.numeric.js" type="text/javascript"/></script>
      <script src="/static/jquery/fileuploader.js" type="text/javascript"></script>
      <script src="/static/jquery/jquery.validate.js" type="text/javascript"></script>

      <kagutags:redactor/>
      <div class="wrapper">
         <kagutags:header navactivebutton="event-it" headerpic="smallheader.jpg"></kagutags:header>
         <form method="post" id="mainform" class="form-horizontal" action="/eventreview">
            <div>
               <div class="buzzinfobox notitle">
                  <fieldset>
                     <legend>Creating a New Event Post For Your Community</legend>
                     <div class="buzzinfoboxpadding">
                        <div class="pull-left">
                           <div class="control-group">
                              <label class="control-label">What's The Address?</label>
                              <div class="controls">
                                 <input type="text" maxlength="75" class="required span3" id="address" value="${event.getAddress()}" name="address" placeholder="1313 Mockingbird Lane., Erie, PA" minlength="5" />
                              </div>
                           </div>
                           <div class="control-group">
                              <label class="control-label">When Does It Start?</label>
                              <div class="controls">
                                 <input style="width: 90px;" class="required" type="text" name="startTime" id="timepicker"> <span>on</span> <input
                                    style="width: 90px;" class="required is_future_date" type="text" name="startDate" id="datepicker">
                              </div>
                           </div>
                           <div class="control-group">
                              <label class="control-label">This Event Lasts For</label>
                              <div class="controls">
                                 <select name="duration" id="duration">
                                    <c:forEach var="eventduration" items="${eventdurations}">
                                       <option value="${eventduration.name()}">${eventduration.getEnumExtendedValues().getDescription()}</option>
                                    </c:forEach>
                                 </select>
                              </div>
                           </div>
                        </div>
                        <div class="pull-left">
                           <div class="control-group">
                              <label class="control-label" for="title">Give Your Event A Title</label>
                              <div class="controls">
                                 <input type="text" 
                                 maxlength="60" 
                                 class="required span3" 
                                 id="title" 
                                 name="title" 
                                 value="${event.getTitle()}" 
                                 placeholder="3 Days Of Peace And Music" 
                                 autocomplete="off" 
                                 title="Similar Events Found Posted Near You"/>                                
                              </div>
                           </div>
                           <div class="control-group">
                              <label class="control-label" for="eventCategory">Category</label>
                              <div class="controls">
                                 <select name="eventCategory" id="eventCategory" class="required span3">
                                    <option value="">Pick One</option>
                                    <c:forEach var="eventcategory" items="${eventcategories}">
                                       <option value="${eventcategory.getId()}">${eventcategory.getName()}</option>
                                    </c:forEach>
                                 </select>
                              </div>
                           </div>
                           <div class="control-group">
                              <label class="control-label" for="inputprice">Price</label>
                              <div class="controls">
                                 <div class="input-prepend input-append">
                                    <span class="add-on">$</span> <input class="span1" id="price" 
                                    value="<c:if test="${event.getPrice() != 0}">${event.getPrice()}</c:if>"
                                    name="price" placeholder="Free" maxlength="6" size="16" type="text"> <span
                                       class="add-on">.00</span>
                                 </div>
                              </div>
                           </div>
                        </div>
                     </div>
                     <div class="clearfix"></div>
                     <kagutags:event_query_options/>
                     <hr class="kagu-hr" />
                     <div style="text-align: center;">
                        <span> Type A Description Of Your Event Below</span>
                     </div>
                  </fieldset>
               </div>
            </div>
            <div class="clear-background muted">
               <span id="chars_remaining_messagebox0"></span>
               <kagutags:pop_over title="Posting an Event" template_name="goodEvent"></kagutags:pop_over>         
            </div>
            <div style="margin-top: 5px;">
               <textarea id="messagebox0" name="description" style="height: 300px;">${event.getBody()}</textarea>
            </div>
            <div class="clearfix"></div>
            <div class="global-margin-top">
               <button class="btn btn-primary pull-right" onclick="return checkRedactorInput('messagebox0');" type="submit">
               Review Your Post <i class="icon-chevron-right icon-white"></i>
               </button>
               <button class="btn btn-info pull-left" onclick="if(!checkRedactorInput('messagebox0')) return; saveDraft();" type="button">
               Save as a Draft 
               </button>
               <div class="clearfix"></div>
            </div>
         </form>
         <kagutags:footer />
      </div>
            <script src="/static/jquery/event-popover-similar.js" type="text/javascript"></script>
      <script type="text/javascript">
         
         $("a[rel=popover]").popover({trigger:'hover'});
         
         $(document).ready(function() {
             $('#messagebox0').redactor({ autoresize: true ,minHeight: 200,  buttons: ['bold', 'italic','underline','fontcolor','backcolor','|',
                                                                                       'unorderedlist', 'orderedlist', '|',
                                                                                       'image', 'video', 'link', '|', 'alignment', '|', 'horizontalrule'] });
             
              monitorRedactorInput(250, 25000, 'messagebox0');
             
             $('#timepicker').timepicker();
             $('#datepicker').datepicker();
         
             
             $('#timepicker').val('<fmt:formatDate pattern="hh:mm a" value="${default_date}"/>');
             $('#datepicker').val('<fmt:formatDate pattern="MM/dd/yyyy" value="${default_date}"/>');
             
             $.validator.addMethod("is_future_date", 
                 function(value, element) {         
                 var date=new Date(value);     
                 var _now=new Date();
                 if(date.getTime()>=_now.getTime()) {
                     return true;
                 }
                 return false;
             }, "Date must be in the future");
             
             $("#mainform").validate({
                 onfocusout: false,
                 errorClass: 'kagu-red',
                 submitHandler: function() { checkForm() },
                 showErrors: function(errorMap, errorList) {
                     this.defaultShowErrors();
                 }
             });
             
             <c:if test="${not empty event}">
                 $('#timepicker').val('<fmt:formatDate pattern="hh:mm a" value="${formatter.gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset())}"/>');
                 $('#datepicker').val('<fmt:formatDate pattern="MM/dd/yyyy" value="${formatter.gmtDateToTzDate(event.getStartDate(), user.getTimeZoneOffset())}"/>');
                 $('#repeats').val('${event.getEventCycle().name()}');
                 $('#duration').val('${event.getEventDuration().name()}');
                 $('#eventCategory').val('${event.getCategory().getId()}'); 
                 $('input:radio[name=age]').filter('[value=${event.getAgeAppropriate().name()}]').attr('checked', true);
                 $('input:radio[name=venue]').filter('[value=${event.getVenue().name()}]').attr('checked', true);
             </c:if>
             
             $('#price').numeric(false);
         
         });
         
         function saveDraft() {
             
             if(!$("#mainform").valid()){
                 return;
             }
         
                $.ajax({
                    type: 'POST',
                    url:  'eventsavedraft',
                    data: { title: $('#title').val(),
                         eventCategory : $('#eventCategory').val(),
                         startDate : $('#datepicker').val(),
                         startTime : $('#timepicker').val(),
                         description : $('#messagebox0').val(),
                         duration : $('#duration').val(),
                         // optional parameters
                         eventsidebarimage : $('#eventsidebarimage').val(),
                         eventbackgroundimage : $('#eventbackgroundimage').val(),
                         price : $('#price').val(), 
                         ages : $('#ages').val(),
                         takesplace : $('#takesplace').val(), 
                         repeats : $('#repeats').val(),
                         address : $('#address').val(),
                         kw1 : $('#kw1').val(),
                         kw2 : $('#kw2').val(),
                         kw3 : $('#kw3').val(),
                         kw4 : $('#kw4').val(),
                         kw5 : $('#kw5').val() },
                    dataType: "json",
                    success: function(data) {                                                   
                        toastr.success(data.message, data.title);
                    }});  
         }
         
      </script>
   </body>
</html>
