package com.example.api.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

//@JsonInclude(JsonInclude.Include.NON_NULL) is used to ignore null fields in an object.
//@JsonIgnoreProperties(ignoreUnknown = true) to ignore any unknown field. Which means if there is a new field is added tomorrow on JSON which represent your Model then Jackson will not throw UnrecognizedPropertyException while parsing JSON in Java.
//@JsonPropertyOrder is used to specify the ordering of the serialized properties.
//We need to add the new properties score and action, cause our response contain them
//The score is based on the user's interactions and is a value between 0 (very likely a bot) and 1.0 (very likely a human).
//Action is a new concept that Google introduced so that we can execute many reCAPTCHA requests on the same web page.
//An action must be specified every time we execute the reCAPTCHA v3. And, we have to verify that the value of the action property in the response corresponds to the expected name.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "success",
        "challenge_ts",
        "hostname",
        "error-codes",
        "score",
        "action"
})
@Data
public class GoogleResponseV3 {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    //The hostname refers to the server that redirected the user to the reCAPTCHA.
    //If you manage many domains and wish them all to share the same key-pair, you can choose to verify the hostname property yourself.
    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("error-codes")
    private ErrorCode[] errorCodes;

    @JsonProperty("score")
    private float score;

    @JsonProperty("action")
    private String action;

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    @JsonProperty("error-codes")
    public ErrorCode[] getErrorCodes() {
        return errorCodes;
    }

    @JsonIgnore
    public boolean hasClientError() {
        ErrorCode[] errors = getErrorCodes();
        if (errors == null) {
            return false;
        }
        for (ErrorCode error : errors) {
            switch (error) {
                case InvalidResponse:
                case MissingResponse:
                    return true;
            }
        }
        return false;
    }

    enum ErrorCode {
        MissingSecret, InvalidSecret,
        MissingResponse, InvalidResponse;

        private static final Map<String, ErrorCode> errorsMap = new HashMap<String, ErrorCode>(4);

        static {
            errorsMap.put("missing-input-secret", MissingSecret);
            errorsMap.put("invalid-input-secret", InvalidSecret);
            errorsMap.put("missing-input-response", MissingResponse);
            errorsMap.put("invalid-input-response", InvalidResponse);
        }

        @JsonCreator
        public static ErrorCode forValue(String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }

}
