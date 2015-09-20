package org.supermmx.asciidog.backend.html5

import org.supermmx.asciidog.ast.Document
import org.supermmx.asciidog.ast.Node
import org.supermmx.asciidog.backend.AbstractLeafNodeRenderer
import org.supermmx.asciidog.converter.DocumentContext

class Html5InlineXrefRenderer extends AbstractLeafNodeRenderer {
    Html5InlineXrefRenderer() {
        nodeType = Node.Type.CROSS_REFERENCE
    }

    void doPre(DocumentContext context, Node xrefNode) {
        context.writer.with {
            writeStartElement('a')

            // find out the target chunk file
            def file = ''
            if (context.attrContainer.getAttribute(Document.OUTPUT_CHUNKED)) {
                def targetNode = context.document.references[(xrefNode.xrefId)]

                if (targetNode != null) {
                    def targetChunk = context.chunkingStrategy.findChunk(targetNode)

                    def chunk = context.chunk
                    if (chunk != targetChunk) {
                        file = context.chunkingStrategy.getChunkFileName(targetChunk)
                    }
                }
            }

            writeAttribute('href', "${file}#${xrefNode.xrefId}")
        }
    }

    void doRender(DocumentContext context, Node xrefNode) {
        context.writer.writeCharacters(context.document.references[(xrefNode.xrefId)].title)
    }

    void doPost(DocumentContext context, Node xrefNode) {
        context.writer.writeEndElement()
    }
}
