<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="address" required="false" type="java.lang.String" %>
<%@ attribute name="zip_code" required="false" type="java.lang.String" %>
<%@ attribute name="kagulocation" required="false" type="com.kagubuzz.datamodels.hibernate.TBLKaguLocation" %>
<%@ attribute name="showdirectionsonload" required="true" type="java.lang.String" %>
<%@ attribute name="zoom" required="true" type="java.lang.Integer" %>
<%@ attribute name="markertag" required="false" type="java.lang.String" %>
<%@ attribute name="save_lat_long_url" required="false" type="java.lang.String" %>
<%@ attribute name="post_id" required="false" type="java.lang.String"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<script src="//maps.googleapis.com/maps/api/js?key=AIzaSyByHm3yjjauqhDz6NvNNsL_KOksafXz_dY&amp;sensor=true"></script>
<script src="/static/jquery/infobubble.js" type="text/javascript"></script>
<div class="map_canvas_box">
<div id="map_canvas"></div>
</div>
<div class="global-margin-top" style="text-align:center;">
   <div id="locationname" style="hidden:true;" ></div>
   <div id="directions" style="hidden:true;"></div>
</div>

<script type="text/javascript">
   var isZipCode;
   var geocoder;
   var map;
   var zoomsetting = ${zoom};
   var showdirectionsonload = '${showdirectionsonload}';
   var marker;
   var searchRadius = new google.maps.Circle({strokeColor:"green", 
                                           strokeWeight: 1});
       
   <c:if test="${not empty markertag}">
    infoBubble = new InfoBubble({
        content: '<div id="marker_badge" class="badge badge-inverse">${markertag}</div>',
        hideCloseButton: true,
        padding: 0,
        borderWidth: 0,
        backgroundColor: 'black',
        shadowStyle: 1,
        arrowStyle: 2,
        arrowPosition: 30,
    });
   </c:if>
   
   $(document).ready(function() 
   {
	   var zipCode = '${zip_code}';
	   var address = '${address}';
	   var kaguLocation = '${kagulocation.getCityState()}';	   	   
	   
	   if(zipCode != "") {
		   codeAddress(zipCode);
		   showDirectionsOnLoad('${kagulocation.getCityState()}');
		   return;
	   }
	   
	   if(address != "") {
		    codeAddress(address);
		    showDirectionsOnLoad(address);
		    return;
       }
	   
	   if(kaguLocation != "") {
	        mapLongLat('${kagulocation.getLatitude()}',
	                   '${kagulocation.getLongitude()}', 
	                   '${kagulocation.getCityState()}');
	        
	        showDirectionsOnLoad('${kagulocation.getCityState()}');
	        return;
	   }
	   
   });
   
   function showDirectionsOnLoad(pointOfInterest) {
	   
	    if(showdirectionsonload != "Y") {
	    	return;
	    }	    

	    $("#locationname").replaceWith('<div id="locationname"><span>'+ pointOfInterest + '<strong></strong></span><br></div>');	    
	    $("#directions").replaceWith('<div id="directions"><a href="http://maps.google.com/maps?saddr=${user.getZipCode()}&daddr=' + pointOfInterest + '" target="_blank">Get Directions</a></div>');
   }
   
   function codeAddress(address, zoom) {
	   
	if(!address) return;
	   
    if(zoom != null) zoomsetting = zoom;
    
    geocoder = new google.maps.Geocoder();
     
    geocoder.geocode( {"address": address}, function(results, status) {
         <c:if test='${not empty save_lat_long_url}'>
             $.ajax({
               type: "POST",
               url:  '${save_lat_long_url}',
               data: { longitude: results[0].geometry.location.lng(), latitude: results[0].geometry.location.lat(), id: '${post_id}'},
               dataType: "json"
            });    
           </c:if>
      
        if (status == google.maps.GeocoderStatus.OK) {
   
            var marker;
   
            var mapOptions = {
                zoom: zoomsetting,
                center: results[0].geometry.location,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
    
            map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
            
            map.panTo(results[0].geometry.location);
            
            if(${showdirectionsonload == "Y" || not empty markertag})
            {
                marker = new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location,
                    animation: google.maps.Animation.DROP
                });
            }
        }
        else {
            toastr.error("Google had trouble finding the address for this posting, please check the desciption for more information. If it's not there please consider flagging this post.", "Google Couldn't Find The Address '" + address +"'" );
        }
        
        <c:if test="${not empty markertag}">
            infoBubble.open(map, marker);
        </c:if>
    });
   }
   
   function updateMarkerName(name) {
    infoBubble.setContent('<div class="badge badge-inverse">' + name + '</div>')
    infoBubble.close();
    infoBubble.open();
   }
   
   function mapLongLat(latitude, longitude, locationName) 
   {
	   
    if(showdirectionsonload == "Y")
    {   
        $("#locationname").replaceWith('<div id="locationname"><span>'+ locationName + '<strong></strong></span><br></div>');   
        $("#directions").replaceWith('<div id="directions"><a href="//maps.google.com/maps?saddr=${user.getZipCode()}&daddr=' + latitude + ',' + longitude + '" target="_blank">Get Directions</a></div>');
    }
   
    var latLng = new google.maps.LatLng(latitude,longitude, true);
    
    var mapOptions = 
    {
        zoom: zoomsetting,
        center: latLng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
   
        map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
        
        map.panTo(latLng);  
        searchRadius.setCenter(latLng);
        searchRadius.setMap(map);
         
        if(showdirectionsonload == "Y")
    {
      marker = new google.maps.Marker({
            map: map,
            position: latLng,
            animation: google.maps.Animation.DROP
        });
   
        searchRadius.setVisible(false);
    }
        else
        {
            setGoogleMapRadiusSearch(1);
            searchRadius.setVisible(true);  
        }
        
    <c:if test="${not empty markertag}">
        infoBubble.open(map, marker);
    </c:if>
    
       showdirectionsonload = "Y";
   }
   
   function setGoogleMapRadiusSearch(miles) {
        searchRadius.setRadius(1.621371*1000*miles);
   }
   
   var curlongitude;
   var curlatitude;
   
   $("body").on(
           "click",
           ".accordion-heading",
           function() {
   
               var coords = $(this).find(".listing-coordinates").text();
               var address = $(this).find(".listing-address").text();
               zoomsetting = 11;
               var parsedLabelName = coords.split(",");
               var latitude = parsedLabelName[0];
               var longitude = parsedLabelName[1];
   
               if (((curlongitude == longitude) && (curlatitude == latitude))
                       || (latitude == "") || (longitude == "")) {
                   return;
               }
   
               curlongitude = longitude;
               curlatitude = latitude;
   
               mapLongLat(latitude, longitude, address);              
  });
</script>
