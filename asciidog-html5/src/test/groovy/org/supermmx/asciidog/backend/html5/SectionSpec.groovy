package org.supermmx.asciidog.backend.html5

import groovy.xml.*

class SectionSpec extends Html5Spec {
    def 'section'() {
        given:
        def content = '''= Document Title

== Section Title
'''
        def expectedBody = markupHtml {
            body {
                h1 'Document Title'
                h2 'Section Title'
            }
        }

        when:
        def html = adocHtml(content)

        def body = XmlUtil.serialize(html.body)

        then:
        body == expectedBody
    }
}
