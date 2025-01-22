package utils.autoUnitTestUtil.testResult;

public class ParameterData {
    private String name;
    private String type;
    private String value;

    public ParameterData() {
    }

    public ParameterData(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + " " + name + " = " + value;
    }
}

