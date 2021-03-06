import org.supermmx.asciidog.plugin.*
import org.supermmx.asciidog.parser.action.*
import org.supermmx.asciidog.parser.block.*
import org.supermmx.asciidog.parser.inline.*

asciidog {
    // the name of the plugin
    name = 'asciidog-core'

    // the customized plugin loader, optioinal
    suite = AsciidogCorePluginSuite

    backends = [
    ]

    // the plugins
    plugins = [
        // block parser
        AttributeEntryParser,
        AuthorParser,
        BlockMacroParser,
        DocumentParser,
        HeaderParser,
        ListItemParser,
        OrderedListParser,
        ParagraphParser,
        PreambleParser,
        SectionParser,
        UnOrderedListParser,

        // inline parsers
        AttributeReferenceParser,
        CrossReferenceParser,
        ConstrainedEmphasisFormattingParser,
        ConstrainedStrongFormattingParser,
        EmphasisFormattingParser,
        StrongFormattingParser,
    ]

    builders = [
    ]
}
