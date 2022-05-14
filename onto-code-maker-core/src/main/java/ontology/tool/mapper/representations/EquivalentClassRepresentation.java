package ontology.tool.mapper.representations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ontology.tool.generator.OntologyGenerator.ENTITY_EQUIVALENCE_PREFIX;

/**
 *  EquivalentClassRepresentation.java
 *
 *  Representation of equivalent classes in inner model.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
public class EquivalentClassRepresentation extends DefaultClassRepresentation{

    private List<ClassRepresentation> equivalentClasses = new ArrayList<>();

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

    public List<ClassRepresentation> getEquivalentClasses(){
        return equivalentClasses;
    }

    public void setClassNameWithConcatEquivalentClasses(){
        String equivalentName = ENTITY_EQUIVALENCE_PREFIX;
        List<String> sortedNames = equivalentClasses.stream().map(r -> r.getName()).sorted().collect(Collectors.toList());
        for(String eqClassName: sortedNames) {
            equivalentName = equivalentName.concat(eqClassName);
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
