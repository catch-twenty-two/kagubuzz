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
<meta charset="utf-8" />
<title>Kagu Buzz - Your Local Community Your Way</title>
</head>

<body>
	<div class="wrapper">
		<kagutags:header navactivebutton="" dont_ask_zip="Y" headerpic="smallheader.jpg"></kagutags:header>
		<div>
			<div class="buzzinfoboxpadding clear-background">

				<img class="pull-right" src="/static/images/cloudgroup.png" />
				<h2 class="featurette-heading"> Help</h2>
				<hr class="kagu-hr">
 
<p>
   <h2>What is Kagu Buzz?</h2>
</p>
<p>
   Kagu Buzz allows you to connect with your community. It lets you know what events are going on, what goods and services your neighbors are offering, plus
   what issues and questions your community is thinking about.
   <br/>
   <br/>
   At Kagu Buzz, our goal is to bring people together by establishing a sense of community, whether that community is one mile square or 30 miles wide.
</p>
<p>
   <h2>Joining Kagu Buzz</h2>
</p>
<p>
   Joining Kagu Buzz is free. Just create a user Id and enter your zip code to set a "Local Community".
</p>
<p>
   <h2>What can you do on Kagu Buzz?</h2>
</p>
<p>
   Kagu Buzz connects you with your community by allowing you to:
</p>
<p>
   Find and post local events
</p>
<ul>
   <li>
      <p>
         Sell or buy items
      </p>
   </li>
   <li>
      <p>
         Exchange information by starting or contributing to a discussion
      </p>
   </li>
   <li>
      <p>
         Ask or give advice
      </p>
   </li>
</ul>
<p>
   <h2>How to use Kagu Buzz</h2>
</p>
<p>
   Posting an event:
</p>
<ul>
   <li>
      <p>
         Click on Event IT
      </p>
   </li>
   <li>
      <p>
         Enter the event name, location and when and how often it happens
      </p>
   </li>
   <li>
      <p>
         Add the event details as well as any pictures and links
      </p>
   </li>
   <li>
      <p>
         Review your post, and if all looks good, post it
      </p>
   </li>
</ul>
<p>
   Selling an item:
</p>
<ul>
   <li>
      <p>
         Click on Ad It
      </p>
   </li>
   <li>
      <p>
         Enter the item for sale and the price you are asking for it
      </p>
   </li>
   <li>
      <p>
         Add details about the item as well as any pictures
      </p>
   </li>
   <li>
      <p>
         Review your post, and if all looks good, post it
      </p>
   </li>
</ul>
<p>
   Buying an item:
</p>
<ul>
   <li>
      <p>
         Clik on Find it
      </p>
   </li>
   <li>
      <p>
         Click on the item you are interested in and read the ad. If you're ready to buy, click on "Make an Offer"
      </p>
   </li>
   <li>
      <p>
         Select how you want to be contacted
      </p>
   </li>
   <li>
      <p>
         Enter your offer amount
      </p>
   </li>
</ul>
<p>
   Starting a discussion:
</p>
<ul>
   <li>
      <p>
         Click on Discuss It
      </p>
   </li>
   <li>
      <p>
         At the right, click on "Start a new Discussion"
      </p>
   </li>
   <li>
      <p>
         Add your discussion details
      </p>
   </li>
   <li>
      <p>
         Click on Post it
      </p>
   </li>
</ul>
<p>
   Participating in a discussion
</p>
<ul>
   <li>
      <p>
         Click on Discuss It
      </p>
   </li>
   <li>
      <p>
         Click on "More"
      </p>
   </li>
   <li>
      <p>
         Click on "Reply To"
      </p>
   </li>
   <li>
      <p>
         Add your reply
      </p>
   </li>
</ul>
<p>
   <h2>Account Settings</h2>
</p>
<p>
   Updating your Profile:
</p>
<ul>
   <li>
      <p>
         Click on your UserId at the top right
      </p>
   </li>
   <li>
      <p>
         Click "Account Settings"
      </p>
   </li>
   <li>
      <p>
         On the Profile tab, you can do the following
      </p>
      <ul>
         <li>
            <p>
               Add or update a profile picture
            </p>
         </li>
         <li>
            <p>
               Update your name
            </p>
         </li>
         <li>
            <p>
               Update your email
            </p>
         </li>
         <li>
            <p>
               Update your notifications
            </p>
         </li>
      </ul>
   </li>
</ul>
<p>
   Click "Save Changes"
</p>
<p>
   Updating preferred meeting spot:
</p>
<ul>
   <li>
      <p>
         Click on Buying and Selling
      </p>
   </li>
   <li>
      <p>
         Enter a preferred meeting spot and give it a name
      </p>
   </li>
   <li>
      <p>
         Add your cell number if you want to receive text messages
      </p>
   </li>
   <li>
      <p>
         Click the box next to "Receive Text Notifications&#8230;."
      </p>
   </li>
   <li>
      <p>
         Click on "Save Changes"
      </p>
   </li>
</ul>
<p>
   Changing your Password:
</p>
<ul>
   <li>
      <p>
         Enter your new password
      </p>
   </li>
   <li>
      <p>
         Re-enter it
      </p>
   </li>
   <li>
      <p>
         Click on "Save Changes"
      </p>
   </li>
</ul>
<p>
   List of who you are following:
</p>
<ul>
   <li>
      <p>
         Click on Following to see the users you have selected to follow
      </p>
   </li>
</ul>

		</div>
		  <kagutags:footer />
		</div>
</body>
</html>


