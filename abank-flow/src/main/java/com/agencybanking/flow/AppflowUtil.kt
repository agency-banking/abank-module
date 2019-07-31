package com.agencybanking.flow

import com.agencybanking.core.services.BizEvent
import org.springframework.util.Assert
import org.springframework.util.Assert.hasLength
import org.springframework.util.StringUtils
import java.util.regex.Pattern

object AppflowUtil {
    val USER_REGEX = "user\\((.*?)\\)"
    val ROLE_REGEX = "role\\((.*?)\\)"
    val GROUP_REGEX = "group\\((.*?)\\)"

    fun userRecipient(recipient: String): String? {
        return matchRecipientExpression(recipient, USER_REGEX)
    }

    fun roleRecipient(recipient: String): String? {
        return matchRecipientExpression(recipient, ROLE_REGEX)
    }

    fun groupRecipient(recipient: String): String? {
        return matchRecipientExpression(recipient, GROUP_REGEX)
    }

    private fun matchRecipientExpression(to: CharSequence, userRoleGroupRegex: String): String? {
        val pattern = Pattern.compile(userRoleGroupRegex)
        val matcher = pattern.matcher(to)
        return if (matcher.find()) {
            matcher.group(1)
        } else null
    }

    //    public static String toUser(String userId) {
    //        return String.format("user(%s)", userId);
    //    }

    //    public static String toRole(String roleRef) {
    //        return String.format("role(%s)", roleRef);
    //    }
    //
    //    public static String toGroup(String groupCode) {
    //        return String.format("group(%s)", groupCode);
    //    }
    fun concoctProcessId(evt: BizEvent): String {
        return processId(evt.product, evt.action)
    }

    fun processId(product: String, event: String): String {
        hasLength(product, "Product is required for process ID creation")
        Assert.notNull(event, "Event Type is required for process ID creation")
        return ("${product}_$event").toLowerCase()
    }

    fun extractProduct(processId: String): String {
        return processId.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    }

//    fun identityToName(link: IdentityLink): String? {
//        when (link.getType()) {
//            IdentityLink.TYPE_ROLE -> return refToName(link.getRoleId())
//            IdentityLink.TYPE_GROUP -> return refToName(link.getGroupId())
//            IdentityLink.TYPE_USER -> return link.getUserId()
//        }
//        return null
//    }

    fun refToName(ref: String): String {
        return StringUtils.capitalize(ref.substring(0, ref.lastIndexOf("_")).replace("_".toRegex(), " "))
    }
}
