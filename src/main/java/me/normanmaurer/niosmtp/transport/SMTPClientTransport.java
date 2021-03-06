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
package me.normanmaurer.niosmtp.transport;

import java.net.InetSocketAddress;

import me.normanmaurer.niosmtp.SMTPClientConfig;
import me.normanmaurer.niosmtp.SMTPResponse;
import me.normanmaurer.niosmtp.SMTPResponseCallback;


/**
 * An SMTPClient Transport which should get used to connect to a remote SMTPServer.
 * 
 * @author Norman Maurer
 *
 */
public interface SMTPClientTransport {

    

    
    /**
     * Return the {@link DeliveryMode} which the {@link SMTPClientTransport} use
     * 
     * @return mode
     */
    public DeliveryMode getDeliveryMode();
    
    /**
     * Connect to the given {@link InetSocketAddress} and executure the {@link SMTPResponseCallback} once the Welcome {@link SMTPResponse} 
     * was received or an {@link Exception} was thrown
     * 
     * @param remote
     * @param config
     * @param callback
     */
    public void connect(InetSocketAddress remote, SMTPClientConfig config, SMTPResponseCallback callback);
    
    
}
