package fr.jhelp.kotlinToSwift.lineParser

interface LineParser
{
    /**
     * Parse the given line from Kotlin to Swift.
     *
     * If the parser don't managed the given line, it returns an empty String
     *
     * The given line is trim, so start with interesting characters
     *
     * If parser can treat the the line, the swift version is returned
     */
    fun parse(trimLine: String): String
}

/** Parsers to test. WARNING : Order is important since first match win*/
val PARSERS: Array<LineParser> =
        arrayOf(ClassInterfaceLineParser(),
                ConstructorLineParser(),
                DeclarationLineParser(),
                FunLineParser(),
                GuardInterfaceLineParser(),
                TryLineParser(),
                AnnotationLineParser(),
                CatchLineParser(),
                IfForWhileLineParser(),
                ImportSwiftLineParser(),
                ClosureLambdaLineParser(),
                AsIsLineParser())

/**
 * Convenient method for parse (Use parsers in good order)
 */
fun parseLine(trimLine: String): String
{
    var line: String

    for (lineParser in PARSERS)
    {
        line = lineParser.parse(trimLine)

        if (line.isNotEmpty())
        {
            return line
        }
    }

    return ""
}
