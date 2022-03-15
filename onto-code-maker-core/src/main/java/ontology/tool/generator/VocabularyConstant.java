package ontology.tool.generator;

public class VocabularyConstant {
    private Boolean isPrivate = false;
    private String type;
    private String name;
    private String value;
    private String constantOf;
    private String objectName;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isValue(){
        return this.value != null;
    }

    public String getConstantOf(){
        return constantOf;
    }

    public void setConstantOf(String constantOf){
        this.constantOf = constantOf;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }

}
