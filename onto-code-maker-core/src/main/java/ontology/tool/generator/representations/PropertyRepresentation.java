package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Values;

import java.util.ArrayList;
import java.util.List;

public class PropertyRepresentation extends EntityRepresentation {

    public static String  PROPERTY_CONSTANT_SUFFIX= "_PROPERTY_IRI";


    private String className;
    private Boolean isPrivate = true;
    private PROPERTY_TYPE type;
    private String rangeDatatype;
    private Resource rangeResource;
    private String value;
    private ClassRepresentation rangeClass;
    private boolean isFunctional = false;
    private PropertyRepresentation inverseFunctionalOf;
    private PropertyRepresentation inverseFunctionalTo;
    private PropertyRepresentation inverseOf;
    private List<PropertyRepresentation> inverseTo = new ArrayList<>();
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

    public IRI getValueIRI(){
        return Values.iri(super.getNamespace() + super.getName());
    }

    public String getStringIRI(){
        return super.getNamespace() + super.getName();
    }

    public void setIsPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setRangeResource(Resource rangeResource) {
        this.rangeResource = rangeResource;
    }

    public Resource getRangeResource() {
        return rangeResource;
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
        return this.value != null;
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

    public String getConstantName(){return getClassName().toUpperCase() + "_" + this.getName().toUpperCase()  + PROPERTY_CONSTANT_SUFFIX;}

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

    public boolean isSuperProperty(){
        return !subProperties.isEmpty();
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

    public void addAllEquivalentProperty(List<PropertyRepresentation> equivalentProperty){
        this.equivalentProperties.addAll(equivalentProperty);
    }

    public List<PropertyRepresentation> getEquivalentProperties(){
        return equivalentProperties;
    }

    public boolean hasEquivalentProperties(){
        return !equivalentProperties.isEmpty();
    }



    public void setInverseFunctionalOf(PropertyRepresentation property){
        this.inverseFunctionalOf = property;
    }

    public PropertyRepresentation getInverseFunctionalOf(){
        return this.inverseFunctionalOf;
    }

    public boolean isInverseFunctionalOf(){
        return this.inverseFunctionalOf != null;
    }

    public void setInverseFunctionalTo(PropertyRepresentation property){
        this.inverseFunctionalTo = property;
    }

    public PropertyRepresentation getInverseFunctionalTo(){
        return this.inverseFunctionalTo;
    }

    public boolean isInverseFunctionalTo(){
        return this.inverseFunctionalTo!= null;
    }

    public void setInverseOf(PropertyRepresentation property){
        this.inverseOf = property;
    }

    public PropertyRepresentation getInverseOf(){
        return this.inverseOf;
    }

    public boolean isInverseOf(){
        return this.inverseOf != null;
    }

    public void addInverseTo(PropertyRepresentation property){
        this.inverseTo.add(property);
    }

    public List<PropertyRepresentation> getInverseTo(){
        return this.inverseTo;
    }

    public boolean isInverseTo(){
        return !this.inverseTo.isEmpty();
    }



}
