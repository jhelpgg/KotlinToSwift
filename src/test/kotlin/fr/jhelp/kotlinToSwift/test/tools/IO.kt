package fr.jhelp.kotlinToSwift.test.tools

import java.io.File
import java.util.Stack

val outsideDirectory = File(File("").absolutePath)

fun File.createDirectory(): Boolean
{
    if (this.exists())
    {
        return this.isDirectory
    }

    return try
    {
        this.mkdirs()
    }
    catch (_: Exception)
    {
        false
    }
}

fun File.createFile(): Boolean
{
    if (this.exists())
    {
        return this.isFile
    }

    if (!this.parentFile.createDirectory())
    {
        return false
    }

    return try
    {
        this.createNewFile()
    }
    catch (_: Exception)
    {
        false
    }
}

fun File.deleteComplete(): Boolean
{
    if (!this.exists())
    {
        return true
    }

    val stack = Stack<File>()
    stack.push(this)

    while (stack.isNotEmpty())
    {
        val file = stack.pop()

        if (file.isDirectory)
        {
            val children = this.listFiles()

            if (children == null || children.isEmpty())
            {
                try
                {
                    if (!file.delete())
                    {
                        return false
                    }
                }
                catch (_: Exception)
                {
                    return false
                }
            }
            else
            {
                stack.push(file)

                for (child in children)
                {
                    stack.push(child)
                }
            }
        }
        else
        {
            try
            {
                if (!file.delete())
                {
                    return false
                }
            }
            catch (_: Exception)
            {
                return false
            }
        }
    }

    return true
}
