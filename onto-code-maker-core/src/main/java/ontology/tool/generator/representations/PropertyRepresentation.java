package ontology.tool.generator.representations;

public class PropertyRepresentation extends EntityRepresentation {

    private Boolean isPrivate = true;
    private PROPERTY_TYPE propertyType;
    private String type;
    private String value;

    public enum PROPERTY_TYPE {
        FUNCTIONAL,
        DATATYPE
    }

    public PropertyRepresentation(String namespace, String name) {
        super(namespace, name);
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
        if (this.value != null){
            return true;
        }
        return false;
    }

    public void setPropertyType(PROPERTY_TYPE type){
        this.propertyType = type;
    }

    public PROPERTY_TYPE getPropertyType(){
        return propertyType;
    }

}
