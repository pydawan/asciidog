package org.supermmx.asciidog.backend.epub

import org.supermmx.asciidog.Utils
import org.supermmx.asciidog.backend.html5.Html5Backend
import org.supermmx.asciidog.backend.AbstractBackend
import org.supermmx.asciidog.converter.DocumentContext
import org.supermmx.asciidog.ast.Block
import org.supermmx.asciidog.ast.Document
import org.supermmx.asciidog.ast.Node

import org.supermmx.epug.creator.EpubCreator
import org.supermmx.epug.epub.Navigation
import org.supermmx.epug.epub.NavigationItem
import org.supermmx.epug.epub.dcmi.DcElement
import org.supermmx.epug.epub.dcmi.DcesTerm
import org.supermmx.epug.epub.dcmi.DcmiTerm

import groovy.util.logging.Slf4j

@Slf4j
class EpubBackend extends Html5Backend {
    EpubBackend() {
        super()

        parentId = id

        id = 'epub'
        ext = '.epub'
    }

    @Override
    void doStartRendering(DocumentContext context) {
        // always chunking for epub
        context.attrContainer.setSystemAttribute(Document.OUTPUT_CHUNKED, true.toString())

        // set the chunk extension
        context.chunkExt = '.xhtml'
    }

    @Override
    void doEndRendering(DocumentContext context) {
        def doc = context.document
        def outputDir = context.outputDir

        def epubCreator = new EpubCreator()

        def rendition = epubCreator.publication.rendition

        // Unique Identifier
        // the id reference is always 'epub-id'
        rendition.uniqueIdentifier = 'epub-id'
        def idAttr= context.attrContainer.getAttribute('epub-id')
        def id = idAttr ? idAttr.value : doc.title
        epubCreator.addDcElement(DcesTerm.identifier, id, 'epub-id')

        // title
        epubCreator.addDcElement(DcesTerm.title, doc.title, null)

        // language
        def langAttr = context.attrContainer.getAttribute('language')
        def lang = langAttr ? langAttr.value : 'en'
        epubCreator.addDcElement(DcesTerm.language, lang, null)

        // modified
        epubCreator.addMetaModified(new Date())

        // find all the chunks
        context.chunkingStrategy.chunks.each { chunk ->
            def chunkFile = new File(outputDir, chunk.fileName)

            epubCreator.addSpineItem(chunkFile.absolutePath, chunk.fileName, chunk.block.id, chunk.block.title)
        }

        // add epub toc item
        Navigation tocNav = createToc(context)
        epubCreator.addNavigation(tocNav)

        // create the epub
        def base = context.attrContainer.getAttribute(Document.OUTPUT_BASE).value

        epubCreator.write(new File(outputDir, base + ext))
    }

    Navigation createToc(DocumentContext context) {
        def nav = new Navigation(type: Navigation.Type.toc)

        def doc = context.document

        def chunk = null
        def anchor = null

        def item = nav
        def navItems = [ ]

        // go through all sections
        def condition = { Node node ->
            node.type == Node.Type.SECTION
        }

        def action = { Node node ->
            Block block = (Block)node
            if (context.chunkingStrategy.isChunkingPoint(block)) {
                chunk = context.chunkingStrategy.getChunk(block)
            } else {
                anchor = Utils.normalizeId(block.id)
            }

            def childItem = new NavigationItem(title: node.title,
                                               file: chunk?.fileName,
                                               anchor: anchor)
            anchor = null

            item.items << childItem

            item = childItem
        }

        def pre = { Block block ->
            navItems << item
        }

        def post = { Block block ->
            navItems = navItems.dropRight(1)
            if (navItems.size() > 0) {
                item = navItems.last()
            } else {
                item = null
            }
        }

        doc.walk(condition, action, pre, post);

        return nav
    }
}
