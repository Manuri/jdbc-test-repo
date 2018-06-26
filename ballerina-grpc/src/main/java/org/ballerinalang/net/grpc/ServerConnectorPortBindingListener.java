/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.net.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.transport.http.netty.contract.PortBindingEventListener;

import java.io.PrintStream;

/**
 * Server Connector Port Binding Listener.
 */
public class ServerConnectorPortBindingListener implements PortBindingEventListener {

    private static final Logger log = LoggerFactory.getLogger(ServerConnectorPortBindingListener.class);
    private static final PrintStream console;

    public void onOpen(String serverConnectorId, boolean isHttps) {
        if (isHttps) {
            console.println("ballerina: started HTTPS/WSS endpoint " + serverConnectorId);
        } else {
            console.println("ballerina: started HTTP/WS endpoint " + serverConnectorId);
        }

    }

    public void onClose(String serverConnectorId, boolean isHttps) {
        if (isHttps) {
            console.println("ballerina: stopped HTTPS/WSS endpoint " + serverConnectorId);
        } else {
            console.println("ballerina: stopped HTTP/WS endpoint " + serverConnectorId);
        }

    }

    public void onError(Throwable throwable) {
        log.error("Error in http endpoint", throwable);
    }

    static {
        console = System.out;
    }
}
