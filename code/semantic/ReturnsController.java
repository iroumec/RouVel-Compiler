package semantic;

/**
 * Clase encargada de controlar que exista una cantidad de retornos apropiada en
 * el código.
 * 
 * Separa la lógica de la gramática.
 */
public class ReturnsController {

    private int returnsFound;
    private int functionLevel;
    private int returnsNeeded;
    private int selectionDepth;
    private boolean isThereReturn;

    public ReturnsController() {
        this.returnsFound = 0;
        this.functionLevel = 0;
        this.returnsNeeded = 0;
        this.selectionDepth = 0;
        this.isThereReturn = false;
    }

    public void notifyStartOfFunctionDeclaration() {
        this.functionLevel++;
        this.returnsNeeded++;
    }

    public void notifyEndOfFunctionDeclaration() {
        this.functionLevel--;
        this.returnsFound--;
        this.returnsNeeded--;
        this.isThereReturn = false;
    }

    public void notifyEmptyElse() {
        // Se decrementa la cantidad de retornos que se requieren si el if está solo.
        this.returnsNeeded--;

        // Se decrementa la cantidad de returns hallados.
        // TODO: REVISAR QUÉ PASA SI DENTRO DEL IF HAY VARIOS RETURNS.
        this.returnsFound--;
    }

    public void notifySelectionStart() {
        this.returnsNeeded++;
        this.selectionDepth++;
    }

    public void notifySelectionEnd() {

        // Se está saliendo del if más externo.
        if (this.selectionDepth == 1) {
            if (this.returnsNeeded == this.returnsFound) {
                this.isThereReturn = true;
            }
        }

        this.selectionDepth--;
    }

    public void notifyReturn() {

        this.returnsFound++;

        if (this.selectionDepth == 0) {
            this.isThereReturn = true;
        }
    }

    public boolean insideFunction() {
        return this.functionLevel > 0;
    }

    public boolean isThereReturn() {
        return this.isThereReturn;
    }

}
