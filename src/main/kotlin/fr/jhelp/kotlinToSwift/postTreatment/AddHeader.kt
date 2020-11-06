package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.KotlinToSwiftOptions
import java.io.File
import java.util.regex.Pattern

private val regexReplacement = ArrayList<Pair<Pattern, String>>()

fun addHeaderClass(files: List<File>)
{
    val header = KotlinToSwiftOptions.headerClass

    if (header.isEmpty())
    {
        return
    }

    val newFiles = HashMap<String, File>()

    for (file in files)
    {
        val name = file.name.removeSuffix(".swift")
        val newName = "$header$name"
        regexReplacement.add(Pair(name.nameRegex, newName.nameReplacement))
        newFiles[file.absolutePath] = File(file.parentFile, "${newName}.swift")
    }

    for (file in files)
    {
        replace(file, newFiles[file.absolutePath]!!, regexReplacement)
    }
}

private fun replace(source: File, destination: File, regexReplacement: ArrayList<Pair<Pattern, String>>)
{
    var text = source.readText()

    for ((pattern, replacement) in regexReplacement)
    {
        text = pattern.matcher(text).replaceAll(replacement)
    }

    destination.writeText(text)
    source.delete()
}

private val String.nameRegex: Pattern get() = Pattern.compile("(^|[^a-z-A-Z0-9_])$this([^a-z-A-Z0-9_]|$)")
private val String.nameReplacement: String get() = "$1$this$2"
