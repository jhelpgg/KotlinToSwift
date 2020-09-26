package fr.jhelp.kotlinToSwift

import java.io.File
import java.util.regex.Pattern

private val whenKeyWordPattern = Pattern.compile("when\\s*(?:\\(\\s*([a-zA-Z0-9_]+)\\s*\\))?(\\s*\\{)")
private const val GROUP_PARAMETER_NAME = 1
private const val GROUP_END_CURLY = 2
private val casePattern = Pattern.compile("([ \t]]*)([^ \t].*)->((\\s*)\\{)?")
private const val GROUP_CASE_SPACES = 1
private const val GROUP_CASE_CONDITION = 2
private const val GROUP_CASE_CURLY = 3
private const val GROUP_CASE_CURLY_SPACE = 4
private val elsePattern = Pattern.compile("\\s*else\\s*")

fun parseWhenInFile(file: String): String
{
    val transformed = StringBuilder()
    val matcher = whenKeyWordPattern.matcher(file)
    var first = 0
    var before: Int
    var start: Int
    var end = 0
    var parameter: String?
    var curlyEnd: String

    while (matcher.find())
    {
        before = matcher.start()
        start = matcher.end()
        end = endCurlyIndex(file, start)
        transformed.append(file.substring(first, before))
        parameter = matcher.group(GROUP_PARAMETER_NAME)
        curlyEnd = matcher.group(GROUP_END_CURLY)

        if (parameter == null)
        {
            replaceWhenWithoutParameter(file, start, end, curlyEnd, transformed)
        }
        else
        {
            replaceWhenWithParameter(file, start, end, parameter, curlyEnd, transformed)
        }

        first = end
    }

    transformed.append(file.substring(end))
    return transformed.toString()
}

fun replaceWhenWithParameter(file: String,
                             start: Int,
                             end: Int,
                             parameter: String,
                             curlyEnd: String,
                             transformed: StringBuilder)
{
    val part = file.substring(start, end)
    transformed.append("switch ")
    transformed.append(parameter)
    transformed.append(curlyEnd)
    val matcher = casePattern.matcher(part)
    var before = 0
    var caseCondition: String

    while (matcher.find())
    {
        transformed.append(part.substring(before, matcher.start()))
        caseCondition = matcher.group(GROUP_CASE_CONDITION)
        transformed.append(matcher.group(GROUP_CASE_SPACES))

        if (elsePattern.matcher(caseCondition).matches())
        {
            transformed.append(caseCondition.replace("else", "default"))
        }
        else
        {
            transformed.append("case ")
            transformed.append(caseCondition)
        }

        transformed.append(" : ")

        if (matcher.group(GROUP_CASE_CURLY) != null)
        {
            transformed.append(matcher.group(GROUP_CASE_CURLY_SPACE))
            transformed.append("do {")
        }

        before = matcher.end()
    }

    transformed.append(part.substring(before))
}

fun replaceWhenWithoutParameter(file: String, start: Int, end: Int, curlyEnd: String, transformed: StringBuilder)
{
    val part = file.substring(start, end - 1)
    val matcher = casePattern.matcher(part)
    var before = 0
    var caseCondition: String
    var notFirst = false
    var closePreviousCurly = ""

    while (matcher.find())
    {
        transformed.append(part.substring(before, matcher.start()))
        transformed.append(closePreviousCurly)
        caseCondition = matcher.group(GROUP_CASE_CONDITION)
        transformed.append(matcher.group(GROUP_CASE_SPACES))

        if (notFirst)
        {
            transformed.append("else ")
        }

        if (!elsePattern.matcher(caseCondition).matches())
        {
            transformed.append("if ")
            transformed.append(caseCondition.trim())
            transformed.append(" ")
        }

        transformed.append("{")

        closePreviousCurly =
            if (matcher.group(GROUP_CASE_CURLY) == null)
            {
                "}"
            }
            else
            {
                ""
            }

        notFirst = true
        before = matcher.end()
    }

    transformed.append(part.substring(before))
    transformed.append(closePreviousCurly)
}
