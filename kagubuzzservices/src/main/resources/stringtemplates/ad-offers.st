email-offer-subject-seller() ::= << 
Offer of \$$offer$.00 made on your ad '$adtitle$' 
>>

email-offer-body-seller() ::= <<
<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>
			<div mc:edit='std_content00'>
				<h4 class='h4'>Hey $sellername$, we have some great news!</h4>
				<strong>$buyername$</strong> offered <strong>\$$offer$.00</strong> on your ad &quot;$adtitle$&quot;.
				<br />
				<br />
				To keep your information private, this email was sent by Kagu Buzz on behalf of <strong>$buyername$</strong>. You can automatically reply to the offer by selecting one of the options below:                                                             
			 </div>																									 
		   <div mc:edit='std_social'>
		   <br />                                                           		
				<a style='color:#da4f49;' href='$server_url$/transaction/accept?id=$discussionid$&amp;adcode=$UUID$'>I Accept!</a>
				&nbsp;&nbsp;&nbsp;<a style='color:#006dcc;' href='$server_url$/transaction/decline?id=$discussionid$&amp;adcode=$UUID$'>No Thanks.</a> 
				&nbsp;&nbsp;&nbsp;<a style='color:#5bb75b;' href='$server_url$/transaction/think_on_it?id=$discussionid$&amp;adcode=$UUID$'>Let Me Think On It</a>                                                                
				&nbsp;
			</div>																							 
			<div mc:edit='std_content00'>
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings.
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>
>>

receipt-offer-seller() ::= <<
Hi $sellername$,
<br>
<br>
$buyername$ has submitted $new_offer$ offer of \$$offer$.00 on your ad '$adtitle$'.
<br>
<br>
You can automatically reply to the offer by selecting one of the options below.                                                            
<br>
<br>
<div style='text-align: right'>
<a href='$server_url$/transaction/accept?id=$discussionid$&amp;adcode=$UUID$' class='btn btn-success'>I Accept</a>
<a href='$server_url$/transaction/decline?id=$discussionid$&amp;adcode=$UUID$' class='btn btn-error'>No Thanks</a>
<a href='$server_url$/transaction/think_on_it?id=$discussionid$&amp;adcode=$UUID$'class='btn btn-primary'>Let Me Think On It</a>
</div>
>>


email-offer-subject-seller-thinkonit() ::="You said you would think on $buyername$'s offer of \$$offer$.00."

email-offer-body-seller-thinkonit() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>	
		   <div mc:edit='std_content00'>
				<h4 class='h4'>Hey $sellername$,</h4>
				You offered to think on the offer of <strong>\$$offer$.00</strong> on the ad &quot;$adtitle$&quot; from $buyername$.
				<br />
				<br />
				$buyername$ is free to make another offer if the item is still available, but per Kagu Buzz etiquette, you should contact $buyername$ with a decision within 3 days.                                                       
			 </div>                                                                                                                                                                  																																						
			<div mc:edit='std_content00'>
				<br>  
				Don't leave 'em hangin'!
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings.
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"
                                                
email-offer-subject-seller-decline() ::="You declined $buyername$'s offer of \$$offer$.00 on ad '$adtitle$'."

email-offer-body-seller-decline() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>
		
		   <div mc:edit='std_content00'>
				<h4 class='h4'>Hey $sellername$,</h4>
			   You declined the offer of <strong>\$$offer$.00</strong> on the ad &quot;$adtitle$&quot; from $buyername$.
				<br />
				<br />
				Remember, $buyername$ is free to make another offer if the item is still available.                                                          
			 </div>                                                                                                                                                                  
																																												  
			<div mc:edit='std_content00'>
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings.
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"
 
email-offer-subject-seller-accept() ::="Offer of \$$offer$.00 from $buyername$ on ad '$adtitle$' accepted"

email-offer-body-seller-accept() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>
		   <div mc:edit='std_content00'>
				<h4 class='h4'>Hey $sellername$,</h4>
				You accepted the offer of <strong>\$$offer$.00</strong> on the ad &quot;$adtitle$&quot; from <strong>$buyername$</strong>.
				<br />
				<br />
				To begin exchanging the goods, you can reach <strong>$buyername$</strong>  via $buyercontactmethod$ at $buyercontactdetails$.                                                           
			 </div>                                                                                                                                                                  																																				  
			<div mc:edit='std_content00'>
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings.
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"

email-offer-subject-buyer() ::="You offered \$$offer$.00 on $sellername$'s ad '$adtitle$'

email-offer-body-buyer() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
<tr>
	<td valign='top'>	
		<div mc:edit='std_content00'>
			<h4 class='h4'>Hey $buyername$,</h4>
			Your offer of <strong>\$$offer$.00</strong> on the ad &quot;$adtitle$&quot; has been sent to $sellername$.
			<br />
			<br />														   
			$sellername$ now has the option of thinking about, declining, or accepting your offer and should be back in contact with you shortly.                                                                                                                                                                                                                                                
			<br/>
			<br/>
			Thank you for using our service, and good luck in your wheelings and dealings!
			<br />
			<br />
			- The Team at Kagu Buzz
		</div>
	</td>
</tr>
</table>"

email-offer-subject-buyer-thinkonit() ::="$sellername$ is thinking over your offer of \$$offer$.00."

email-offer-body-buyer-thinkonit() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>
		   <div mc:edit='std_content00'>
				<h4 class='h4'>Hey $buyername$,</h4>
				<strong>$sellername$</strong> offered to think on your offer of <strong>\$$offer$.00</strong> on the ad &quot;$adtitle$&quot;.
				<br />
				<br />
				Feel free to make another offer if the item is still available. Per Kagu Buzz etiquette, $sellername$ should contact you with a decision within 3 days.                                                          
			 </div>                                                                                                                                                                  																																									  
			<div mc:edit='std_content00'>
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings.
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"

email-offer-subject-buyer-decline() ::="Offer of \$$offer$.00 on $sellername$'s ad '$adtitle$' declined.

email-offer-body-buyer-decline() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>
		   <div mc:edit='std_content00'>
				<h4 class='h4'>Hey $buyername$,</h4>
				<strong>$sellername$</strong> declined your offer of <strong>\$$offer$.00</strong> on the ad &quot;$adtitle$&quot;.
				<br />
				<br />
				Feel free to make another offer if the item is still available.                                                          
			 </div>                                                                                                                                                                  																																							  
			<div mc:edit='std_content00'>
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings.
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"

email-offer-subject-buyer-accept() ::="Offer of \$$offer$.00 on $sellername$'s ad '$adtitle$' accepted!"

email-offer-body-buyer-accept() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>
		   <div mc:edit='std_content00'>
				<h4 class='h4'>Hey $buyername$, we have some great news!</h4>
				<strong>$sellername$</strong> accepted your offer of <strong>\$$offer$.00</strong> on the ad &quot;$adtitle$&quot;.
				<br />
				<br />
				You can reach <strong>$sellername$</strong> via the Kagu Buzz website to begin exchanging the goods!                                                           
			 </div>                                                                                                                                                                  
			<div mc:edit='std_content00'>
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings.
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"

email-offer-body-seller-already-accepted() ::=
"<table border="0" cellpadding="20" cellspacing="0" width="100%">
	<tr>
		<td valign="top">
			<div mc:edit="std_content00">
				<h4 class="h4">Hey $sellername$,</h4>
				<strong>$buyername$</strong> offered <strong>\$$offer$.00</strong> on your ad &quot;$adtitle$&quot;.
				<br />
				<br />
				To keep your information private, this email was sent by Kagu Buzz on behalf of <strong>$buyername$</strong>.  
				Since you have already accepted another offer, you can only accept $buyername$'s new offer by canceling the currently accepted offer. Please log in to do so.                                                             
			 </div>																									 
			<div mc:edit="std_content00">
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings.
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"

email-exchange-canceled-subject() ::= "The exchange for the ad $adtitle$ has been canceled."

email-exchange-canceled-body() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>
			<div mc:edit='std_content00'>
				<h4 class='h4'>Hello $recipient$,</h4>
				This is a notice to let you know that the exchange for the ad <strong>&quot;$adtitle$&quot;</strong>, with <strong>$other_party$</strong> has been canceled.
				<br />
				<br />
				If you feel this is a mistake, please contact $other_party$ with the link below. Otherwise, please take a few moments to rate your exchange so that others can benefit from your experience here:		
			 </div>				
			 <br />
				<a style='color:#006dcc;' href='$server_url$/$viewing_url$'>$adtitle$</a> 																					 
			<div mc:edit='std_content00'>
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings!
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"

email-exchange-finalized-subject() ::="The exchange for the ad $adtitle$ has been marked as closed."

email-exchange-finalized-body() ::=
"<table border='0' cellpadding='20' cellspacing='0' width='100%'>
	<tr>
		<td valign='top'>
			<div mc:edit='std_content00'>
				<h4 class='h4'>Hello $recipient$,</h4>
				This is a notice to let you know that the exchange for the ad &quot;$adtitle$&quot;, with $other_party$ has been closed.
				<br />
				<br />
				By now you should have made an exchange of goods or services with $other_party$. Please take a few moments to rate your exchange with $other_party$ here so that others can benefit from your experience:		
			 </div>				
			 <br />
				<a style='color:#006dcc;' href='$server_url$/$viewing_url$'>$adtitle$</a> 																					 
			<div mc:edit='std_content00'>
				<br />
				Thank you for using our service, and good luck in your wheelings and dealings!
				<br />
				<br />
				- The Team at Kagu Buzz
			</div>
		</td>
	</tr>
</table>"

receipt-offer-buyer-accept() ::="
Hey $buyername$, great news! Your offer of \$$offer$.00 on the ad '$adtitle$' was accepted by $sellername$. This message was sent by Kagu Buzz on behalf of $sellername$. Hit the reply button below to start a private conversation about how to exchange the goods."

receipt-offer-buyer-decline() ::="
Your offer of \$$offer$.00 on the ad '$adtitle$' was declined by $sellername$. Feel free to make another offer if the item is still available."


receipt-offer-buyer-thinkonit() ::="
$sellername$ decided to think on the offer of \$$offer$.00 for the ad '$adtitle$'. Per Kagu Buzz etiquette, $sellername$ should make a decision on your offer within 3 days."

receipt-offer-buyer() ::="
This is to let you know that your offer of \$$offer$.00 on the ad '$adtitle$' has been sent to $sellername$. $sellername$ now has the option of thinking about, declining, or accepting your offer, and should be back in contact with you shortly."

receipt-offer-seller-accept() ::="
You accepted $buyername$'s offer of \$$offer$.00 on your ad '$adtitle$'. This message was sent by Kagu Buzz on behalf of $buyername$. $buyername$ can be reached via $buyercontactmethod$ at $buyercontactdetails$, or hit the reply button below to start a private conversation about how to exchange the goods."

receipt-offer-seller-already-accepted() ::="
Hi $sellername$,
<br>
<br>
$buyername$ has submitted $new_offer$ offer of \$$offer$.00 on your ad '$adtitle$'.
<br>
<br>
You can automatically reply to this user by selecting one of the options below.
<br>
<br>
<strong>WARNING: In order to accept this new offer, you need to cancel your currently accepted offer. Doing so can lead to bad feedback, especially if you don't work out the cancellation with your accepted buyer first.</strong>
<div style='text-align: right'>
<a href='$server_url$/transaction/accept?id=$discussionid$&amp;adcode=$UUID$' class='btn btn-success'>I Accept</a>
<a href='$server_url$/transaction/decline?id=$discussionid$&amp;adcode=$UUID$' class='btn btn-error'>No Thanks</a>
<a href='$server_url$/transaction/transaction/transaction_think_on_it?id=$discussionid$&amp;adcode=$UUID$'class='btn btn-primary'>Let Me Think On It</a>
</div>
<br>
<br>"

receipt-offer-seller-decline() ::="
You declined $buyername$'s offer of \$$offer$.00 on your ad '$adtitle$'. Remember, you can always contact $buyername$ later through this message history if you decide you want to accept this offer in the future."

receipt-offer-seller-thinkonit() ::="
You offered to think on $buyername$'s offer of \$$offer$.00 on your ad '$adtitle$'. Don't leave 'em hangin'!
<br>
<br>
Use the reply link to start a conversation, or you can automatically decline or accept the offer using the buttons below.
<br>
<br>
<div style='text-align: right'>
<a href='$server_url$/transaction/accept?id=$discussionid$&adcode=$UUID$' class='btn btn-success'>I Accept</a>
<a href='$server_url$/transaction/decline?id=$discussionid$&adcode=$UUID$' class='btn btn-error'>No Thanks</a>
</div>"

receipt-offer-seller() ::="
Hi $sellername$,
<br>
<br>
$buyername$ has submitted $new_offer$ offer of \$$offer$.00 on your ad '$adtitle$'.
<br>
<br>
You can automatically reply to the offer by selecting one of the options below.                                                            
<br>
<br>
<div style='text-align: right'>
<a href='$server_url$/transaction/accept?id=$discussionid$&amp;adcode=$UUID$' class='btn btn-success'>I Accept</a>
<a href='$server_url$/transaction/decline?id=$discussionid$&amp;adcode=$UUID$' class='btn btn-error'>No Thanks</a>
<a href='$server_url$/transaction/think_on_it?id=$discussionid$&amp;adcode=$UUID$'class='btn btn-primary'>Let Me Think On It</a>
</div>"

receipt-exchange-canceled() ::="This is a notice to let you know that the exchange for the ad <strong>&quot;$adtitle$&quot;</strong> with $other_party$ has been canceled. If you feel this should not be the case, please contact $other_party$ by clicking on the reply button below. Otherwise, please take a few moments to rate your exchange so that others can benefit from your experience. Clicking on the 'Rate This Exchange With $other_party$' button at the top of this page to do so."

receipt-exchange-finalized() ::="Hello $recipient$. This is a notice to let you know that the exchange for the ad &quot;$adtitle$&quot;, has been marked as complete. By now you should have made an exchange of goods or services with $other_party$. Please take a few moments to rate your exchange with $other_party$ by clicking on the 'Rate This Exchange With $other_party$' button at the top of this page."

