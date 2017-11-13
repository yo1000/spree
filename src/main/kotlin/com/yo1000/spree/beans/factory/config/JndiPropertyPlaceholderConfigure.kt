package com.yo1000.spree.beans.factory.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import java.util.*
import javax.naming.InitialContext
import javax.naming.NamingException

/**
 * Subclass of [PropertyPlaceholderConfigurer] that resolves placeholders as
 * JNDI Simple value resources (that is, context.xml Context > Environment
 * entries).
 *
 * for Examples.
 *
 * `context.xml`
 *
 * ```
 * &lt;Context&gt;
 *   &lt;Environment name="Key" value="Value" /&gt;
 * &lt;/Context&gt;
 * ```
 *
 * `applicationContext.xml`
 *
 * ```
 * &lt;bean id="propertyConfigure" class="com.yo1000.spree.beans.factory.config.JndiPropertyPlaceholderConfigure"&gt;
 *   &lt;property name="locations" value="classpath:config.properties"/&gt;
 *   &lt;property name="jndiOverride" value="true"/&gt;
 * &lt;/bean&gt;
 * ```
 *
 * @author yo1000
 */
open class JndiPropertyPlaceholderConfigure(
        /**
         * Whether JNDI Resource value should override local properties within the application.
         * Default is "false": JNDI settings serve as fallback.
         */
        var jndiOverride: Boolean = false
): PropertyPlaceholderConfigurer() {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(JndiPropertyPlaceholderConfigure::class.java)
    }

    override fun resolvePlaceholder(placeholder: String, props: Properties, systemPropertiesMode: Int): String? {
        var value: String? = null
        if (jndiOverride) {
            value = this.resolvePlaceholder(placeholder)
        }

        if (value == null) {
            value = this.resolvePlaceholder(placeholder, props)
        }

        if (value == null && systemPropertiesMode == 1) {
            value = this.resolvePlaceholder(placeholder)
        }

        return value
    }

    /**
     * Default implementation  JNDI name resolves follows
     * "java:comp/env/&#42;", "java:comp/&#42;", "java:&#42;", "&#42;".
     * Can be overridden to customize this behavior.
     *
     * @param placeholder the placeholder to resolve
     */
    open protected fun resolvePlaceholder(placeholder: String): String? {
        var value = lookup(placeholder, "java:comp/env/")
        if (value != null) {
            return value
        }

        value = lookup(placeholder, "java:comp/")
        if (value != null) {
            return value
        }

        value = lookup(placeholder, "java:")
        if (value != null) {
            return value
        }

        value = lookup(placeholder, "")
        return if (value != null) {
            value
        } else null

    }

    private fun lookup(placeholder: String, jndiPrefix: String): String? {
        try {
            val resource = InitialContext().lookup(jndiPrefix + placeholder)
            return resource?.toString()
        } catch (e: NamingException) {
            LOGGER.debug(e.message, e)
            return null
        }
    }
}
