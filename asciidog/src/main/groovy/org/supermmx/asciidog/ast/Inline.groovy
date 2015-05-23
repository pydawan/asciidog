package org.supermmx.asciidog.ast

import java.util.regex.Matcher

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Canonical
@EqualsAndHashCode(callSuper=true)
@ToString(includeSuper=true, includePackage=false, includeNames=true)

/**
 * Base inline with inline information
 */
abstract class Inline extends Node {
    InlineInfo info = new InlineInfo()
}