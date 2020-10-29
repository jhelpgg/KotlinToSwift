package fr.jhelp.kotlinToSwift

import java.io.File

private class ToSwift

fun main(args: Array<String>)
{
    if (args.size < 2)
    {
        println("Need Kotlin source directory and Swift destination directory")
        return
    }

    //Init
    val destination = File(args[args.size - 1])
    destination.deleteRecursively()

    //Transform code
    for(sourceIndex in 0 until  args.size-1)
    {
        swiftTransformer(File(args[sourceIndex]), destination)
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