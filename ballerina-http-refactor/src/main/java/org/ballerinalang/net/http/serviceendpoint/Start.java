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

package org.ballerinalang.net.http.serviceendpoint;

import org.ballerinalang.bre.Context;
import org.ballerinalang.connector.api.BLangConnectorSPIUtil;
import org.ballerinalang.connector.api.Struct;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.wso2.transport.http.netty.contract.ServerConnector;

/**
 * Get the ID of the connection.
 *
 * @since 0.966
 */

@BallerinaFunction(
        packageName = "ballerina.net.http",
        functionName = "start",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "ServiceEndpoint",
                             structPackage = "ballerina.net.http"),
        isPublic = true
)
public class Start extends AbstractHttpNativeFunction {

    @Override
    public BValue[] execute(Context context) {
        Struct serviceEndpoint = BLangConnectorSPIUtil.getConnectorEndpointStruct(context);


    }
}
