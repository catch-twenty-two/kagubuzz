<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html lang="en">
   <head>
      <kagutags:bootstrap_css/>
      <link rel="stylesheet" href="/static/css/main.css" type="text/css" />
      <link rel="stylesheet" href="/static/css/bootstrap-tagmanager.css" type="text/css" />
      <meta charset="utf-8" />
      <title>Kagu Buzz - Your Local Community Your Way</title>
   </head>
   <body>
      <kagutags:jquery/>
      <kagutags:bootstrap_js/>
      <kagutags:redactor/>
      <script src="/static/jquery/fileuploader.js" type="text/javascript"/></script>
      <script src="/static/jquery/jquery.numeric.js" type="text/javascript"/></script>
      <script src="/static/jquery/jquery.validate.js" type="text/javascript"/></script>
      <script src="/static/jquery/textarea-limit.js" type="text/javascript"/></script>
      <script src="/static/jquery/jquery.form.js" type="text/javascript"/></script>
      <div class="wrapper">
         <kagutags:header navactivebutton="ad-it" headerpic="smallheader.jpg"/>
         <form accept-charset="utf-8" method="post" id="mainform" class="form-horizontal">
            <div class="container-one-column container-outer container-rounded-top">
                         <span class="lead title">Posting An Exchange To The Urban Gardening Group In Berkeley, Ca</span>
               <img class="pull-left" style="margin-right: 10px;" alt="" src="/static/images/create_exchange_bw.png">
               <div class="clearfix"></div>  
               <div class="container-inner global-margin-top">
                  <div class="global-margin-bottom global-margin-top pull-left">
                     <label>Title</label>                       
                     <input class="span4 required" type="text" maxlength="60" name="title" value="${ad.getTitle()}" placeholder="Walk In My Gently Used Shoes For A Mile!"/>
                  </div>
                  <div class="global-margin-bottom global-margin-top  pull-left" style="margin-left: 10px;">
                     <div id="compensation" >
                        <label>Compensation</label>
                        <div class="input-prepend input-append pull-left inline">
                           <span class="add-on">$</span> <input class="span1" id="price" 
                           value="<c:if test="${ad.getPrice() != 0 && ad.getPrice() != null}">${ad.getPrice()}</c:if>"
                           id="price" name="compensation" placeholder="Free" maxlength="6" size="19" type="text"> 
                           <span class="add-on">.00</span>                                                                             
                        </div>
                     </div>
                  </div>
                  <div class="global-margin-bottom global-margin-top pull-right" >
                     <label>Exchange Type</label>
                     <select name="ad_group" id="ad_group" class="selectpicker show-tick show-menu-arrow span2">
                        <optgroup label="Commuinity Services">
                           <option  data-subtext="Wanted" value="Wanted">Service</option>
                           <option  data-subtext="Offered" value="Offered">Service</option>
                        </optgroup>
                        <optgroup label="Merchandise">
                           <option value="ForSale">For Sale</option>
                        </optgroup>
                     </select>
                  </div>
                  <div class="global-margin-bottom global-margin-top pull-left" style="margin-left: 10px;">
                     <label style="visibility: hidden;">&nbsp;</label>
                     <button id="image_panel_toggle" type="button" class="btn" data-toggle="button" data-target="#add_images">
                     Add Images
                     </button>
                  </div>
                  <div class="clearfix"></div>
                  <label>Description</label>
                  <textarea id="messagebox0" id="description">${ad.getBody()}</textarea>
                  <div class="global-margin-top">
                  <div id="work_unit_div" class="pull-right">
                     <input type="checkbox" id="per_hour"  name="per_hour"> Per Hour                            
                     <input type="checkbox" id="accept_timebanking" name="accept_timebanking">
                     Allow <a href="http://omnui.com/Home/About" target="_blank">Omnui</a> As A Payment Option
                     <kagutags:pop_over title="What Can I Do With Time Banking?" template_name="timeBanking"/>
                  </div>
                    <div class="pull-left">
                     <input type="text" 
                        autocomplete="off" 
                        data-items="3"
                        data-provide="typeahead" 
                        name="tags" 
                        placeholder="Ex: Apples" 
                        style="width:9em;" 
                        class="input-medium tagManager"
                        data-original-title=""/> </div>

                  <div class="clearfix"></div>
                                          </div>
                  <hr class="style-one">
                                   
                   
                  <div class="global-margin-top global-margin-bottom">
                     <button id="review_btn" class="btn btn-primary pull-right " type="button">
                     Review Your Post
                     </button>
                     <button id="save_btn" class="btn btn-info pull-left" type="button">
                     Save as a Draft 
                     </button>
                     <div class="clearfix"></div>
                  </div>
               </div>
<!--                <div> -->
<!--                   <span id="chars_remaining_messagebox0"></span> -->
<%--                   <kagutags:pop_over title="Posting A Good Exchange" template_name="goodAd"/> --%>
<!--                </div> -->
<input type="hidden" name="ad_type" id="ad_type" value="">
            
                  <input class="span1 required" type="hidden" id="zip_code" maxlength="5" name="zip_code" value="${user.getZipCode()}"/>
      
               <div id="add_images" class="collapse">
                  <div id="thumbnailholder" style="text-align: center; margin-left: 35px;">
                     <ul class="thumbnails global-margin-top">
                        <kagutags:image_uploader 
                           captiontitle="Upload a Picture (Optional)"
                           height="140" 
                           width="180" 
                           imagename="adimage1" 
                           defaultimageurl="${image1}"/>
                        <kagutags:image_uploader
                           captiontitle="Upload a 2nd Picture (Optional)"
                           height="140" 
                           width="180" 
                           imagename="adimage2" 
                           defaultimageurl="${image2}"/>
                        <kagutags:image_uploader 
                           captiontitle="Upload a 3rd Picture (Optional)"
                           height="140" 
                           width="180" 
                           imagename="adimage3" 
                           defaultimageurl="${image3}"/>
                        <kagutags:image_uploader
                           captiontitle="Upload a 4th Picture (Optional)" 
                           height="140" 
                           width="180" 
                           imagename="adimage4" 
                           defaultimageurl="${image4}"/>
                     </ul>
                  </div>
               </div>
            </div>
         </form>
         <kagutags:footer/>
      </div>
            <script src="/static/jquery/bootstrap-tagmanager.js" type="text/javascript"/></script>
      <kagutags:type_ahead ajaxurl="/discussions/browse/type_ahead_tags"/>
      <script type="text/javascript">
         $("a[rel=popover]").popover({trigger:'hover'});
            
            var catJobs = [${jobcategories}];
            var catForSale = [${forsalecategories}];
            var catHousing = [${housingcategories}];
            var catServices = [${servicescategories}];
         
            $("#ad_group").change(function() {
             setDomForAdGroup($(this).val()) 
            }); 
            
            $('#add_images').fadeTo(0, 0);
            
            $('#add_images').on('hidden', function () {            	
             $('#add_images').fadeTo(100, 0).removeClass("container-inner");;
            })
            
            $('#add_images').on('shown', function () {
            	$('#add_images').addClass("container-inner");
                $('#add_images').fadeTo(600, 1);
            })   
            
            $("#image_panel_toggle").click(function() {
             $('#add_images').collapse('toggle');    
            });
            
               function updateCategories(items) {
                   $('#ad_category').empty();
                   for(var i = 0 ; i < items.length; i++) {             
                       $('<option value="'+ items[i] +'">' + items[i] + '</option>').appendTo($('#ad_category'));
                   }
               }
               
               function setDomForAdGroup(adGroup) {
                $("#ad_type").val("Offered");
                   switch(adGroup) {
                   case "HousingAndRealEstate":
                       //updateCategories(catHousing);
                       break;
                   case "Wanted":
                    $("#ad_type").val("Wanted");
                   case "Offered":
                       //updateCategories(catServices);
                       $('#caption_title_adimage2').html('Upload Your Logo Or An Image');
                       $('#caption_title_adimage3').html('Upload A Background Image');
                       $('#imageuploaderadimage1').fadeTo(600, 0);
                       $('#imageuploaderadimage4').fadeTo(600, 0); 
                       $('#imageuploaderadimage4 input[type=file]').hide();
                       $('#imageuploaderadimage1 input[type=file]').hide();
                       $("#firm").prop('checked',true);
                       $("#price").attr('placeholder','None');
                       $("#location_address").hide();
                       $("#location_zip_code").show();
                       $("#ad_type_div").show();
                       $("#per_time_unit").show();
                       $("#work_unit_div input").attr('disabled',false);
                       
                       if("${image2}" == "") {
                          $('#imageadimage2').prop('src', '/static/images/blankimagesidebar.jpg');
                       }
                       
                       if("${image3}" == "") {
                          $('#imageadimage3').prop('src', '/static/images/blankbackgroundupload.jpg');
                       }
                       
                       break;
                   case "Jobs":
                       //updateCategories(catJobs);
                       break;
                   case "ForSale":
                   default: 
                    //updateCategories(catForSale);
                       $('#caption_title_adimage2').html("Upload a 2nd Picture (Optional)");
                       $('#caption_title_adimage3').html("Upload a 3rd Picture (Optional)");
                       $('#imageuploaderadimage1').fadeTo(600, 1);
                       $('#imageuploaderadimage4').fadeTo(600, 1); 
                       $('#imageuploaderadimage4 input[type=file]').show();
                       $('#imageuploaderadimage1 input[type=file]').show();
                       $("#firm").prop('checked',false);
                       
                       $("#price").attr('placeholder','Free');
                       $("#ad_type_div").hide();
                       $("#location_address").hide();
                       $("#location_zip_code").show();
                       $("#per_time_unit").hide();
                       $("#work_unit_div input").attr('disabled',true);
                       
                       if("${image2}" == "") {
                          $('#imageadimage2').prop('src', '/static/images/adblankimage2.jpg');
                       }
                       
                       if("${image3}" == "") {
                          $('#imageadimage3').prop('src', '/static/images/adblankimage3.jpg');
                       }
                       
                       break;
                   }
               }      
               
               function validateForm() {             
                   if(!checkRedactorInput('messagebox0')) return false;              
                   if(!$("#mainform").valid()) return false;
                   
                   return true;
               }
               
               $('#review_btn').click(function () {
                
                   if(!validateForm()) return;
                   
                   $.ajax({
                       type: "POST",
                       url: "/ad/save",
                       data: $("#mainform").serialize(), 
                       success: function(data) {
                        if(data.alertType != "error") {
                             window.location.href = '/ad/review';
                        } else {
                             renderServerNotification(data);
                        }
                       }
                     });
               });
               
               $('#save_btn').click(function() { 
                
                if(!validateForm()) return;
                  
                   $.ajax({
                       type: "POST",
                       url: "/ad/save",
                       data: $("#mainform").serialize(), 
                       success: function(data) {
                        renderServerNotification(data); 
                       }
                     });
               });     
               function notifyRemoved(name){}
               function notifyAdded(name){}
               $(document).ready(function() {         
                   
                   
                  $('.selectpicker').selectpicker({
                   size: 4,
                   showSubtext: true
                   });
                   
                   
                   $('#messagebox0').redactor({ autoresize: true ,minHeight: 300,  buttons: ['bold', 'italic','underline','fontcolor','backcolor','|',
                                                                                             'unorderedlist', 'orderedlist', '|',
                                                                                             'image', 'video', 'link', '|', 'alignment', '|', 'horizontalrule'] });
                   
                   monitorRedactorInput(150, 25000, 'messagebox0');
                  
                   $("#mainform").validate({
                       onfocusout: false,
                       errorClass: 'kagu-red',
                       submitHandler: function() { checkForm() },
                       showErrors: function(errorMap, errorList) {
                           this.defaultShowErrors();
                       }
                   });      
                   
                   $('#price').numeric(false);
                   $('#zip_code').numeric(false);
                   
                   $('input:radio[name=ad_group]').filter('[value=${ad.getAdGroup().name()}]').attr('checked', true);
                   $('input:radio[name=ad_type]').filter('[value=${ad.getAdType().name()}]').attr('checked', true);
                   
                   setDomForAdGroup('${ad.getAdGroup()}');
                   
                   $('#ad_category').val('${ad.getCategory().getName()}');             
                   $("#firm").prop('checked', '${formatter.isChecked(ad.isFirm())}');
                   $("#accept_timebanking").prop('checked', '${formatter.isChecked(ad.acceptsTimebanking())}');
                   $("#per_hour").prop('checked', '${formatter.isChecked(ad.isPerHour())}');
                   
                   
                   jQuery(".tagManager:eq(0)").tagsManager({
                       preventSubmitOnEnter: true,
                       typeahead: true,
                       prefilled: 'Urban Gardening',
                       typeaheadSource: typeAheadHelper,
                       blinkBGColor_1: '#FFFF9C',
                       blinkBGColor_2: '#CDE69C',
                       hiddenTagListName: 'post_tags'
                     });
                     

               });
            
      </script>
   </body>
</html>