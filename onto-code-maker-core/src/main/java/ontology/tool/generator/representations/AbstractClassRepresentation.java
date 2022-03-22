package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Values;

public class AbstractClassRepresentation extends ClassRepresentation {
    private String bnodeId;

    private boolean isGenerated = false;

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

    public void setClassNameWithConcatUnionClasses(){
        String unionName = "UnionOf";
        for(ClassRepresentation eqClassRep: getUnionOf()) {
            unionName = unionName.concat(eqClassRep.getName());
        }
        this.setName(unionName);
    }
}
