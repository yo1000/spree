package com.yo1000.spree.beans.factory.config

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on
import org.springframework.mock.jndi.SimpleNamingContextBuilder
import kotlin.test.assertEquals

/**
 *
 * @author yo1000
 */
class JndiPropertyPlaceholderConfigureTest : Spek({
    given("the JNDI Resolver") {
        val contextBuilder = SimpleNamingContextBuilder()
        contextBuilder.bind("java:comp/env/Name1", "Value1")
        contextBuilder.bind("java:comp/Name2", "Value2")
        contextBuilder.bind("java:Name3", "Value3")
        contextBuilder.bind("Name4", "Value4")
        contextBuilder.activate()

        val data = arrayOf(
                data("Name1", "Value1"),
                data("Name2", "Value2"),
                data("Name3", "Value3"),
                data("Name4", "Value4"),
                data("Name0", "")
        )

        on("resolvePlaceholder by '%s'", *data) { jndiName, expected ->
            val driver = object : JndiPropertyPlaceholderConfigure() {
                fun doResolvePlaceholder(jndiName: String): String? {
                    return resolvePlaceholder(jndiName)
                }
            }

            it("should resolve to '$expected'") {
                assertEquals(driver.doResolvePlaceholder(jndiName) ?: "", expected)
            }
        }
    }
})
