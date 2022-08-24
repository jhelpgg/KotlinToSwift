package fr.jhelp.kotlinToSwift.lineParser

class IgnoredAnnotationsParser : LineParser
{
    companion object
    {
        private val IGNORED_ANNOTATIONS_START = arrayOf("@Jvm")
    }

    override fun parse(trimLine: String): String
    {
        for (ignoredAnnotationStart in IGNORED_ANNOTATIONS_START)
        {
            if (trimLine.startsWith(ignoredAnnotationStart))
            {
                return " "
            }
        }

        return ""
    }
}