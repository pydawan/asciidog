package org.supermmx.asciidog.ast

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@Canonical
@EqualsAndHashCode(callSuper=true)
@ToString(includeSuper=true, includePackage=false, includeNames=true)

class Block extends Node {
    String title
    List<String> lines = []
    List<Block> blocks = []

    Block leftShift(Block block) {
        blocks << block

        return this
    }
}