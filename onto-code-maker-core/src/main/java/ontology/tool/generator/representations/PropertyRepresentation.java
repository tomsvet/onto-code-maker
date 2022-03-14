package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.IRI;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class PropertyRepresentation extends EntityRepresentation {

    private String className;
    private Boolean isPrivate = true;
    private PROPERTY_TYPE type;
    private String rangeDatatype;
    private IRI rangeIRI;
    private String value;
    private ClassRepresentation rangeClass;
    private boolean isFunctional = false;
    private List<PropertyRepresentation> subProperties = new ArrayList<>();
    private List<PropertyRepresentation> superProperties = new ArrayList<>();

    private PropertyRepresentation isEquivalentTo;
    private List<PropertyRepresentation> equivalentProperties = new ArrayList<>();

    public enum PROPERTY_TYPE {
        OBJECT,
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

    public void setRangeIRI(IRI rangeIRI) {
        this.rangeIRI = rangeIRI;
    }

    public IRI getRangeIRI() {
        return rangeIRI;
    }

    public void setRangeDatatype(String rangeDatatype) {
        this.rangeDatatype = rangeDatatype;
    }

    public String getRangeDatatype() {
        return rangeDatatype;
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

    public void setType(PROPERTY_TYPE type){
        this.type = type;
    }

    public PROPERTY_TYPE getType(){
        return type;
    }

    public void setRangeClass(ClassRepresentation rangeClass) {
        this.rangeClass = rangeClass;
    }

    public ClassRepresentation getRangeClass() {
        return rangeClass;
    }

    public String getConstantName(){return getClassName().toUpperCase() + "_" + this.getName().toUpperCase()  + "_PROPERTY_IRI";}

    public String getClassName(){
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setIsFunctional(boolean isFunctional){
        this.isFunctional = isFunctional;
    }

    public boolean isFunctional() {
        return isFunctional;
    }

    public void setIsEquivalentTo(PropertyRepresentation property){
        this.isEquivalentTo = property;
    }

    public PropertyRepresentation getIsEquivalentTo(){
        return this.isEquivalentTo;
    }

    public void addSubProperty(PropertyRepresentation subProperty) {
        this.subProperties.add(subProperty);
    }

    public List<PropertyRepresentation> getSubProperties(){
        return subProperties;
    }

    public boolean isSubProperty(){
        return !superProperties.isEmpty();
    }

    public void addSuperProperty(PropertyRepresentation superProperty) {
        this.superProperties.add(superProperty);
    }

    public List<PropertyRepresentation> getSuperProperties(){
        return superProperties;
    }

    public void addEquivalentProperty(PropertyRepresentation equivalentProperty){
        this.equivalentProperties.add(equivalentProperty);
    }

    public List<PropertyRepresentation> getEquivalentProperties(){
        return equivalentProperties;
    }
}
