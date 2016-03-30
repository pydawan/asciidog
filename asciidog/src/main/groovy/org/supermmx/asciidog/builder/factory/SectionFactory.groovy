package org.supermmx.asciidog.builder.factory

import org.supermmx.asciidog.ast.Section
import org.supermmx.asciidog.ast.Paragraph
import org.supermmx.asciidog.ast.AdocList

class SectionFactory extends AbstractBlockFactory {
    SectionFactory() {
        name = 'section'

        childClasses = SECTION_CLASSES
    }

    @Override
    def newInstance(FactoryBuilderSupport builder, name, value, Map attributes) {
        Section section = new Section(title: value)
        
        return section
    }
}