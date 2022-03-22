package ontology.tool.generator.representations;

import java.util.ArrayList;
import java.util.List;

public class EquivalentClassRepresentation extends DefaultClassRepresentation{

    private List<ClassRepresentation> equivalentClasses = new ArrayList<>();

    private boolean isGenerate = false;

    public EquivalentClassRepresentation(String name){
        super(name);
    }

    public EquivalentClassRepresentation(){
        super();
    }

    public void addEquivalentClass(ClassRepresentation equivalentClass){
        this.equivalentClasses.add(equivalentClass);
    }

    public void addEquivalentClasses(List<ClassRepresentation> equivalentClasses){
        this.equivalentClasses.addAll(equivalentClasses);
    }

    public List<ClassRepresentation> getEquivalentClasses(){
        return equivalentClasses;
    }

    public void setClassNameWithConcatEquivalentClasses(){
        String equivalentName = "";
        for(ClassRepresentation eqClassRep: equivalentClasses) {
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
