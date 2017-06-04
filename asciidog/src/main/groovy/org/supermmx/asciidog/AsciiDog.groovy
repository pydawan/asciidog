package org.supermmx.asciidog

import org.supermmx.asciidog.ast.Document
import org.supermmx.asciidog.converter.Converter
import org.supermmx.asciidog.plugin.PluginRegistry

import org.apache.commons.cli.Option

import groovy.util.logging.Slf4j

import org.slf4j.Logger

@Slf4j(category='AsciiDog')
class AsciiDog {
    void execute(String[] args) {
        def cli = new CliBuilder(usage: 'asciidog [OPTIONS]')
        cli.with {
            i(longOpt: 'input-file', args: 1, argName: 'input-file', required: true, 'Input AsciiDoc file')
            o(longOpt: 'output-dir', args: 1, argName: 'output-dir', required: true, 'Output directory')
            B(longOpt: 'base', args: 1, argName: 'base', 'Base name')
            b(longOpt: 'backends', args: Option.UNLIMITED_VALUES, argName: 'backends', valueSeparator: ',', 'Comma-separated backend IDs')

            oc(longOpt: 'output-chunked', 'Enable chunked output')
        }

        def options = cli.parse(args)

        if (!options) {
            return
        }

        def file = options.i
        def output = options.o

        def base = options.B
        if (!base) {
            base = new File(file).name.split('\\.')[0]
        }

        def backends = options.bs
        if (!backends) {
            backends = [ 'html5' ]
        }

        def adOptions = [:]
        adOptions[(Document.OUTPUT_BASE)] = base

        if (options.oc) {
            adOptions[(Document.OUTPUT_CHUNKED)] = options.oc.toString()
        }

        // more options
        adOptions[Document.OUTPUT_WRITING_MODE] = Document.WritingMode.vrl.toString()

        log.info("[Converter] AsciiDoc Input File: {}", file)
        log.info("[Converter] Backends: {}", backends)
        log.info("[Converter] Converted Output Directory: {}", output)
        log.info("[Converter] Converted Output Base Name: {}", base)

        log.info("[Converter] Options: ${adOptions}")

        // parse
        Parser parser = new Parser()
        Document doc = parser.parseFile(file)

        log.debug('Parsed document = \n{}', doc)

        // render
        Converter converter = new Converter()

        backends.each{ id ->
            log.info("[Converter] Rendering with backend {}...", id);
            converter.convertToFile(doc, output, id,
                                    adOptions)
        }
    }

    static void main(String[] args) {
        def asciidog = new AsciiDog()

        asciidog.execute(args)
    }
}
