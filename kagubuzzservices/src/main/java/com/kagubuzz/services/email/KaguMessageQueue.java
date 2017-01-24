package com.kagubuzz.services.email;

import com.kagubuzz.message.types.Message;

public class KaguMessageQueue {
    
    Message message;

    public KaguMessageQueue(Message message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        return message.getReceipt().getSubject().hashCode() + message.getReceipt().getRecipient().getEmail().hashCode();    
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KaguMessageQueue other = (KaguMessageQueue) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        }
        else if (!message.equals(other.message))
            return false;
        return true;
    } 
}
