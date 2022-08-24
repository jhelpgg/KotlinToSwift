package fr.jhelp.kotlinToSwift.postTreatment

private const val EXTENSION_HEADER = "public extension "

fun parseExtension(file: String): String
{
    if (file.contains(EXTENSION_HEADER))
    {
        return "$file\n}"
    }

    return file
}
