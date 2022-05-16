package ontology.tool.mapper.representations;

import java.util.ArrayList;
import java.util.List;

import static ontology.tool.generator.OntologyGenerator.ENTITY_INTERFACE_SUFFIX;

/**
 *  ClassRepresentation.java
 *
 *  Represenation of all classes in inner model.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
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

    private List<DefaultClassRepresentation> disjointWith = new ArrayList<>();

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
        if(equivalentClass != null){
            for (ClassRepresentation eqClasses : equivalentClass.getEquivalentClasses()) {
                if(!eqClasses.getProperties().contains(property)) {
                    eqClasses.addProperties(property);
                }
            }
        }
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

    public void addDisjointWith(DefaultClassRepresentation disjointWith){
        this.disjointWith.add(disjointWith);
    }

    public List<DefaultClassRepresentation> getDisjointWith(){
        return disjointWith;
    }

    public boolean isDisjointWith(){
        return !disjointWith.isEmpty();
    }

    public String getDatatypeValue(){
        if(isHasInterface()){
            return getName() + ENTITY_INTERFACE_SUFFIX;
        }else{
            return getName();
        }
    }
}
