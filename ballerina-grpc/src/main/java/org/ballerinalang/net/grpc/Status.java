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

import org.ballerinalang.net.grpc.exception.StatusException;
import org.ballerinalang.net.grpc.exception.StatusRuntimeException;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import javax.annotation.Nullable;

/**
 * Defines the status of an operation by providing a standard {@link Code} in conjunction with an
 * optional descriptive message. Instances of {@code Status} are created by starting with the
 * template for the appropriate {@link Status.Code} and supplementing it with additional
 * information: {@code Status.NOT_FOUND.withDescription("Could not find 'important_file.txt'");}
 * <p>
 * <p>For clients, every remote call will return a status on completion. In the case of errors this
 * status may be propagated to blocking stubs as a {@link RuntimeException} or to a listener as an
 * explicit parameter.
 * <p>
 * <p>Similarly servers can report a status by throwing {@link StatusRuntimeException}
 * or by passing the status to a callback.
 * <p>
 * <p>Utility functions are provided to convert a status to an exception and to extract them
 * back out.
 */
public final class Status implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * The set of canonical status codes. If new codes are added over time they must choose
     * a numerical value that does not collide with any previously used value.
     */
    public enum Code {
        /**
         * The operation completed successfully.
         */
        OK(0),

        /**
         * The operation was cancelled (typically by the caller).
         */
        CANCELLED(1),

        /**
         * Unknown error.  An example of where this error may be returned is
         * if a Status value received from another address space belongs to
         * an error-space that is not known in this address space.  Also
         * errors raised by APIs that do not return enough error information
         * may be converted to this error.
         */
        UNKNOWN(2),

        /**
         * Client specified an invalid argument.  Note that this differs
         * from FAILED_PRECONDITION.  INVALID_ARGUMENT indicates arguments
         * that are problematic regardless of the state of the system
         * (e.g., a malformed file name).
         */
        INVALID_ARGUMENT(3),

        /**
         * Deadline expired before operation could complete.  For operations
         * that change the state of the system, this error may be returned
         * even if the operation has completed successfully.  For example, a
         * successful response from a server could have been delayed long
         * enough for the deadline to expire.
         */
        DEADLINE_EXCEEDED(4),

        /**
         * Some requested entity (e.g., file or directory) was not found.
         */
        NOT_FOUND(5),

        /**
         * Some entity that we attempted to create (e.g., file or directory) already exists.
         */
        ALREADY_EXISTS(6),

        /**
         * The caller does not have permission to execute the specified
         * operation.  PERMISSION_DENIED must not be used for rejections
         * caused by exhausting some resource (use RESOURCE_EXHAUSTED
         * instead for those errors).  PERMISSION_DENIED must not be
         * used if the caller cannot be identified (use UNAUTHENTICATED
         * instead for those errors).
         */
        PERMISSION_DENIED(7),

        /**
         * Some resource has been exhausted, perhaps a per-user quota, or
         * perhaps the entire file system is out of space.
         */
        RESOURCE_EXHAUSTED(8),

        /**
         * Operation was rejected because the system is not in a state
         * required for the operation's execution.  For example, directory
         * to be deleted may be non-empty, an rmdir operation is applied to
         * a non-directory, etc.
         * <p>
         * <p>A litmus test that may help a service implementor in deciding
         * between FAILED_PRECONDITION, ABORTED, and UNAVAILABLE:
         * (a) Use UNAVAILABLE if the client can retry just the failing call.
         * (b) Use ABORTED if the client should retry at a higher-level
         * (e.g., restarting a read-modify-write sequence).
         * (c) Use FAILED_PRECONDITION if the client should not retry until
         * the system state has been explicitly fixed.  E.g., if an "rmdir"
         * fails because the directory is non-empty, FAILED_PRECONDITION
         * should be returned since the client should not retry unless
         * they have first fixed up the directory by deleting files from it.
         */
        FAILED_PRECONDITION(9),

        /**
         * The operation was aborted, typically due to a concurrency issue
         * like sequencer check failures, transaction aborts, etc.
         * <p>
         * <p>See litmus test above for deciding between FAILED_PRECONDITION,
         * ABORTED, and UNAVAILABLE.
         */
        ABORTED(10),

        /**
         * Operation was attempted past the valid range.  E.g., seeking or
         * reading past end of file.
         * <p>
         * <p>Unlike INVALID_ARGUMENT, this error indicates a problem that may
         * be fixed if the system state changes. For example, a 32-bit file
         * system will generate INVALID_ARGUMENT if asked to read at an
         * offset that is not in the range [0,2^32-1], but it will generate
         * OUT_OF_RANGE if asked to read from an offset past the current
         * file size.
         * <p>
         * <p>There is a fair bit of overlap between FAILED_PRECONDITION and OUT_OF_RANGE.
         * We recommend using OUT_OF_RANGE (the more specific error) when it applies
         * so that callers who are iterating through
         * a space can easily look for an OUT_OF_RANGE error to detect when they are done.
         */
        OUT_OF_RANGE(11),

        /**
         * Operation is not implemented or not supported/enabled in this service.
         */
        UNIMPLEMENTED(12),

        /**
         * Internal errors.  Means some invariants expected by underlying
         * system has been broken.  If you see one of these errors,
         * something is very broken.
         */
        INTERNAL(13),

        /**
         * The service is currently unavailable.  This is a most likely a
         * transient condition and may be corrected by retrying with
         * a backoff.
         * <p>
         * <p>See litmus test above for deciding between FAILED_PRECONDITION,
         * ABORTED, and UNAVAILABLE.
         */
        UNAVAILABLE(14),

        /**
         * Unrecoverable data loss or corruption.
         */
        DATA_LOSS(15),

        /**
         * The request does not have valid authentication credentials for the
         * operation.
         */
        UNAUTHENTICATED(16);

        private final int value;

        Code(int value) {

            this.value = value;
        }

        /**
         * The numerical value of the code.
         */
        public int value() {

            return value;
        }

        /**
         * Returns a {@link Status} object corresponding to this status code.
         */
        public Status toStatus() {

            return STATUS_LIST.get(value);
        }
    }

    // Create the canonical list of Status instances indexed by their code values.
    private static final List<Status> STATUS_LIST = buildStatusList();

    private static List<Status> buildStatusList() {

        TreeMap<Integer, Status> canonicalizer = new TreeMap<Integer, Status>();
        for (Code code : Code.values()) {
            Status replaced = canonicalizer.put(code.value(), new Status(code));
            if (replaced != null) {
                throw new IllegalStateException("Code value duplication between "
                        + replaced.getCode().name() + " & " + code.name());
            }
        }
        return Collections.unmodifiableList(new ArrayList<Status>(canonicalizer.values()));
    }

    /**
     * Return a {@link Status} given a canonical error {@link Code} value.
     */
    public static Status fromCodeValue(int codeValue) {

        if (codeValue < 0 || codeValue > STATUS_LIST.size()) {
            return Status.Code.UNKNOWN.toStatus().withDescription("Unknown code " + codeValue);
        } else {
            return STATUS_LIST.get(codeValue);
        }
    }

    /**
     * Return a {@link Status} given a canonical error {@link Code} object.
     */
    public static Status fromCode(Code code) {

        return code.toStatus();
    }

    /**
     * Extract an error {@link Status} from the causal chain of a {@link Throwable}.
     * If no status can be found, a status is created with {@link Code#UNKNOWN} as its code and
     * {@code t} as its cause.
     *
     * @return non-{@code null} status
     */
    public static Status fromThrowable(Throwable t) {

        Throwable cause = t;
        while (cause != null) {
            if (cause instanceof StatusException) {
                return ((StatusException) cause).getStatus();
            } else if (cause instanceof StatusRuntimeException) {
                return ((StatusRuntimeException) cause).getStatus();
            }
            cause = cause.getCause();
        }
        // Couldn't find a cause with a Status
        return Status.Code.UNKNOWN.toStatus().withCause(t);
    }

    public static String formatThrowableMessage(Status status) {

        if (status.description == null) {
            return status.code.toString();
        } else {
            return status.code + ": " + status.description;
        }
    }

    private final Code code;
    private final String description;
    private final Throwable cause;

    private Status(Code code) {

        this(code, null, null);
    }

    private Status(Code code, String description, Throwable cause) {

        this.code = code;
        this.description = description;
        this.cause = cause;
    }

    /**
     * Create a derived instance of {@link Status} with the given cause.
     * However, the cause is not transmitted from server to client.
     */
    public Status withCause(Throwable cause) {

        if (java.util.Objects.equals(this.cause, cause)) {
            return this;
        }
        return new Status(this.code, this.description, cause);
    }

    /**
     * Create a derived instance of {@link Status} with the given description.  Leading and trailing
     * whitespace may be removed; this may change in the future.
     */
    public Status withDescription(String description) {

        if (java.util.Objects.equals(this.description, description)) {
            return this;
        }
        return new Status(this.code, description, this.cause);
    }

    /**
     * Create a derived instance of {@link Status} augmenting the current description with
     * additional detail.  Leading and trailing whitespace may be removed; this may change in the
     * future.
     */
    public Status augmentDescription(String additionalDetail) {

        if (additionalDetail == null) {
            return this;
        } else if (this.description == null) {
            return new Status(this.code, additionalDetail, this.cause);
        } else {
            return new Status(this.code, this.description + "\n" + additionalDetail, this.cause);
        }
    }

    /**
     * The canonical status code.
     */
    public Code getCode() {

        return code;
    }

    /**
     * A description of this status for human consumption.
     */
    @Nullable
    public String getDescription() {

        return description;
    }

    /**
     * The underlying cause of an error.
     * Note that the cause is not transmitted from server to client.
     */
    @Nullable
    public Throwable getCause() {

        return cause;
    }

    /**
     * Is this status OK, i.e., not an error.
     */
    public boolean isOk() {

        return Code.OK == code;
    }

    /**
     * Convert this {@link Status} to a {@link RuntimeException}. Use {@link #fromThrowable}
     * to recover this {@link Status} instance when the returned exception is in the causal chain.
     */
    public StatusRuntimeException asRuntimeException() {

        return new StatusRuntimeException(this);
    }

    /**
     * Convert this {@link Status} to an {@link Exception}. Use {@link #fromThrowable}
     * to recover this {@link Status} instance when the returned exception is in the causal chain.
     */
    public StatusException asException() {

        return new StatusException(this);
    }

    /**
     * A string representation of the status useful for debugging.
     */
    @Override
    public String toString() {

        return ("Status{ code " + code.name() + ", ") +
                "description " + description + ", " +
                "cause " + (cause != null ? getStackTraceAsString(cause) : null) + "}";
    }

    private static String getStackTraceAsString(Throwable throwable) {

        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
