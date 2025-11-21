package semantic;

import common.Monitor;

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

    /**
     * Número par para selecciones.
     * Número impar para alternativas.
     */
    private int selectionDepth;
    private boolean isThereReturn;

    /**
     * -1 si no hay retorno.
     * Si tiene otro número, indica la
     * profundidad en la que aparece.
     */
    private int depthOfReturn;

    private static final boolean debug = true;

    // --------------------------------------------------------------------------------------------

    public ReturnsController() {
        this.returnsFound = 0;
        this.functionLevel = 0;
        this.returnsNeeded = 0;
        this.selectionDepth = 0;
        this.depthOfReturn = -1;
        this.isThereReturn = false;

        if (debug) debugPrint("ReturnsController");
    }

    // --------------------------------------------------------------------------------------------

    public void notifyStartOfFunctionDeclaration() {
        this.functionLevel++;
        this.returnsNeeded++;
        this.notifySectionStart();

        if (debug) debugPrint("notifyStartOfFunctionDeclaration");
    }

    // --------------------------------------------------------------------------------------------

    public void notifyEndOfFunctionDeclaration() {
        this.functionLevel--;
        this.returnsFound--;
        this.returnsNeeded--;
        this.isThereReturn = false;
        this.notifySectionEnd();

        if (debug) debugPrint("notifyEndOfFunctionDeclaration");
    }

    // --------------------------------------------------------------------------------------------

    public void notifyEmptyElse() {
        // Se decrementa la cantidad de retornos que se requieren si el if está solo.
        this.returnsNeeded--;

        // Se decrementa la cantidad de returns hallados.
        // TODO: REVISAR QUÉ PASA SI DENTRO DEL IF HAY VARIOS RETURNS.
        this.returnsFound--;

        if (debug) debugPrint("notifyEmptyElse");
    }

    // --------------------------------------------------------------------------------------------

    public void notifySelectionStart() {
        this.returnsNeeded++;

        // If dentro del if.
        if (this.selectionDepth % 2 != 0) {
            this.selectionDepth += 2;
        } else {
            this.selectionDepth++;
        }

        this.notifySectionStart();

        if (debug) debugPrint("notifySelectionStart");
    }

    // --------------------------------------------------------------------------------------------

    public void notifyAlternativeStart() {
        //this.returnsNeeded++;
        this.selectionDepth++;
        this.depthOfReturn = -1;
        this.notifySectionStart();

        if (debug)
            debugPrint("notifyAlternativeStart");
    }

    // --------------------------------------------------------------------------------------------

    public void notifyAlternativeEnd() {

        this.notifySectionEnd();

        this.selectionDepth--;

        if (debug)
            debugPrint("notifyAlternativeEnd");
    }

    // --------------------------------------------------------------------------------------------

    public void notifySelectionEnd() {

        // Se está saliendo del if más externo.
        if (this.selectionDepth == 1) {
            if (this.returnsNeeded == this.returnsFound) {
                this.isThereReturn = true;
            } else {
                if (!this.isThereReturn) {
                    this.returnsNeeded = 1;
                    this.returnsFound = 0;
                }
            }
        }

        this.notifySectionEnd();

        this.selectionDepth--;

        if (debug) debugPrint("notifySelectionEnd");
    }

    // --------------------------------------------------------------------------------------------

    private void notifySectionStart() {
        // Empty
        /*
        if (this.depthOfReturn == -1) {
            this.depthOfReturn = selectionDepth;
        }
        */
    }

    // --------------------------------------------------------------------------------------------


    private void notifySectionEnd() {
        if (this.selectionDepth == this.depthOfReturn) {
            this.depthOfReturn = -1;
        }

        if (debug)
            debugPrint("notifySectionEnd");
    }

    // --------------------------------------------------------------------------------------------

    public boolean notifyReturn() {

        if (!this.isThereReturnInSection()) {
            this.returnsFound++;
            this.depthOfReturn = this.selectionDepth;
            
            if (this.selectionDepth == 0) {
                this.isThereReturn = true;
            }

        }
        
        if (debug) debugPrint("notifyReturn");

        return !this.isThereReturnInSection();
    }

    // --------------------------------------------------------------------------------------------

    public boolean isThereReturnInSection() {

        return this.depthOfReturn != -1;
    }

    // --------------------------------------------------------------------------------------------

    public boolean insideFunction() {
        return this.functionLevel > 0;
    }

    // --------------------------------------------------------------------------------------------

    public boolean isThereReturnInDeclaration() {
        return this.isThereReturn;
    }

    // --------------------------------------------------------------------------------------------

    public void debugPrint(String message) {
        System.out.println(message+
            "\nreturnsFound: "+returnsFound+
            "\nfunctionLevel: "+functionLevel+
            "\nreturnsNeeded: "+returnsNeeded+
            "\nselectionDepth: "+selectionDepth+
            "\nisThereReturn: "+isThereReturn+
            "\ndepthOfReturn: "+depthOfReturn
        );
    }

    // --------------------------------------------------------------------------------------------

    private static void notifyError(String errorMessage) {

        Monitor monitor = Monitor.getInstance();

        monitor.addError(String.format(
                "ERROR SEMÁNTICO: Línea %d: %s",
                monitor.getLineNumber(), errorMessage));
    }
}
