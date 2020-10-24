package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.protocol.parseProtocolsInFiles
import java.io.File
import java.util.regex.Pattern

private val INTERNAL_OPEN_PATTERN = Pattern.compile("internal\\s+open\\s+")
private const val INTERNAL_OPEN_REPLACEMENT = "open "

fun postTreatments(files: List<File>)
{
    var transformed: String

    for (file in files)
    {
        transformed = parseCompanionInFile(file.readText())
        transformed = parseWhenInFile(transformed)
        transformed = parseConstructorInFile(transformed)
        transformed = parseEnumFile(transformed)
        transformed = parseExceptionFile(transformed)
        transformed = parseTestFile(transformed)
        transformed = parseEqualsInFile(transformed)
        transformed = parseComparableInFile(transformed)
        transformed = parseToStringInFile(transformed)

        transformed = INTERNAL_OPEN_PATTERN.matcher(transformed).replaceAll(INTERNAL_OPEN_REPLACEMENT)

        file.writeText(transformed)
    }

    parseProtocolsInFiles(files)
}