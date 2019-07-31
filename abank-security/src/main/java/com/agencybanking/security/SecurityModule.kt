package com.agencybanking.security

import com.agencybanking.security.password.PasswordReset
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security")
open class SecurityModule {
    val passwordPolicy = PasswordPolicy(
            resetSendType = PasswordReset.SEND_TYPE_LINK,
            resetLinkExpirationHrs = 1,
            resetTokenExpirationHrs = 1)

    val jwt = Jwt(
            header = "Authorization",
            secret= "MySecret",
            expiration= 10000,
            accessTokenExpiry= 120,
            firstTimeTokenExpiry= 600
            )

    class PasswordPolicy(val resetSendType: String, val resetLinkExpirationHrs: Int, val resetTokenExpirationHrs: Int)


    class Jwt(val header: String,val secret: String,val expiration: Long, val accessTokenExpiry: Long, val firstTimeTokenExpiry: Long)


    companion object {
        val CODE = "security"

        val ERR_USERNAME_EXISTS = "err.mbcp-201"
        val ERR_EMAIL_EXISTS = "err.mbcp-202"
        val WARN_ADD_CMPY_TO_USER = "wrn.mbcp-203"
        val SUCCESS_USER_CREATED = "suc.mbcp-204"
        val SUCCESS_USER_ASSIGNED_CMPY = "suc.mbcp-205"
        val SUCCESS_ROLE_CREATED = "suc.mbcp-206"
        val ERR_ROLE_EXISTS = "err.mbcp-207"
        val WARN_ROLE_PERM_EXISTS = "wrn.mbcp-208"
        val ERR_ROLE_PERM_CREATE = "err.mbcp-209"
        val SUCCESS_ROLE_PERM_CREATED = "suc.mbcp-210"
        val SUCCESS_BOOKMARKED_CREATED = "suc.mbcp-211"
        val ERR_BOOKMARKED_ERROR = "err.mbcp-212"
        val WARNING_BOOKMARK_EXISTS = "wrn.mbcp-213"
        val SUCCESS_GROUP_CREATED = "suc.mbcp-214"
        val ERR_GROUP_EXISTS = "err.mbcp-215"

        val ERR_COMPANY_USER_EXISTS = "err.mbcp-216"
        val SUCCESS_COMPANY_USER_CREATED = "suc.mbcp-217"

        val ERR_USER_GROUP_EXISTS = "err.mbcp-218"
        val SUCCESS_USER_GROUP_CREATED = "suc.mbcp-219"
        val WARN_USER_ROLE_EXISTS = "wrn.mbcp-220"
        val SUCCESS_USER_ROLE_CREATED = "suc.mbcp-221"
        val SUCCESS_COMPANY_CREATED = "suc.mbcp-222"
        val SUCCESS_ROLE_UPDATED = "suc.mbcp-223"
        val WARN_USER_GROUP_EXISTS = "wrn.mbcp-224"
        val ERR_AUTHCONFIG_EXISTS = "err.mbcp-225"
        val SUCCESS_AUTHCONFIG_UPDATED = "suc.mbcp-226"
        val SUCCESS_AUTHCONFIG_CREATED = "suc.mbcp-227"
        val ERR_NO_ACCOUNT = "err.mbcp-228"
        val ERR_FIRST_LOGIN = "inf.mbcp-229"
        val ERR_NO_COMPANY_USER = "err.mbcp-230"
        val ERR_ACCOUNT_LOCKED = "err.mbcp-231"
        val ERR_USER_INACTIVE = "err.mbcp-233"
        val ERR_BAD_CREDENTIALS = "err.mbcp-234"
        val ERR_PROVIDER_NOT_FOUND = "err.mbcp-235"
        //LdapDetails Messages
        val ERR_LDAPDETAILS_EXISTS = "err.mbcp-236"
        val SUCCESS_LDAPDETAILS_CREATED = "suc.mbcp-237"
        val SUCCESS_LDAPDETAILS_UPDATED = "suc.mbcp-238"
        val ERR_MAX_AUTH_LEVEL_EXCEEDED = "err.mbcp-239"
        val ERR_COMPANY_NOT_EXIST = "err.mbcp-240"
        val SUCCESS_AUTH_TOKENSERVER_SAVED = "suc.mbcp-241"
        val ERR_TOKENSERVER_SAVE_FAILED = "err.mbcp-242"
        val ERR_COMPANY_EXISTS = "err.mbcp-243"
        val SUCCESS_COMPANY_UPDATED = "suc.mbcp-244"
        val SUCCESS_USER_UPDATED = "suc.mbcp-245"
        val SUCCESS_COMPANY_USER_UPDATED = "suc.mbcp-246"
        val SUCCESS_RESET_PASSWORD_TOKEN_EMAIL = "suc.mbcp-247"
        val ERROR_INVALID_TOKEN = "suc.mbcp-248"
        val ERROR_EXPIRED_TOKEN = "suc.mbcp-249"
        val SUCCESS_RESET_PASSWORD = "suc.mbcp-250"
        val ERR_RESET_PASSWORD = "err.mbcp-251"
        val ERR_PWORD_RESET_NO_USER = "err.mbcp-252"

        val SUCCESS_RESET_PASSWORD_LINK_EMAIL = "suc.mbcp-253"
        val INFO_FIRST_TIME_LOGIN = "inf.mbcp-254"

        val ERR_TIN_EXISTS = "err.mbcp-255"
        val ERR_RC_EXISTS = "err.mbcp-256"

        val ERR_INVALID_LOGIN_STATE = "err.mbcp-257"

        val ERR_SECURITY_POLICY_EXISTS = "err.mbcp-258"
        val SUCCESS_SECURITY_POLICY_UPDATED = "suc.mbcp-259"

        val SUCCESS_ROLE_DELETE = "suc.mbcp-260"
        val ERROR_ROLE_DELETE_ASSIGNED_TO_USER = "err.mbcp-261"

        val ERR_COMPANY_PRODUCT_CREATE = "err.mbcp-262"
        val SUCCESS_COMPANY_PRODUCT_CREATED = "suc.mbcp-263"

        val WARN_COMPANY_PRODUCT_EXISTS = "wrn.mbcp-264"

        val SUCCESS_USER_ROLE_UPDATED = "suc.mbcp-265"


        val ERR_ACCESS_DENIED = "err.mbcp-266"
        val INFO_EXPIRED_ROLE = "inf.mbcp-267"
        val INFO_DATE_OF_ROLE_EXPIRY = "inf.mbcp-268"
        val ERR_PASSWORD_UPDATE = "err.mbcp-269"
    }
}
