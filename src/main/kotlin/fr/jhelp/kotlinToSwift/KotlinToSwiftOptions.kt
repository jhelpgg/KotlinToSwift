package fr.jhelp.kotlinToSwift

const val HEADER_OPTION = "-header"
const val DISABLE_AUTOMATIC_PUBLIC = "-disableAutomaticPublic"
const val REMOVE_OPEN = "-removeOpen"

object KotlinToSwiftOptions
{
    var headerClass = ""
        private set
    var automaticPublic = true
        private set
    var removeOpen = false
        private set

    /**
     * Extract and  options set from array
     * @return Parameters that not options
     */
    fun extractOptions(parameters: Array<String>): List<String>
    {
        val other = ArrayList<String>()
        var index = 0

        while (index < parameters.size)
        {
            val parameter = parameters[index]

            when (parameter)
            {
                HEADER_OPTION ->
                {
                    index++

                    if (index < parameters.size)
                    {
                        this.headerClass = parameters[index]
                    }
                    else
                    {
                        throw IllegalArgumentException("$HEADER_OPTION must be follow be the header")
                    }
                }
                DISABLE_AUTOMATIC_PUBLIC ->
                    this.automaticPublic = false
                REMOVE_OPEN ->
                    this.removeOpen = true
                else                     ->
                    other.add(parameter)
            }

            index++
        }

        return other
    }
}