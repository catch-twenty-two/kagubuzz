<table border="0" cellpadding="20" cellspacing="0" width="100%">
    <tr>
        <td valign="top">        
            <div mc:edit="std_content00">
                <h4 class="h4">Hey $recipient_name$,</h4>
                <br />
                <br />
                This email was sent because you requested to reset your password. To do so, follow the link below. If you didn't want to reset your password, please ignore this email.                                                             
             </div>
                                                                                                                 
           <div mc:edit="std_social">
                <br />
               <a style="color:#006dcc;" href="$server_url$/reset_password_step_1?id=$recipient_id$&securityCode=$recipient_security_code$">Reset My Password</a> 
            </div>
                                                                                                                     
            <div mc:edit="std_content00">
                <br />
                Thank you for using our service,
                <br />
                <br />
                The Team at Kagu Buzz
            </div>            
        </td>
    </tr>
</table>