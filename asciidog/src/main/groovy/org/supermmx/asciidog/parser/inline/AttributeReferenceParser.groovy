package org.supermmx.asciidog.parser.inline

import org.supermmx.asciidog.Parser
import org.supermmx.asciidog.ast.Inline
import org.supermmx.asciidog.ast.Node
import org.supermmx.asciidog.ast.InlineInfo
import org.supermmx.asciidog.ast.AttributeReferenceNode
import org.supermmx.asciidog.parser.inline.InlineParserPlugin

/*
import org.supermmx.asciidog.ast.AttributeSettingNode
import org.supermmx.asciidog.ast.CounterSettingNode
*/

import java.util.regex.Matcher

/**
 * Attribute Refrence parser plugin
 */
class AttributeReferenceParser extends InlineParserPlugin {
    static final def ATTRIBUTE_REFERENCE_PATTERN = ~'''(?Usxm)
(\\\\?)             # 1, escape
\\{
(                   # 2
  (set|counter2?)   # 3, set or counter
  :
  ([^:]+?)          # 4, attribute name
  (?:
    :
    (.*?)           # 5, attribute value
  )?
  |
  (                 # 6, attribute name
    [\\w-.]+?
  )
)
\\}
'''

    static final String ID = 'plugin:parser:inline:attribute_reference'

    AttributeReferenceParser() {
        id = ID
        nodeType = Node.Type.ATTRIBUTE_REFERENCE

        pattern = ATTRIBUTE_REFERENCE_PATTERN
    }

    @Override
    protected List<Inline> createNodes(Matcher m, List<String> groups) {
        Inline inline = null;

        def action = groups[3]
        if (action == 'set') {
            //inline = new AttributeSettingNode()
        } else if (action == 'counter'
            || action == 'counter2') {
            //inline = new CounterSettingNode()
        } else {
            // normal reference
            inline = new AttributeReferenceNode()
            inline.name = groups[6]
        }

        return [ inline ]
    }

    @Override
    protected boolean fillNodes(List<InlineInfo> infoList, Matcher m, List<String> groups) {
        infoList[0].with {
            inlineNode.escaped = (groups[0] == '')

            contentStart = m.start(2)
            contentEnd = m.end(2)
        }

        return true
    }
}
