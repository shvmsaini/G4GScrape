package com.shvmsaini.gfg

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.net.URLEncoder

@RestController
class Controller {

    @GetMapping("/")
    fun getHelloWorld(): String {
        return "Hello, world!"
    }

    @GetMapping("/content/{url}")
    fun getContent(@PathVariable("url") url: String): String {
        val url1: String = if (url.last() == '/') url.substring(0, url.lastIndex - 1) else url
        // Instantiate the client
        val client = WebClient()
        client.options.isCssEnabled = false
        client.options.isJavaScriptEnabled = false

        // Set up the URL with the search term and send the request
        val searchUrl = "https://www.geeksforgeeks.org/" + URLEncoder.encode(url1, "UTF-8")
        val page = client.getPage<HtmlPage>(searchUrl)
        page.asXml()
        val items = page.getByXPath<Any?>("//article") as List<*>
        if (items.isNotEmpty()) {
            // Removing Unnecessary stuff
            val elements = page.getByXPath<Any?>("//*[@id]") as List<*>
            elements.forEach { element ->
                val elementId = (element as HtmlElement).id
                if (elementId != null && elementId.contains(Regex("GFG|G4G|myDropdown"))) {
                    element.parentNode.removeChild(element)
                }
            }

            val mainPage = items[0] as? HtmlElement
            return mainPage?.asXml() ?: "Whoops! Some error."
        }
        return "Not found."
    }
}