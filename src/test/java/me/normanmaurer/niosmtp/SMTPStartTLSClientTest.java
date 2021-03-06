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
package me.normanmaurer.niosmtp;



import org.apache.james.protocols.api.handler.WiringException;
import org.apache.james.protocols.impl.NettyServer;
import org.apache.james.protocols.smtp.SMTPConfigurationImpl;
import org.apache.james.protocols.smtp.SMTPProtocol;
import org.apache.james.protocols.smtp.SMTPProtocolHandlerChain;
import org.apache.james.protocols.smtp.hook.Hook;

import me.normanmaurer.niosmtp.transport.impl.NettySMTPClientTransport;


public class SMTPStartTLSClientTest extends SMTPClientTest{


    @Override
    protected NettyServer create(Hook hook) throws WiringException {
        SMTPConfigurationImpl config = new SMTPConfigurationImpl() {

            @Override
            public boolean isStartTLSSupported() {
                return true;
            }
            
        };
        
        SMTPProtocolHandlerChain chain = new SMTPProtocolHandlerChain();
        chain.addHook(hook);
        return new NettyServer(new SMTPProtocol(chain, config),BogusSslContextFactory.getServerContext());
    }

    @Override
    protected NettySMTPClientTransport createSMTPClient() {
        return NettySMTPClientTransport.createStartTLS(BogusSslContextFactory.getClientContext(), true);
    }
    
}
