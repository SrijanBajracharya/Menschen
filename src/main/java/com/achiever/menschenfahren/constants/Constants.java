package com.achiever.menschenfahren.constants;

public class Constants {

    public static final String SERVICE_EVENT_API        = "/api";

    public static final String SERVICE_URL_EVENT_PREFIX = SERVICE_EVENT_API + "/events";

    /**
     * Class for defining the token claims
     *
     * @author Srijan Bajracharya
     *
     */
    public class TOKEN {
        /** Claim for setting the username **/
        public static final String CLAIM_USERNAME = "username";

        /** Claim for setting the role of user. **/
        public static final String CLAIM_ROLE     = "roles";

        /** Claim for setting the issuer of the token. **/
        public static final String CLAIM_ISSUER   = "com.achiever.menschenfahren";
        
        /** Claim for the identifier **/
        public static final String CLAIM_INDENTIFIER   = "identifier";
        
        public static final String TOKEN_TYPE   = "Bearer";

    }

    public class ROLE {
        /** Text for user role. **/
        public static final String ROLE_USER = "user";
    }

}
