<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>

<c:if test="${not empty currentWeather}">
<hr class="kagu-hr">
<small class="muted">Weather In ${user.getTblKaguLocation().getCity()}, ${user.getTblKaguLocation().getState()}</small>
<div>
	<div class="pull-left textsmall" style="text-align: center; width: 95px;">
		<img style="height:60px; width:60px;" alt="" src="/static/images/weather/${currentWeather.getDescriptionIconName()}" /><br> <strong>${currentWeather.getWeatherDescription()}</strong>
	</div>
	<div class="pull-left" style="text-align: center; width: 95px;">
        <strong>Currently</strong>
		
		<div style="line-height: 24px; text-align: center;">
		  <span class="textlargebold">${currentWeather.getCurrentTemp()}&deg;</span>
		</div>
        
		<div style="line-height: 12px; text-align: center;">
			<small>
			High ${todayforecast.getHighTemp()}&deg;<br>Low ${todayforecast.getLowTemp()}&deg;
			</small>
		</div>
	</div>
	<div class="pull-left" style="text-align: center; width: 95px;">
		<strong>Tomorrow</strong>
		<c:choose>
		<c:when test="not empty ${tomorrowforecast.getCoverageDescription()}">
		<div style="line-height: 12px; text-align: center;">
		   <strong><small>${tomorrowforecast.getCoverageDescription()}<br>${tomorrowforecast.getWeatherDescription()}</small></strong>
		   </div>
		</c:when>
		<c:otherwise>
		<div style="line-height: 24px; text-align: center;">
		  <strong><small>${tomorrowforecast.getWeatherDescription()}</small></strong>
		  </div>
		</c:otherwise>
		</c:choose>
            <div style="line-height: 12px; text-align: center;">
                <small>
				High ${tomorrowforecast.getHighTemp()}&deg;<br>Low ${tomorrowforecast.getLowTemp()}&deg;
				</small>
		   </div>
		 
	</div>
	<div class="clearfix"></div>
</div>
</c:if>
