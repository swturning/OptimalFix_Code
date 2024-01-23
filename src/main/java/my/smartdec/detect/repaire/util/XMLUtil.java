package my.smartdec.detect.repaire.util;

public class XMLUtil {
    public static String suc003=".//expressionStatement[ancestor::contractDefinition]\n" +
            "               [\n" +
            "               expression[text()[1] = \"=\"]\n" +
            "               [\n" +
            "               expression[2]//functionCall/functionName//identifier[text()[1] = \"staticcall\" or text()[1] = \"call\" or text()[1] = \"delegatecall\"]\n" +
            "               ]\n" +
            "               [\n" +
            "               not(\n" +
            "               expression[1]//variableDeclaration[typeName/elementaryTypeName[text()[1] = \"bool\"]]/identifier =\n" +
            "               ancestor::block//condition//expression//identifier\n" +
            "               or\n" +
            "               expression[1]//variableDeclaration[typeName/elementaryTypeName[text()[1] = \"bool\"]]/identifier =\n" +
            "               ancestor::block//functionCall[functionName[identifier='require' or identifier='assert' or identifier='revert']]/callArguments//identifier\n" +
            "               or\n" +
            "               expression[1]//variableDeclaration[typeName/elementaryTypeName[text()[1] = \"bool\"]]/identifier =\n" +
            "               ancestor::block//expressionStatement//expression[expression/primaryExpression[identifier='require' or identifier='assert' or identifier='revert']]/callArguments//identifier\n" +
            "               or\n" +
            "               expression[1]//primaryExpression/identifier =\n" +
            "               ancestor::block//variableDeclarationStatement[variableDeclaration/typeName/elementaryTypeName[text()[1] = \"bool\"]]//identifier\n" +
            "               or\n" +
            "               expression[1]//primaryExpression/identifier =\n" +
            "               ancestor::contractDefinition//stateVariableDeclaration[typeName/elementaryTypeName[text()[1] = \"bool\"]]//identifier\n" +
            "               )\n" +
            "               ]\n" +
            "               ]";



    public static String ree001 =".//functionCall [functionName/identifier[text()[1]=\"call\"]][value]\n" +
            "               [ not(gas) or gas/expression//identifier or gas/expression//numberLiteral[decimalNumber >= 10000] ]\n" +
            "               [not(ancestor::condition)]\n" +
            "               [ ancestor::statement/following-sibling::statement//expression[lvalueOperator]/expression[1]//identifier[text()[1]=(ancestor::contractDefinition//stateVariableDeclaration[typeName[not(elementaryTypeName = \"bool\")]]/identifier)]\n" +
            "               or ancestor::statement/following-sibling::statement//expression[text()[1]=\"=\"]/expression[1]//identifier[text()[1]=(ancestor::contractDefinition//stateVariableDeclaration[typeName[not(elementaryTypeName = \"bool\")]]/identifier)]\n" +
            "               or ancestor::statement/following-sibling::statement//expression[twoPlusMinusOperator]/expression[1]//identifier[text()[1]=(ancestor::contractDefinition//stateVariableDeclaration[typeName[not(elementaryTypeName = \"bool\")]]/identifier)]\n" +
            "               or value/expression/primaryExpression/identifier =ancestor::statement/following-sibling::statement//expression/primaryExpression/identifier\n" +
            "               ]";
}
