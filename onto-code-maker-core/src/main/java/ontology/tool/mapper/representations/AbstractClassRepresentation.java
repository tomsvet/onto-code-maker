package ontology.tool.mapper.representations;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Values;

import java.util.List;
import java.util.stream.Collectors;

import static ontology.tool.generator.OntologyGenerator.ENTITY_ABSTRACTCLASS_SUFFIX;
import static ontology.tool.generator.OntologyGenerator.ENTITY_INTERFACE_SUFFIX;

/**
 *  AbstractClassRepresentation.java
 *
 *  Represenation of Abstract class in inner model.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
public class AbstractClassRepresentation extends ClassRepresentation {
    private String bnodeId;

    private boolean isGenerated = false;
    private boolean toRemove = false;

    private ABSTRACT_CREATE_OF createOf;

    public enum ABSTRACT_CREATE_OF {
        UNIONOF,
        EQUIVALENT,
        INTERSECTIONOF,
        COMPLEMENT
    }

    /**
     * This constructor is used for Bnodes , for abstract classes
     * @param id
     */
    public AbstractClassRepresentation(String id) {
        super(CLASS_TYPE.ABSTRACT);
        this.bnodeId = id;
    }

    @Override
    public Resource getResourceValue(){
        return Values.bnode(this.bnodeId);
    }

    public String getBnodeId(){
        return bnodeId;
    }

    public boolean isGenerated(){
        return this.isGenerated;
    }

    public void setIsGenerated(boolean isGenerate){
        this.isGenerated = isGenerate;
    }

    public boolean isToRemove(){
        return toRemove;
    }

    public void setToRemove(boolean toRemove){
        this.toRemove = toRemove;
    }


    public void setAbstractClassName(){
        if(ABSTRACT_CREATE_OF.UNIONOF.equals(this.createOf)){
            setClassNameWithConcatUnionClasses();
        }else if(ABSTRACT_CREATE_OF.INTERSECTIONOF.equals(this.createOf)){
            setClassNameWithConcatIntersectionClasses();
        }else if(ABSTRACT_CREATE_OF.COMPLEMENT.equals(this.createOf)){
            setClassNameWithConcatComplementClass();
        }
    }

    private void setClassNameWithConcatUnionClasses(){
        String unionName = "UnionOf";
        List<String> sortedNames = getUnionOf().stream().map(r -> r.getName()).sorted().collect(Collectors.toList());
        for(String eqClassName: sortedNames) {
            unionName = unionName.concat(eqClassName);
        }
        this.setName(unionName);
    }

    private void setClassNameWithConcatIntersectionClasses(){
        String intersectionName = "IntersectionOf";
        List<String> sortedNames = getIntersectionOf().stream().map(r -> r.getName()).sorted().collect(Collectors.toList());
        for(String eqClassName: sortedNames) {
            intersectionName = intersectionName.concat(eqClassName);
        }
        this.setName(intersectionName + ENTITY_ABSTRACTCLASS_SUFFIX);
    }

    private void setClassNameWithConcatComplementClass(){
        this.setName("ComplementOf"+ getComplementOf().getName() + ENTITY_ABSTRACTCLASS_SUFFIX);
    }

    public void setCreateOf(ABSTRACT_CREATE_OF createOf){
        this.createOf = createOf;
    }

    public ABSTRACT_CREATE_OF getCreateOf(){
        return createOf;
    }


    public boolean isEmptyClass(){
        return !(this.isUnionOf() || this.isIntersectionOf() || this.isComplementOf() || this.getEquivalentClass() != null || this.hasSubClass());
    }

}
