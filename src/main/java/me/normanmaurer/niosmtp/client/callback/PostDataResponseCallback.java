/**
* Licensed to niosmtp developers ('niosmtp') under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* niosmtp licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package me.normanmaurer.niosmtp.client.callback;

import java.util.Iterator;
import java.util.List;

import me.normanmaurer.niosmtp.MessageInput;
import me.normanmaurer.niosmtp.SMTPResponse;
import me.normanmaurer.niosmtp.SMTPResponseCallback;
import me.normanmaurer.niosmtp.client.DeliveryRecipientStatus;
import me.normanmaurer.niosmtp.client.DeliveryRecipientStatusImpl;
import me.normanmaurer.niosmtp.client.DeliveryResultImpl;
import me.normanmaurer.niosmtp.client.DeliveryRecipientStatus.Status;
import me.normanmaurer.niosmtp.client.SMTPClientFutureImpl;
import me.normanmaurer.niosmtp.core.SMTPRequestImpl;
import me.normanmaurer.niosmtp.transport.SMTPClientSession;


/**
 * {@link AbstractResponseCallback} implementation which will handle the <code>POST DATA</code> {@link SMTPResponse} which will
 * get send after the {@link MessageInput} get submitted via the CRLF.CRLF sequence
 * 
 * 
 * @author Norman Maurer
 *
 */
public class PostDataResponseCallback extends AbstractResponseCallback {

    
    /**
     * Get instance of this {@link SMTPResponseCallback} implemenation
     */
    public final static SMTPResponseCallback INSTANCE = new PostDataResponseCallback();
    
    
    private PostDataResponseCallback() {
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(SMTPClientSession session, SMTPResponse response) {

        SMTPClientFutureImpl future = (SMTPClientFutureImpl) session.getAttributes().get(FUTURE_KEY);
        List<DeliveryRecipientStatus> statusList = (List<DeliveryRecipientStatus>) session.getAttributes().get(DELIVERY_STATUS_KEY);
        
        int code = response.getCode();

        if (code < 400) {
            
            // Set the final status for successful recipients
            Iterator<DeliveryRecipientStatus> status = statusList.iterator();
            while(status.hasNext()) {
                DeliveryRecipientStatus s = status.next();
                if (s.getStatus() == Status.Ok) {
                    ((DeliveryRecipientStatusImpl)s).setResponse(response);
                }
            }
            future.setDeliveryStatus(new DeliveryResultImpl(statusList));

        } else {
            Iterator<DeliveryRecipientStatus> status = statusList.iterator();
            while(status.hasNext()) {
                ((DeliveryRecipientStatusImpl)status.next()).setResponse(response);
            }
            future.setDeliveryStatus(new DeliveryResultImpl(statusList));

        }    
        session.send(SMTPRequestImpl.quit(), SMTPResponseCallback.EMPTY);
        session.close();
    }

    
}
