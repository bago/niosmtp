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
package me.normanmaurer.niosmtp.client;

import java.net.ConnectException;
import java.util.Iterator;

import me.normanmaurer.niosmtp.SMTPConnectionException;
import me.normanmaurer.niosmtp.SMTPException;

/**
 * Simple {@link DeliveryResult} implementation 
 * 
 * @author Norman Maurer
 *
 */
public class DeliveryResultImpl implements DeliveryResult{

    private final Iterable<DeliveryRecipientStatus> status;
    private final SMTPException exception;

    public DeliveryResultImpl(Iterable<DeliveryRecipientStatus> status) {
        this.status = status;
        this.exception = null;
    }
    
    public DeliveryResultImpl(SMTPException exception) {
        this.exception = exception;
        this.status = null;
    }
    
    @Override
    public boolean isSuccess() {
        return exception == null;
    }

    @Override
    public SMTPException getException() {
        return exception;
    }

    @Override
    public Iterator<DeliveryRecipientStatus> getRecipientStatus() {
        if (status == null) {
            return null;
        }
        return status.iterator();
    }
    
    /**
     * Create a new {@link DeliveryResultImpl} by taking care to wrap or cast the given {@link Throwable} to the right {@link SMTPException}
     * 
     * @param t
     * @return result
     */
    public static DeliveryResultImpl create(Throwable t) {
        final SMTPException exception;
        if (t instanceof SMTPException) {
            exception = (SMTPException) t;
        } else if (t instanceof ConnectException) {
            exception = new SMTPConnectionException(t);
        } else {
            exception = new SMTPException("Exception while try to deliver msg", t);
        }
        return new DeliveryResultImpl(exception);
        
    }

}
