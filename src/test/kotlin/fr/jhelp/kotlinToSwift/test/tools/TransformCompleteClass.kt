package fr.jhelp.kotlinToSwift.test.tools

import fr.jhelp.kotlinToSwift.internalSwiftTransformer
import java.io.File
import java.io.IOException

fun transformCompleteClass(kotlwiftSource: String): String
{
    val source = File(outsideDirectory, "source")

    if (!source.createFile())
    {
        throw IOException("Can't create : ${source.absolutePath}")
    }

    source.writeText(kotlwiftSource)
    val destination = File(outsideDirectory, "destination")

    if (!destination.createFile())
    {
        throw IOException("Can't create : ${destination.absolutePath}")
    }

    internalSwiftTransformer(source, destination)
    val transformed = destination.readText()
    source.deleteComplete()
    destination.deleteComplete()
    return transformed
}