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
        // Instantiate the client
        val client = WebClient()
        client.options.isCssEnabled = false
        client.options.isJavaScriptEnabled = false

        // Set up the URL with the search term and send the request
        val searchUrl = "https://www.geeksforgeeks.org/" + URLEncoder.encode(url, "UTF-8")
        val page = client.getPage<HtmlPage>(searchUrl)
        page.asXml()
        val items = page.getByXPath<Any?>("//article") as List<HtmlElement?>
        if (items.isNotEmpty()) {
            return items[0]?.asXml() ?: ""
        }
        return "Not found."
    }
}