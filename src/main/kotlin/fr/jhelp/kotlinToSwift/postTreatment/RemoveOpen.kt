package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.KotlinToSwiftOptions
import java.io.File
import java.util.regex.Pattern

private val OPEN_REGEX = Pattern.compile("(^|\\s)open\\s")

fun removeOpen(files: List<File>)
{
    if (KotlinToSwiftOptions.removeOpen)
    {
        for (file in files)
        {
            var text = file.readText()
            text = OPEN_REGEX.matcher(text).replaceAll("$1")
            file.writeText(text)
        }
    }
}