package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassRepresentation extends DefaultClassRepresentation{

    public enum CLASS_TYPE {
        ABSTRACT,
        NORMAL
    }

    private CLASS_TYPE type;

    private EquivalentClassRepresentation equivalentClass;

    private List<PropertyRepresentation> properties = new ArrayList<>();

    private boolean hasInterface;

    private boolean hasSuperAbstractClass;

    private List<ClassRepresentation> unionOf = new ArrayList<>();

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

    abstract public Resource getResourceValue();

}
