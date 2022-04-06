package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.Resource;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassRepresentation extends DefaultClassRepresentation{


    private EquivalentClassRepresentation equivalentClass;

    private List<PropertyRepresentation> properties = new ArrayList<>();

    private List<RestrictionRepresentation> restrictions = new ArrayList<>();

    private boolean hasInterface;

    private boolean hasSuperAbstractClass;

    private boolean isUnion = false;
    private List<DefaultClassRepresentation> unionOf = new ArrayList<>();
    private boolean isIntersection = false;
    private List<DefaultClassRepresentation> intersectionOf = new ArrayList<>();
    private DefaultClassRepresentation complementOf;
    private int sameNameIndex = 0;

    public ClassRepresentation(String namespace, String name,CLASS_TYPE type){
        super(namespace,name,type);
    }

    public ClassRepresentation(String name,CLASS_TYPE type){
        super(name,type);
    }

    public ClassRepresentation(CLASS_TYPE type){
        super(type);
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

    public void addUnionOf(DefaultClassRepresentation unionOf){
        this.unionOf.add(unionOf);
    }

    public List<DefaultClassRepresentation> getUnionOf(){
        return unionOf;
    }

    public boolean isUnionOf(){
        return !unionOf.isEmpty();
    }

    public void addIntersectionOf(DefaultClassRepresentation intersectionOf){
        this.intersectionOf.add(intersectionOf);
    }

    public List<DefaultClassRepresentation> getIntersectionOf(){
        return intersectionOf;
    }

    public boolean isIntersectionOf(){
        return !intersectionOf.isEmpty();
    }

    public void setComplementOf(DefaultClassRepresentation complementOf){
        this.complementOf = complementOf;
    }

    public DefaultClassRepresentation getComplementOf(){
        return complementOf;
    }

    public boolean isComplementOf(){
        return complementOf != null;
    }

    abstract public Resource getResourceValue();

    public int getSameNameIndex(){
        return sameNameIndex;
    }

    public void setSameNameIndex(int index){
        this.sameNameIndex = index;
    }

    public boolean isSameName(){
        return this.sameNameIndex != 0;
    }

    public void addRestriction(RestrictionRepresentation restriction){
        restrictions.add(restriction);
    }

    public List<RestrictionRepresentation> getRestrictions(){
        return restrictions;
    }
}
