package fr.jhelp.kotlinToSwift.lineParser

import java.util.regex.Pattern

/**
 * Pattern for match class/interface declaration.
 *
 * Class line looks like:
 *
 *       <public|internal> class <name_generic> [: <implements_extends>] [{]
 *       <public|internal> interface <name_generic> [: <implements_extends>] [{]
 *
 *       +--------------------------------------------+-----+-----------------------------+
 *       |                  Group                     | ID  |           Capture           |
 *       +--------------------------------------------+-----+-----------------------------+
 *       | (?:public|internal)?\s+(?:class|interface) |  1  | class/interface declaration |
 *       | \s+[a-zA-Z][a-zA-Z0-9_<>]*                 |  2  | class/interface name        |
 *       | \s*:[^{]+                                  |  3  | extends/implements          |
 *       | \s*\{                                      |  4  | Curly ending                |
 *       +--------------------------------------------+-----+-----------------------------+
 */
private val CLASS_INTERFACE_PATTERN =
    Pattern.compile("((?:public|internal)\\s+)?((?:\\s+open\\s+)?(?:class|interface))(\\s+[a-zA-Z][a-zA-Z0-9_<, >]*)(\\s*:[^{]+)?(\\s*\\{)?")
private const val GROUP_CLASS_PUBLIC_INTERNAL = 1
private const val GROUP_CLASS_INTERFACE_DESCRIPTION = 2
private const val GROUP_CLASS_INTERFACE_NAME = 3
private const val GROUP_CLASS_INTERFACE_EXTENDS = 4
private const val GROUP_CLASS_INTERFACE_CURLY = 5

class ClassInterfaceLineParser : LineParser
{
    override fun parse(trimLine: String): String
    {
        val matcher = CLASS_INTERFACE_PATTERN.matcher(trimLine)

        if (matcher.matches())
        {
            val parsed = StringBuilder()

            matcher.group(GROUP_CLASS_PUBLIC_INTERNAL)?.let { header -> parsed.append(header) }
            ?: let { parsed.append("public ") }
            parsed.append(matcher.group(GROUP_CLASS_INTERFACE_DESCRIPTION).replace("interface", "protocol"))
            parsed.append(matcher.group(GROUP_CLASS_INTERFACE_NAME))
            matcher.group(GROUP_CLASS_INTERFACE_EXTENDS)?.let { parsed.append(it) }
            matcher.group(GROUP_CLASS_INTERFACE_CURLY)?.let { parsed.append(it) }
            return parsed.toString()
        }

        return ""
    }
}