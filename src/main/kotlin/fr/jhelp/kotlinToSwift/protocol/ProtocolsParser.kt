package fr.jhelp.kotlinToSwift.protocol

import java.io.File
import java.util.Stack
import java.util.TreeSet
import java.util.regex.Matcher
import java.util.regex.Pattern

val PROTOCOL = Pattern.compile(
    "protocol\\s+([a-zA-Z][a-zA-Z0-9_]*)\\s*(<[a-zA-Z0-9_, ]+>)?\\s*(:\\s*[a-zA-Z0-9_,<> ]+)?\\s*\\{")
val GROUP_NAME = 1
val GROUP_PROTOCOL_GENERICS = 2
val GROUP_IMPLEMENTED_PROTOCOLS = 3
val PROTOCOL_IMPLEMENTS = Pattern.compile("([a-zA-Z][a-zA-Z0-9_]*)\\s*(<[a-zA-Z0-9_, ]+>)?")
val PROTOCOL_OR_CLASS = Pattern.compile(
    "(protocol|class)\\s+([a-zA-Z][a-zA-Z0-9_]*)\\s*(<[a-zA-Z0-9_, ]+>)?\\s*(:\\s*[a-zA-Z0-9_,<> ]+)?\\s*\\{")
val GROUP_PROTOCOL_OR_CLASS = 1
val GROUP_PROTOCOL_OR_CLASS_NAME = 2
val GROUP_PROTOCOL_OR_CLASS_GENERICS = 3
val GROUP_PROTOCOL_OR_CLASS_IMPLEMENTED = 4
val GENERIC_NAME = Pattern.compile("((?:<|:|->)\\s*)([a-zA-Z][a-zA-Z0-9_]*)(>)?")
val GROUP_GENERIC_SEPARATOR = 1
val GROUP_GENERIC_NAME = 2
val GROUP_GENERIC_ENDING = 3
val METHOD_DESCRIPTION = Pattern.compile("func\\s*([a-zA-Z][a-zA-Z0-9_]*)\\s*\\(")
val GROUP_METHOD_NAME = 1
val METHOD_OVERRIDE_DESCRIPTION = Pattern.compile("((?:internal|public)\\s+)?override\\s+func\\s*([a-zA-Z][a-zA-Z0-9_]*)\\s*\\(")
const val GROUP_OVERRIDE_INTERNAL_PUBLIC = 1
const val GROUP_OVERRIDE_METHOD_NAME = 2
val PUBLIC_FUN = Pattern.compile("public\\s+fun")
val METHOD_RETURN_GENERIC = Pattern.compile("(.*)func\\s*([a-zA-Z][a-zA-Z0-9_]*)\\s*(<.*>)?\\s*\\(((?:.|,\\n)*)\\)\\s*->\\s*([a-zA-Z][a-zA-Z0-9_]*)\\s*<(.*)>(\\s*)")
const val GROUP_METHOD_RETURN_GENERIC_HEADER = 1
const val GROUP_METHOD_RETURN_GENERIC_NAME = 2
const val GROUP_METHOD_RETURN_GENERIC_GENERICS = 3
const val GROUP_METHOD_RETURN_GENERIC_PARAMETERS = 4
const val GROUP_METHOD_RETURN_GENERIC_TYPE = 5
const val GROUP_METHOD_RETURN_GENERIC_TYPE_GENERIC = 6
const val GROUP_METHOD_RETURN_GENERIC_ENDING_SPACES = 7

object ProtocolsParser
{
    val protocols = HashMap<String, Protocol>()

    fun addFile(file: String)
    {
        var matcher = PROTOCOL.matcher(file)
        val name: String
        val generics: String?
        val implemented: String?
        val protocol: Protocol
        val matcherImplements: Matcher
        var implementsName: String
        var implementsGenerics: String?
        var index: Int
        var indexInProtocol: Int

        if (matcher.find())
        {
            name = matcher.group(GROUP_NAME)
            generics = matcher.group(GROUP_PROTOCOL_GENERICS)
            implemented = matcher.group(GROUP_IMPLEMENTED_PROTOCOLS)
            protocol = Protocol(name)
            index = 0

            generics?.substring(1, generics.length - 1)
                ?.split(',')
                ?.forEach { genericName ->
                    val trim = genericName.trim()
                    val replaceName =
                        if (trim.length > 3)
                        {
                            trim
                        }
                        else
                        {
                            "${name}_$trim"
                        }

                    protocol.generics[trim] = Generic(index, genericName, replaceName)
                    index++
                }

            if (implemented != null)
            {
                matcherImplements = PROTOCOL_IMPLEMENTS.matcher(implemented.substring(1).trim())

                while (matcherImplements.find())
                {
                    implementsName = matcherImplements.group(GROUP_NAME)
                    implementsGenerics = matcherImplements.group(GROUP_PROTOCOL_GENERICS)
                    indexInProtocol = 0
                    implementsGenerics?.substring(1, implementsGenerics.length - 1)
                        ?.split(',')
                        ?.forEach { genericName ->
                            val trim = genericName.trim()
                            protocol.generics[trim]?.fromProtocol = implementsName
                            protocol.generics[trim]?.indexInProtocol = indexInProtocol
                            indexInProtocol++
                        }
                }
            }

            this.protocols[name] = protocol

            matcher = METHOD_DESCRIPTION.matcher(file)

            while (matcher.find())
            {
                protocol.methodsName += matcher.group(GROUP_METHOD_NAME)
            }
        }
    }

    fun compile()
    {
        var changed: Boolean
        var newName: String

        do
        {
            changed = false

            this.protocols.values.forEach { protocol ->
                protocol.generics.values.forEach { generic ->
                    if (generic.fromProtocol != null)
                    {
                        newName = this.protocols[generic.fromProtocol!!]!![generic.indexInProtocol].replaceName

                        if (newName != generic.replaceName)
                        {
                            generic.replaceName = newName
                            changed = true
                        }
                    }
                }
            }
        }
        while (changed)
    }

    fun transform(file: String): String
    {
        var matcher = PROTOCOL_OR_CLASS.matcher(file)
        var transformed = StringBuilder()
        var isProtocol = false
        var string: String
        var protocolClassName: String
        var start = 0
        var end: Int
        val protocolsTypes = ArrayList<Pair<Protocol, List<String>>>()
        var matcherImplements: Matcher
        var first: Boolean
        var implementsName: String
        var implementsGenerics: String?
        var protocol: Protocol?
        var currentProtocol: Protocol
        var matcherGenericName: Matcher
        var genericName: String
        var more: Int

        while (matcher.find())
        {
            end = matcher.start()

            if (start > end)
            {
                continue
            }

            string = matcher.group(GROUP_PROTOCOL_OR_CLASS)
            isProtocol = string == "protocol"

            if (start < end)
            {
                transformed.append(file.substring(start, end))
            }

            end = matcher.end()
            transformed.append(string)
            transformed.append(' ')
            protocolClassName = matcher.group(GROUP_PROTOCOL_OR_CLASS_NAME)
            transformed.append(protocolClassName)

            if (!isProtocol)
            {
                matcher.group(GROUP_PROTOCOL_OR_CLASS_GENERICS)?.let { transformed.append(it) }
            }

            matcher.group(GROUP_PROTOCOL_OR_CLASS_IMPLEMENTED)?.let { implemented ->
                transformed.append(" : ")

                matcherImplements = PROTOCOL_IMPLEMENTS.matcher(implemented.substring(1).trim())
                first = true

                while (matcherImplements.find())
                {
                    if (!first)
                    {
                        transformed.append(", ")
                    }

                    first = false
                    implementsName = matcherImplements.group(GROUP_NAME)
                    implementsGenerics = matcherImplements.group(GROUP_PROTOCOL_GENERICS)
                    transformed.append(implementsName)
                    protocol = this.protocols[implementsName]

                    if (protocol == null)
                    {
                        implementsGenerics?.let { transformed.append(it) }
                    }
                    else
                    {
                        val list = ArrayList<String>()
                        implementsGenerics?.substring(1, implementsGenerics!!.length - 1)
                            ?.split(',')
                            ?.forEach { list += it.trim() }
                        protocolsTypes += Pair(protocol!!, list)
                    }
                }
            }

            transformed.append(" {\n")

            if (isProtocol)
            {
                currentProtocol = this.protocols[protocolClassName]!!
                currentProtocol.generics.values.forEach { generic ->
                    if (generic.fromProtocol == null)
                    {
                        transformed.append("     associatedtype ${generic.replaceName}\n")
                    }
                }

                more = end
                matcherGenericName = GENERIC_NAME.matcher(file.substring(end))

                while (matcherGenericName.find())
                {
                    start = end
                    end = matcherGenericName.start() + more

                    if (start < end)
                    {
                        transformed.append(file.substring(start, end))
                    }

                    transformed.append(matcherGenericName.group(GROUP_GENERIC_SEPARATOR))
                    genericName = matcherGenericName.group(GROUP_GENERIC_NAME)
                    transformed.append(currentProtocol.generics[genericName]?.replaceName
                                       ?: genericName)
                    matcherGenericName.group(GROUP_GENERIC_ENDING)?.let { transformed.append(it) }
                    end = matcherGenericName.end() + more
                }
            }
            else
            {
                protocolsTypes.forEach { (protocol, types) ->
                    for (index in 0 until protocol.size)
                    {
                        transformed.append("     public typealias ${protocol[index].replaceName} = ${types[index]}\n")
                    }
                }
            }

            start = end
        }

        if (start < file.length)
        {
            transformed.append(file.substring(start))
        }

        val toClear = if (isProtocol)
        {
            PUBLIC_FUN.matcher(transformed.toString()).replaceAll("fun")
        }
        else
        {
            transformed.toString()
        }

        //        if (isProtocol)
        //        {
        //            return toClear.replace("func ", "mutating func ")
        //        }

        transformed = StringBuilder()
        val length = toClear.length
        start = 0
        var methodName: String
        val protocolsTreated = TreeSet<String>()
        val methodsName = TreeSet<String>()
        val stack = Stack<Protocol>()
        var prot: Protocol

        protocolsTypes.forEach { (protocol, _) -> stack.push(protocol) }

        while (stack.isNotEmpty())
        {
            prot = stack.pop()

            if (protocolsTreated.add(prot.name))
            {
                methodsName.addAll(prot.methodsName)

                prot.generics.forEach { (_, generic) ->
                    val from = generic.fromProtocol

                    if (from != null && from !in protocolsTreated)
                    {
                        stack.push(this.protocols[from])
                    }
                }
            }
        }

        matcher = METHOD_OVERRIDE_DESCRIPTION.matcher(toClear)

        while (matcher.find())
        {
            end = matcher.start()

            if (start < end)
            {
                transformed.append(toClear.substring(start, end))
            }

            matcher.group(GROUP_OVERRIDE_INTERNAL_PUBLIC)?.let { header -> transformed.append(header) }

            methodName = matcher.group(GROUP_OVERRIDE_METHOD_NAME)

            if (methodName in methodsName)
            {
                transformed.append("func ")
            }
            else
            {
                transformed.append("override func ")
            }

            transformed.append(methodName)
            transformed.append("(")

            start = matcher.end()
        }

        if (start < length)
        {
            transformed.append(toClear.substring(start))
        }

        return transformed.toString()
    }

    fun returnProtocolMethods(file: String): String
    {
        val transformed = StringBuilder()
        val matcher = METHOD_RETURN_GENERIC.matcher(file)
        var start = 0

        while (matcher.find())
        {
            val end = matcher.start()
            transformed.append(file.substring(start, end))
            val returnGenericType: String = matcher.group(GROUP_METHOD_RETURN_GENERIC_TYPE)
            val protocol = this.protocols[returnGenericType]

            if (protocol == null)
            {
                transformed.append(matcher.group(0))
            }
            else
            {
                transformed.append(matcher.group(GROUP_METHOD_RETURN_GENERIC_HEADER))
                transformed.append("func ")
                transformed.append(matcher.group(GROUP_METHOD_RETURN_GENERIC_NAME))
                val methodGenerics = matcher.group(GROUP_METHOD_RETURN_GENERIC_GENERICS)

                if (methodGenerics == null)
                {
                    transformed.append("<")
                }
                else
                {
                    transformed.append(methodGenerics.substring(0, methodGenerics.length - 1))
                    transformed.append(", ")
                }

                transformed.append("RETURN_")
                transformed.append(protocol.name)
                transformed.append(":")
                transformed.append(protocol.name)
                transformed.append("> (")
                transformed.append(matcher.group(GROUP_METHOD_RETURN_GENERIC_PARAMETERS))
                transformed.append(") -> RETURN_")
                transformed.append(protocol.name)
                transformed.append(" where ")
                val generics: List<String> = matcher.group(GROUP_METHOD_RETURN_GENERIC_TYPE_GENERIC).split(",")

                for ((index, name) in generics.withIndex())
                {
                    if (index > 0)
                    {
                        transformed.append(" && ")
                    }

                    transformed.append("RETURN_")
                    transformed.append(protocol.name)
                    transformed.append(".")
                    transformed.append(protocol[index].replaceName)
                    transformed.append("==")
                    transformed.append(name)
                }

                transformed.append(matcher.group(GROUP_METHOD_RETURN_GENERIC_ENDING_SPACES))
            }

            start = matcher.end()
        }

        transformed.append(file.substring(start))
        return transformed.toString()
    }
}

fun parseProtocolsInFiles(files: List<File>)
{
    files.forEach { ProtocolsParser.addFile(it.readText()) }
    ProtocolsParser.compile()
    var transformed: String

    files.forEach { file ->
        transformed = ProtocolsParser.transform(file.readText())
        transformed = ProtocolsParser.returnProtocolMethods(transformed)
        file.writeText(transformed)
    }
}