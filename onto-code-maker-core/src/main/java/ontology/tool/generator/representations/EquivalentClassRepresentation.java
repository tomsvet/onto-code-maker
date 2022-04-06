package ontology.tool.generator.representations;

import java.util.ArrayList;
import java.util.List;

import static ontology.tool.generator.OntologyGenerator.ENTITY_EQUIVALENCE_PREFIX;

public class EquivalentClassRepresentation extends DefaultClassRepresentation{

    private List<ClassRepresentation> equivalentClasses = new ArrayList<>();

   // private List<RestrictionRepresentation> equivalentRestrictionClasses = new ArrayList<>();

    private boolean isGenerate = false;

    public EquivalentClassRepresentation(String name){
        super(name,CLASS_TYPE.EQUIVALENT);
    }

    public EquivalentClassRepresentation(){
        super(CLASS_TYPE.EQUIVALENT);
    }

    public void addEquivalentClass(ClassRepresentation equivalentClass){
        this.equivalentClasses.add(equivalentClass);
    }

    public void addEquivalentClasses(List<ClassRepresentation> equivalentClasses){
        this.equivalentClasses.addAll(equivalentClasses);
    }

   /* public List<RestrictionRepresentation> getEquivalentRestrictionClasses(){
        return equivalentRestrictionClasses;
    }

    public void addEquivalentRestrictionClass(RestrictionRepresentation equivalentRestrictionClasses){
        this.equivalentRestrictionClasses.add(equivalentRestrictionClasses);
    }

    public void addEquivalentRestrictionClasses(List<RestrictionRepresentation> equivalentRestrictionClasses){
        this.equivalentRestrictionClasses.addAll(equivalentRestrictionClasses);
    }*/

    public List<ClassRepresentation> getEquivalentClasses(){
        return equivalentClasses;
    }

    public void setClassNameWithConcatEquivalentClasses(){
        String equivalentName = ENTITY_EQUIVALENCE_PREFIX;
        for(DefaultClassRepresentation eqClassRep: equivalentClasses) {
            equivalentName = equivalentName.concat(eqClassRep.getName());
        }
        this.setName(equivalentName);
    }

    public boolean isGenerate(){
        return isGenerate;
    }

    public void setIsGenerate(boolean isGenerate){
        this.isGenerate = isGenerate;
    }

}
