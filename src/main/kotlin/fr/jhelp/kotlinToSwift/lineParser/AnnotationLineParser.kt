package fr.jhelp.kotlinToSwift.lineParser

import java.util.regex.Matcher
import java.util.regex.Pattern


private val ANNOTATION_REPLACEMENT = arrayOf(
    Pair(Pattern.compile("@Try\\s+((?:(?:var|val|let)\\s+)?[a-zA-Z0-9_\\[\\]]+\\s*=)(.*)"), "$1 try $2"),
    Pair(Pattern.compile("@Try([^a-zA-Z0-9_].*)"), "try$1"),
    Pair(Pattern.compile("@Override([^a-zA-Z0-9_].*)"), "override$1"))


class AnnotationLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        var matcher: Matcher

        for ((pattern, replacement) in ANNOTATION_REPLACEMENT)
        {
            matcher = pattern.matcher(trimLine)

            if (matcher.matches())
            {
                return matcher.replaceAll(replacement)
            }
        }

        return ""
    }
}