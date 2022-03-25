package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassRepresentation extends DefaultClassRepresentation{

    public enum CLASS_TYPE {
        ABSTRACT("Abstract"),
        NORMAL("Normal");

        private final String name;

        /**
         * @param name
         */
        private CLASS_TYPE(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private CLASS_TYPE type;

    private EquivalentClassRepresentation equivalentClass;

    private List<PropertyRepresentation> properties = new ArrayList<>();

    private boolean hasInterface;

    private boolean hasSuperAbstractClass;

    private List<ClassRepresentation> unionOf = new ArrayList<>();
    private List<ClassRepresentation> intersectionOf = new ArrayList<>();
    private ClassRepresentation complementOf;

    public ClassRepresentation(String namespace, String name,CLASS_TYPE type){
        super(namespace,name);
        this.type = type;
    }

    public ClassRepresentation(String name,CLASS_TYPE type){
        super(name);
        this.type = type;
    }

    public ClassRepresentation(CLASS_TYPE type){
        super();
        this.type = type;
    }

    public CLASS_TYPE getClassType(){
        return type;
    }

    public void addProperties(PropertyRepresentation property){
        this.properties.add(property);
    }

    public List<PropertyRepresentation> getProperties() {
        return properties;
    }

    public boolean isHasInterface(){
        return hasInterface;
    }

    public void setHasInterface(boolean value){
        this.hasInterface = value;
    }

    public boolean isHasSuperAbstractClass(){
        return hasSuperAbstractClass;
    }

    public void setHasSuperAbstractClass(boolean value){
        this.hasSuperAbstractClass = value;
    }


    public boolean hasEquivalentClassInterface() {
        return equivalentClass != null;
    }

    public void setEquivalentClass(EquivalentClassRepresentation equivalentClass){
        this.equivalentClass = equivalentClass;
    }


    public EquivalentClassRepresentation getEquivalentClass() {
        return equivalentClass;
    }

    public void addUnionOf(ClassRepresentation unionOf){
        this.unionOf.add(unionOf);
    }

    public List<ClassRepresentation> getUnionOf(){
        return unionOf;
    }

    public boolean isUnionOf(){
        return !unionOf.isEmpty();
    }

    public void addIntersectionOf(ClassRepresentation intersectionOf){
        this.intersectionOf.add(intersectionOf);
    }

    public List<ClassRepresentation> getIntersectionOf(){
        return intersectionOf;
    }

    public boolean isIntersectionOf(){
        return !intersectionOf.isEmpty();
    }

    public void setComplementOf(ClassRepresentation complementOf){
        this.complementOf = complementOf;
    }

    public ClassRepresentation getComplementOf(){
        return complementOf;
    }

    public boolean isComplementOf(){
        return complementOf != null;
    }

    abstract public Resource getResourceValue();

}
