package fr.jhelp.kotlinToSwift

import java.io.File

private class ToSwift

fun main(args: Array<String>)
{
    val parameters = KotlinToSwiftOptions.extractOptions(args)

    if (parameters.size < 2)
    {
        println("Need Kotlin source directory and Swift destination directory")
        return
    }

    //Init
    val destination = File(parameters[parameters.size - 1])
    destination.deleteRecursively()

    //Transform code
    for (sourceIndex in 0 until parameters.size - 1)
    {
        swiftTransformer(File(parameters[sourceIndex]), destination)
    }

    //Transfer prebuilt files
    transferResource("WorkHelper.swift", destination)
    transferResource("TimeCalendar.swift", destination)
    transferResource("Pair.swift", destination)
    transferResource("GenericError.swift", destination)
}

private fun transferResource(resourceName: String, destination: File)
{
    try
    {
        val url = ToSwift::class.java.getResource(resourceName)
        val toCopy = url.openStream().bufferedReader()
        val whereCopy = File(destination, resourceName).bufferedWriter()
        toCopy.lines().forEach { line ->
            whereCopy.write(line)
            whereCopy.newLine()
        }
        whereCopy.flush()
        whereCopy.close()
        toCopy.close()
    }
    catch (exception: Exception)
    {
        println("Failed to copy resource: $resourceName in ${destination.absolutePath}")
        exception.printStackTrace()
    }
}