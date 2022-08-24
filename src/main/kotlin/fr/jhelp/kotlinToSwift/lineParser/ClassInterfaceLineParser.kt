package fr.jhelp.kotlinToSwift.lineParser

import fr.jhelp.kotlinToSwift.KotlinToSwiftOptions
import java.util.regex.Pattern

/**
 * Pattern for match class/interface declaration.
 *
 * Class line looks like:
 *
 *       [<public|internal>] [open] class <name_generic> [: <implements_extends>] [{]
 *       [<public|internal>] [open] interface <name_generic> [: <implements_extends>] [{]
 *
 *       +--------------------------------------------+-----+-------------------------------+
 *       |                  Group                     | ID  |           Capture             |
 *       +--------------------------------------------+-----+-------------------------------+
 *       | (?:public|internal)\s+                     |  1  | class or interface visibility |
 *       | (?:\s+open\s+)?(?:class|interface)         |  2  | class/interface declaration   |
 *       | \s+[a-zA-Z][a-zA-Z0-9_<>]*                 |  3  | class/interface name          |
 *       | \s*:[^{]+                                  |  4  | extends/implements            |
 *       | \s*\{                                      |  5  | Curly ending                  |
 *       +--------------------------------------------+-----+-------------------------------+
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
            ?: if (KotlinToSwiftOptions.automaticPublic)
            {
                parsed.append("public ")
            }
            parsed.append(matcher.group(GROUP_CLASS_INTERFACE_DESCRIPTION).replace("interface", "protocol"))
            parsed.append(matcher.group(GROUP_CLASS_INTERFACE_NAME))
            matcher.group(GROUP_CLASS_INTERFACE_EXTENDS)?.let { parsed.append(it) }
            matcher.group(GROUP_CLASS_INTERFACE_CURLY)?.let { parsed.append(it) }
            return parsed.toString()
        }

        return ""
    }
}