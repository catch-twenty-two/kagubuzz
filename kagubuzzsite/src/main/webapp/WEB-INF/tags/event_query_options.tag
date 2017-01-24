<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags" %>
		<div class="accordion-heading" style="text-align: center;">
		<a class="accordion-toggle" data-toggle="collapse" href="#collapseSearchOptions" style="margin: 2px 0px;">Click Here To Add Optional Stuff (Pictures, A Repeating Schedule, Keywords, Ect..)</a>
		</div>			
		<div id="collapseSearchOptions" class="accordion-body collapse">
			<div class="accordion-inner" style=" padding: 5px 0px 0px 2px;">
						<div id="thumbnailholder">
				<ul class="thumbnails global-margin-top" style="width:500px;">
				<c:choose>
					<c:when test="${empty event.getSideBarImageName()}">
			            <kagutags:image_uploader
			            captiontitle="Add a Sidebar Image"             
			            height="140" 
			            width="180" 
			            imagename="eventsidebarimage" 
			            defaultimageurl="static/images/blankimagesidebar.jpg"/>
                    </c:when>	
                    <c:otherwise>	            
                        <kagutags:image_uploader
                        captiontitle="Add a Sidebar Image"             
                        height="140" 
                        width="180" 
                        imagename="eventsidebarimage" 
                        defaultimageurl="${sideBarImageURL}"/>
                    </c:otherwise>
                </c:choose>
                <c:choose>
			        <c:when test="${empty event.getBackgroundImageName()}">
			            <kagutags:image_uploader            
                        captiontitle="Add a Background Image" 
                        height="140" 
                        width="180" 
                        imagename="eventbackgroundimage" 
                        defaultimageurl="static/images/blankbackgroundupload.jpg"/>
			         </c:when>
		             <c:otherwise>
			            <kagutags:image_uploader            
			           	captiontitle="Add a Background Image" 
			           	height="140" 
			            width="180" 
			            imagename="eventbackgroundimage" 
			            defaultimageurl="${backgroundImageURL}"/>
			        </c:otherwise>
		        </c:choose>   
	            </ul>
            </div>
            <div style="margin-top: 30px;">
			<div class="pull-left" >	
			<div class="control-group">
    						<label class="control-label" for="inputEmail">It Repeats</label>
    						<div class="controls">
      							<select name="repeats" id="repeats">									
									<c:forEach var="eventperiod" items="${eventperiods}">
										<option value="${eventperiod.name()}">${eventperiod.getEnumExtendedValues().getDescription()}</option>
									</c:forEach>		
								</select>
    						</div>
  						</div>
				<!-- <div class="control-group">
    				<label class="control-label">Keywords </label>
    				<div class="controls">
						<input type="text" class="span3" name="kw1" placeholder="Example: Circus" /><br><br>				
						<input type="text" class="span3" name="kw2" placeholder="Example: Clowns" /><br><br>	
						<input type="text" class="span3" name="kw1" placeholder="Example: Cotton Candy" /><br><br>				
						<input type="text" class="span3" name="kw2" placeholder="Example: Ringmaster" />						
					</div> 
			 	</div>-->
			</div>
			<div class="pull-left">
  				<div class="control-group">
    				<label class="control-label" for="inputprice">Venue Location</label>
    				<div class="controls">
    				<c:forEach var="eventvenue" items="${eventvenues}">
                    <label class="radio">
                      <input type="radio" name="venue" value="${eventvenue.name()}">
                        ${eventvenue.getEnumExtendedValues().getDescription()}
                  </label>
                </c:forEach>                					
    				</div>
  				</div>
				<div class="control-group">
    				<label class="control-label" for="inputprice">Age Appropriate For </label>
    				<div class="controls">	
						 <c:forEach var="age" items="${ages}">
                    <label class="radio"> <input type="radio" name="age" value="${age.name()}">
                        ${age.getEnumExtendedValues().getDescription()}
                  </label>
                </c:forEach> 
    				</div>
  				</div>
  			</div> 			
			<div class="clearfix"></div>
			</div>
		</div>
		</div>