<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="kagutags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">

<!-- Jquery has to go before bootstrap -->

<kagutags:jquery />

   <kagutags:bootstrap_css/>
   <kagutags:bootstrap_js/>

<link rel="stylesheet" href="/static/css/main.css" type="text/css" />
<link rel="stylesheet" href="/static/css/featurette.css" type="text/css" />

<head>
<meta name="robots" content="noindex" charset="utf-8">
<title>Kagu Buzz - Your Local Community Your Way</title>
</head>

<body>
	<div class="wrapper">
		<kagutags:header navactivebutton="" dont_ask_zip="Y" headerpic="smallheader.jpg"></kagutags:header>
		<div >
			<div class="buzzinfoboxpadding">
			     
            <img class="pull-right" src="/static/images/cloudgroup.png" />
				<h2 class="featurette-heading">About Us</h2>
				<hr class="kagu-hr">
				<div class="media">
					<a class="pull-left" href="javascript:void(null)"> <img class="avatar-img-size-100x100 media-object"
						src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${jimmy_id}">
					</a>
					<div class="media-body">
						<h4 class="media-heading">Jimmy Johnson (Co-Founder, Software Development)</h4>
						Jimmy has been in software development for more than 13 years.  In 2012, frustrated with sites like Yelp, Craigslist and E-bay, he
						decided to embark on a journey to create a site that brought the best of all these popular sites together. When he's not working on
						Kagu Buzz or at his day job, Jimmy spends his spare time enjoying the many goings ons in Berkeley, California, where he currently
						resides. 
					</div>
				</div>
				<hr class="kagu-hr">
				<div class="media">
					<a class="pull-left" href="javascript:void(null)"> <img class="avatar-img-size-100x100 media-object"
						src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${marvin_id}">
					</a>

					<div class="media-body">
						<h4 class="media-heading">Marvin Bauzon (Co-Founder, Business Development)</h4>
						Marvin has worked in small, mid-sized and large national/international companies for over 20 years. He's not as frustrated as Jimmy,
						but always wanted to be part of something that was locally focused and dedicated to giving something back to the community. When he's
						not at his day job, he works on Kagu Buzz and spends his spare time with his wife in Oakland, California, where they reside.
					</div>
				</div>
				<hr class="kagu-hr">
                <div class="media">
                    <a class="pull-left" href="javascript:void(null)"> <img class="avatar-img-size-100x100 media-object"
                        src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${thone_id}">
                    </a>
                    <div class="media-body">
                        <h4 class="media-heading">Thone Soungpanya (Co-Founder, Software Development)</h4>
                        Thone has been in the Information Technology industry for over 12 years, working in different positions from IT Support, System Administrator, 
                        to Software Engineer. He joined the team at Kagu Buzz as he also wanted a site that listed local events, a better user experience for 
                        buying and selling transactions, and a site that brings the local community together. When he&rsquo;s not working on Kagu Buzz or at 
                        his day job, Thone spends his spare time at the gym, enjoying sporting events, or exploring the vastness that the Bay Area communities offer.
                    </div>
                </div>
				<hr class="kagu-hr">
				<div class="media">
					<a class="pull-left" href="javascript:void(null)"> <img class="avatar-img-size-100x100 media-object"
						src="/fileServerServlet?type=GetAvatarByUserId&amp;id=${rachel_id}">
					</a>
					<div class="media-body">
						<h4 class="media-heading">Rachel Seligman (Writer and Editor)</h4>
						Rachel has been involved with a variety of ventures over the years, including some time spent as a writer and editor for magazines and
						websites. She came to Kagu Buzz as someone also frustrated, but her annoyance was with the lack of good-quality local event listings.
						When she's not working on Kagu Buzz, she can usually be found at her day job, at the gym, or exploring her local community in the East
						Bay.
					</div>
				</div>

				<hr class="kagu-hr">
				<div class="media">
					<a class="pull-left" href="javascript:void(null)"> <img class="avatar-img-size-100x100 media-object"
						src="http://cdn1.arkive.org/media/32/32E6E5EB-A688-485A-9D94-CA582D3303D4/Presentation.Large/Kagu-displaying.jpg">
					</a>
					<div class="media-body">
						<h4 class="media-heading">Kagu Bird (endangered)</h4>
						<h5>"So, What's a Kagu?"</h5>  Glad you asked.  It's a rare, flightless bird found only in New Caledonia, an Island in the South Pacific.  The
						locals refer to them as the 'ghost of the forest'.
						<h5>Why did we choose the name?</h5>Well, it's hard to find catchy a name that isn't
						already taken these days, but after coming across the bird, we thought it sounded cool, and looked cool as well.  We still think so. Unfortuntaly 
						this really cool bird is endangered.  
						<div class="clearfix"></div>
						<br>
						<p>
							<a class="pull-right" href="http://www.arkive.org/kagu/rhynochetos-jubatus/">More Info On the Kagu Bird and how you can help</a>
						</p>
						<br>
						<br>
					</div>
				</div>
			</div>
		</div>
		<kagutags:footer/>
	</div>
</body>
</html>


