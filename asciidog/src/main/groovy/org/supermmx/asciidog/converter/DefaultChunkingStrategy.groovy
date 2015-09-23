package org.supermmx.asciidog.converter

import org.supermmx.asciidog.ast.Block
import org.supermmx.asciidog.ast.Node
import org.supermmx.asciidog.ast.Document

class DefaultChunkingStrategy extends AbstractChunkingStrategy {
    DefaultChunkingStrategy(DocumentContext context) {
        initialize(context)
    }

    @Override
    protected boolean isChunkingPoint(Block block) {
        def createChunk = false

        // whether it is chunked or not
        def chunked = context.attrContainer.getAttribute(Document.OUTPUT_CHUNKED).value
        def isStream = context.attrContainer.getAttribute(Document.OUTPUT_STREAM).value
        def type = block.type

        if (type == Node.Type.DOCUMENT) {
            // always create a chunk for document
            createChunk = true
        } else if (chunked && !isStream) {
            // no need to create chunk for streaming

            // TODO: check levels
            createChunk = (block.type == Node.Type.SECTION)
        }

        return createChunk
    }
}
