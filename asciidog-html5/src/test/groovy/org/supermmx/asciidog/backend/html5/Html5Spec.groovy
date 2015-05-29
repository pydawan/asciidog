package org.supermmx.asciidog.backend.html5

import org.supermmx.asciidog.Parser
import org.supermmx.asciidog.Reader

import groovy.xml.*

import spock.lang.*

class Html5Spec extends Specification {
    @Shared
    def backend = new Html5Backend()

    @Shared
    def renderer = backend.createRenderer([:])

    /**
     * Create xml slurper from HTML string
     */
    def html(String content) {
        def html = new XmlSlurper(false, false, true).parseText(content)

        return html
    }

    /**
     * Create xml slurper from AsciiDoc, and return the html string
     * of the element returned from the closure
     */
    def adocHtml(String text, Closure closure) {
        def parser = new Parser()
        def reader = Reader.createFromString(text)
        parser.reader = reader
        def doc = parser.parseDocument()

        def baos = new ByteArrayOutputStream()
        renderer.renderDocument(doc, baos)

        def htmlText = baos.toString('UTF-8')

        def html = html(htmlText)

        return XmlUtil.serialize(closure(html))
    }

    /**
     * Create HTML string from markup
     */
    def markupHtml(Closure closure) {
        return XmlUtil.serialize(new StreamingMarkupBuilder().bind(closure))
    }
}
