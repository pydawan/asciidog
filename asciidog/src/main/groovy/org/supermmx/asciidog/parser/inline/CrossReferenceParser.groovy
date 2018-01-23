package org.supermmx.asciidog.parser.inline

import org.supermmx.asciidog.Parser
import org.supermmx.asciidog.ast.CrossReferenceNode
import org.supermmx.asciidog.ast.Inline
import org.supermmx.asciidog.ast.Node
import org.supermmx.asciidog.parser.inline.InlineParserPlugin

import java.util.regex.Matcher

/**
 * Plugin for Cross Reference
 */
class CrossReferenceParser extends InlineParserPlugin {
    static final def CROSS_REFERENCE_PATTERN = ~'''(?Usxm)
(\\\\?)             # 1, escape
(?:
  \\[
     ([^\\]]+?)     # 2, Attributes
  \\]
)?
<<
(.*?)          # 3, id, allow any characters
>>
'''
    static final String ID = 'plugin:parser:inline:cross_reference'

    CrossReferenceParser() {
        id = ID
        nodeType = Node.Type.CROSS_REFERENCE

        pattern = CROSS_REFERENCE_PATTERN
    }

    @Override
    protected List<Inline> createNodes(Matcher m, List<String> groups) {
        CrossReferenceNode xrNode = new CrossReferenceNode()

        return [ xrNode ]
    }

    @Override
    protected boolean fillNodes(List<Inline> infoList, Matcher m, List<String> groups) {
        infoList[0].with {
            contentStart = m.start(3)
            contentEnd = m.end(3)

            inlineNode.with {
                escaped = (groups[1] != '')

                xrefId = groups[3]

                def attrsStr = groups[2]
                if (attrsStr != null) {
                    def attrs = Parser.parseAttributes(attrsStr)
                    attributes.putAll(attrs)
                }
            }
        }

        return true
    }
}