package org.supermmx.asciidog.plugin

import org.supermmx.asciidog.Parser
import org.supermmx.asciidog.ast.Node
import org.supermmx.asciidog.ast.FormattingNode

import groovy.util.logging.Slf4j

import org.slf4j.Logger

/**
 * Plugin registry
 */
@Slf4j
@Singleton(strict=false)
class PluginRegistry {
    List<Plugin> plugins = []

    PluginRegistry() {
        registerDefaultPlugins()
    }

    void register(Plugin plugin) {
        log.info "Registering plugin ID: '${plugin.id}', Type: ${plugin.type}, Node Type: ${plugin.nodeType}"

        if (plugins.find { it.id == plugin.id } == null) {
            plugins << plugin
        }
    }

    List<Plugin> getPlugins(Closure condition) {
        return plugins.findAll(condition)
    }

    List<InlineParserPlugin> getInlineParserPlugins() {
        return plugins.findAll { plugin ->
            plugin.nodeType.isInline() && plugin.isParserPlugin()
        }
    }

    final static def TEXT_FORMATTING_PLUGINS_DATA = [
        // id, formatting type, constrained, pattern
        [ 'strong_unconstrained', FormattingNode.Type.STRONG, false, Parser.STRONG_UNCONSTRAINED_PATTERN ],
        [ 'strong_constrained', FormattingNode.Type.STRONG, true, Parser.STRONG_CONSTRAINED_PATTERN ],
        [ 'emphasis_unconstrained', FormattingNode.Type.EMPHASIS, false, Parser.EMPHASIS_UNCONSTRAINED_PATTERN ],
        [ 'emphasis_constrained', FormattingNode.Type.EMPHASIS, true, Parser.EMPHASIS_CONSTRAINED_PATTERN ],
    ]

    private void registerDefaultPlugins() {
        TEXT_FORMATTING_PLUGINS_DATA.each { pluginData ->
            def (id, ftType, constrained, pattern) = pluginData
            def plugin = new TextFormattingInlineParserPlugin(id: id, formattingType: ftType,
                                                              constrained: constrained, pattern: pattern)

            register(plugin)
        }

        // Cross Reference Inline Parser Plugin
        register(new CrossReferenceInlineParserPlugin())
    }
}
