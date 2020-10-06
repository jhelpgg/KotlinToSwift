package fr.jhelp.kotlinToSwift.postTreatment

import fr.jhelp.kotlinToSwift.endCurlyIndex
import java.util.regex.Pattern

private val TEST_CASE_CLASS_PATTERN =
    Pattern.compile("@TestCaseClass\\s*\\(\\s*\"(.*)\"\\s*\\)(\\s*(?:(?:public|internal)\\s+)?class\\s+[a-zA-Z0-9_]+)(\\s*\\{)")
private const val REFERENCE_NAME_GROUP = 1
private const val DECLARATION_CLASS_GROUP = 2
private const val OPEN_CURLY_GROUP = 3
private val BEFORE_PATTERN =
    Pattern.compile("@Before\\s+((?:(?:public|internal)\\s+)?fun(?:c)?\\s+)[a-zA-Z0-9_]+(\\s*\\(.*\\)\\s*\\{)")
private const val BEFORE_DECLARATION_FUNCTION_GROUP = 1
private const val BEFORE_FUNCTION_PARAMETERS_GROUP = 2
private val AFTER_PATTERN =
    Pattern.compile("@After\\s+((?:(?:public|internal)\\s+)?fun(?:c)?\\s+)[a-zA-Z0-9_]+(\\s*\\(.*\\)\\s*\\{)")
private const val AFTER_DECLARATION_FUNCTION_GROUP = 1
private const val AFTER_FUNCTION_PARAMETERS_GROUP = 2
private val FUNCTION_TEST_PATTERN =
    Pattern.compile("@Test\\s+((?:(?:public|internal)\\s+)?fun(?:c)?\\s+)([a-zA-Z0-9_]+\\s*\\((?:\\s|.)*\\)\\s*\\{)")
private const val TEST_DECLARATION_FUNCTION_GROUP = 1
private const val TEST_FUNCTION_DECLARATION_GROUP = 2
private val ASSERTIONS_REPLACEMENT = listOf(
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?fail\\s*\\(\\s*(\".*\")\\s*\\)"),
         "XCTFail($1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?fail\\s*\\(\\s*\\)"),
         "XCTFail(\"Failure !\")"),

    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertTrue\\s*\\(\\s*(\".*\")\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertTrue($2, $1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertTrue\\s*\\(\\s*(.*)\\s*\\)"),
         "XCTAssertTrue($1, \"Is not true\")"),

    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertFalse\\s*\\(\\s*(\".*\")\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertFalse($2, $1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertFalse\\s*\\(\\s*(.*)\\s*\\)"),
         "XCTAssertFalse($1, \"Is not false\")"),

    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertNull\\s*\\(\\s*(\".*\")\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertNil($2, $1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertNull\\s*\\(\\s*(.*)\\s*\\)"),
         "XCTAssertNil($1, \"Is not nil\")"),

    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertNotNull\\s*\\(\\s*(\".*\")\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertNotNil($2, $1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertNotNull\\s*\\(\\s*(.*)\\s*\\)"),
         "XCTAssertNotNil($1, \"Is nil\")"),

    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertEquals\\s*\\(\\s*(\".*\")\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertEqual($2, $3, $4, $1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertEquals\\s*\\(\\s*(\".*\")\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertEqual($2, $3, $1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertEquals\\s*\\(\\s*(.*)\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertEqual($1, $2, $3, \"Objects are not equivalents\")"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertEquals\\s*\\(\\s*(.*)\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertEqual($1, $2, \"Objects are not equals\")"),

    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertNotEquals\\s*\\(\\s*(\".*\")\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertNotEqual($2, $3, $4, $1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertNotEquals\\s*\\(\\s*(\".*\")\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertNotEqual($2, $3, $1)"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertNotEquals\\s*\\(\\s*(.*)\\s*,\\s*(.*)\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertNotEqual($1, $2, $3, \"Objects are equivalents\")"),
    Pair(Pattern.compile("(?:Assert(?:ions)?\\.)?assertNotEquals\\s*\\(\\s*(.*)\\s*,\\s*(.*)\\s*\\)"),
         "XCTAssertNotEqual($1, $2, \"Objects are equals\")"),
)

fun parseTestFile(file: String): String
{
    val matcher = TEST_CASE_CLASS_PATTERN.matcher(file)

    if (!matcher.find())
    {
        return file
    }

    val result = StringBuilder()
    result.append(file.substring(0, matcher.start()))
    result.append("\nimport XCTest")
    result.append("\n@testable import ")
    result.append(matcher.group(REFERENCE_NAME_GROUP))
    result.append(matcher.group(DECLARATION_CLASS_GROUP))
    result.append(" : XCTestCase")
    result.append(matcher.group(OPEN_CURLY_GROUP))
    var left = file.substring(matcher.end())
    left = beforeToSetup(left)
    left = afterToTearDown(left)
    left = functionsTest(left)

    for ((pattern, replacement) in ASSERTIONS_REPLACEMENT)
    {
        left = pattern.matcher(left).replaceAll(replacement)
    }

    result.append(left)

    return result.toString()
}

private fun beforeToSetup(source: String): String
{
    val matcher = BEFORE_PATTERN.matcher(source)

    if (!matcher.find())
    {
        return source
    }

    val destination = StringBuilder()
    destination.append(source.substring(0, matcher.start()))
    destination.append("override ")
    destination.append(matcher.group(BEFORE_DECLARATION_FUNCTION_GROUP))
    destination.append("setUp")
    destination.append(matcher.group(BEFORE_FUNCTION_PARAMETERS_GROUP))
    destination.append("\n          super.setUp()\n")
    destination.append(source.substring(matcher.end()))

    return destination.toString()
}

private fun afterToTearDown(source: String): String
{
    val matcher = AFTER_PATTERN.matcher(source)

    if (!matcher.find())
    {
        return source
    }

    val destination = StringBuilder()
    destination.append(source.substring(0, matcher.start()))
    destination.append("override ")
    destination.append(matcher.group(AFTER_DECLARATION_FUNCTION_GROUP))
    destination.append("tearDown")
    destination.append(matcher.group(AFTER_FUNCTION_PARAMETERS_GROUP))
    val index = endCurlyIndex(source, matcher.end())
    destination.append(source.substring(matcher.end(), index - 1))
    destination.append("\n          super.tearDown()\n     }")
    destination.append(source.substring(index + 1))

    return destination.toString()
}

private fun functionsTest(source: String): String
{
    val destination = StringBuilder()
    val matcher = FUNCTION_TEST_PATTERN.matcher(source)
    var start = 0
    var end: Int

    while (matcher.find())
    {
        if (start > matcher.start())
        {
            continue
        }

        end = endCurlyIndex(source, matcher.end())
        destination.append(source.substring(start, matcher.start()))
        destination.append(matcher.group(TEST_DECLARATION_FUNCTION_GROUP))
        destination.append("test_")
        destination.append(matcher.group(TEST_FUNCTION_DECLARATION_GROUP))
        destination.append(source.substring(matcher.end(), end))

        start = end
    }

    destination.append(source.substring(start))
    return destination.toString()
}
