// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

public type CallerAction object {

    documentation {
        Sends outbound response to the caller.

        P{{res}} - The outbound response message.
    }
    public native function send(any res, Headers... headers) returns error?;

    documentation {
        Informs the caller, server finished sending messages.
    }
    public native function complete(Headers... headers) returns error?;

    documentation {
        Checks whether the connection is closed by the caller.
    }
    public native function isCancelled() returns boolean;

    documentation {
        Sends server error to the caller.

        P{{statusCode}} - Error status code.
        P{{message}} - Error message.
    }
    public native function sendError(int statusCode, string message, Headers... headers) returns error?;
};
