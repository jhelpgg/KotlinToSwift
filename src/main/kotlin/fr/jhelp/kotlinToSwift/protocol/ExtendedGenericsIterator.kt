package fr.jhelp.kotlinToSwift.protocol

class ExtendedGenericsIterator(private val extendsGenericsList: String) : Iterator<ExtendedGenerics>
{
    private val extendsGenerics = this.extendsGenericsList.toCharArray()
    private val length = this.extendsGenerics.size
    private var index = 0

    override fun hasNext(): Boolean = this.index < this.length

    override fun next(): ExtendedGenerics
    {
        var genericDeep = 0
        var start = this.index
        var genericStart = -1

        while (this.index < this.length)
        {
            when (this.extendsGenerics[this.index])
            {
                ' ', '\t', '\n' ->
                    if (start == this.index)
                    {
                        start++
                    }
                '<'             ->
                {
                    if (genericDeep == 0)
                    {
                        genericStart = this.index
                    }

                    genericDeep++
                }
                '>'             ->
                {
                    genericDeep--

                    if (genericDeep == 0)
                    {
                        this.index++
                        return ExtendedGenerics(this.extendsGenericsList.substring(start, genericStart).trim(), this.extendsGenericsList.substring(genericStart, this.index).trim())
                    }
                }
                ','             ->
                    if (genericDeep == 0)
                    {
                        if (start == this.index)
                        {
                            start++
                        }
                        else
                        {
                            this.index++

                            if (genericStart >= 0)
                            {
                                return ExtendedGenerics(this.extendsGenericsList.substring(start, genericStart).trim(), this.extendsGenericsList.substring(genericStart, this.index - 1).trim())
                            }

                            return ExtendedGenerics(this.extendsGenericsList.substring(start, this.index - 1).trim(), null)
                        }
                    }
            }

            this.index++
        }

        if (genericStart >= 0)
        {
            return ExtendedGenerics(this.extendsGenericsList.substring(start, genericStart).trim(), this.extendsGenericsList.substring(genericStart).trim())
        }

        return ExtendedGenerics(this.extendsGenericsList.substring(start).trim(), null)
    }
}