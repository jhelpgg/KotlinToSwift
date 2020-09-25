package fr.jhelp.kotlinToSwift.protocol

data class Protocol(val name: String)
{
    val generics = HashMap<String, Generic>()
    val methodsName = ArrayList<String>()
    val size get() = this.generics.size

    operator fun get(index: Int): Generic
    {
        for (generic in this.generics.values)
        {
            if (generic.index == index)
            {
                return generic
            }
        }

        throw IndexOutOfBoundsException("$index not in [0, ${this.size}[")
    }
}