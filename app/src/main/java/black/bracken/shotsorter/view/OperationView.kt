package black.bracken.shotsorter.view

/**
 * @author BlackBracken
 */
interface OperationView {

    fun toggleActivateIsChecked(isTurnedOn: Boolean)

    fun toggleStartupIsChecked(isTurnedOn: Boolean)

    fun requestPermission()

}