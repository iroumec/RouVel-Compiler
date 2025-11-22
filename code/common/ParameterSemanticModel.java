package common;

public enum ParameterSemanticModel {

    CV(SymbolCategory.CV_PARAMETER),
    CVR(SymbolCategory.CVR_PARAMETER);

    // --------------------------------------------------------------------------------------------

    private SymbolCategory symbolCategory;

    // --------------------------------------------------------------------------------------------

    private ParameterSemanticModel(SymbolCategory symbolCategory) {

        this.symbolCategory = symbolCategory;
    }

    // --------------------------------------------------------------------------------------------

    SymbolCategory getSymbolCategory() {

        return this.symbolCategory;
    }
}
