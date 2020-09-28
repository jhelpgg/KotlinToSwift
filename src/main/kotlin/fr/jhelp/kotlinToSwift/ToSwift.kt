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
    val source = File(args[0])
    val destination = File(args[1])
    destination.deleteRecursively()

    //Transform code
    swiftTransformer(source, destination)

    //Transfer prebuilt files
    transferResource("WorkHelper.swift", destination)
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