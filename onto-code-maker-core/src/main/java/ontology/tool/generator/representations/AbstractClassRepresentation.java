package ontology.tool.generator.representations;

import ontology.tool.generator.OntologyGenerator;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Values;

import static ontology.tool.generator.OntologyGenerator.ENTITY_ABSTRACTCLASS_SUFFIX;

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
        return isGenerated;
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

    public void setClassNameWithConcatUnionClasses(){
        String unionName = "UnionOf";
        for(ClassRepresentation eqClassRep: getUnionOf()) {
            unionName = unionName.concat(eqClassRep.getName());
        }
        this.setName(unionName + ENTITY_ABSTRACTCLASS_SUFFIX);
    }

    public void setClassNameWithConcatIntersectionClasses(){
        String intersectionName = "IntersectionOf";
        for(ClassRepresentation eqClassRep: getIntersectionOf()) {
            intersectionName = intersectionName.concat(eqClassRep.getName());
        }
        this.setName(intersectionName + ENTITY_ABSTRACTCLASS_SUFFIX);
    }

    public void setClassNameWithConcatComplementClass(){
        this.setName("ComplementOf"+ getComplementOf().getName() + ENTITY_ABSTRACTCLASS_SUFFIX);
    }

    public void setCreateOf(ABSTRACT_CREATE_OF createOf){
        this.createOf = createOf;
    }


}
