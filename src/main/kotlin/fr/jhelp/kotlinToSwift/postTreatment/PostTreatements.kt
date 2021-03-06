package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.protocol.parseProtocolsInFiles
import java.io.File

fun postTreatments(files: List<File>)
{
    var transformed: String

    for (file in files)
    {
        transformed = parseCompanionInFile(file.readText())
        transformed = parseWhenInFile(transformed)
        transformed = parseConstructorInFile(transformed)
        transformed = parseEnumFile(transformed)

        file.writeText(transformed)
    }

    parseProtocolsInFiles(files)
}