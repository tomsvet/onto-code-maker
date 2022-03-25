package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Values;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultClassRepresentation extends EntityRepresentation{

    private List<ClassRepresentation> subClasses = new ArrayList<>();
    private List<ClassRepresentation> superClasses = new ArrayList<>();

    public DefaultClassRepresentation(String namespace, String name){
        super(namespace,name);
    }

    public DefaultClassRepresentation(String name){
        super(name);
    }

    public DefaultClassRepresentation(){
        super();
    }

    public void addSubClasses(ClassRepresentation subClasses) {
        this.subClasses.add(subClasses);
    }

    public List<ClassRepresentation> getSubClasses(){
        return subClasses;
    }

    public boolean hasSubClass(){
        return !subClasses.isEmpty();
    }

    public void addSuperClasses(ClassRepresentation superClasses) {
        this.superClasses.add(superClasses);
    }

    public void addAllSuperClasses(List<ClassRepresentation> superClasses) {
        this.superClasses.addAll(superClasses);
    }

    public List<ClassRepresentation> getSuperClasses(){
        return superClasses;
    }

    public boolean hasSuperClass(){
        return !superClasses.isEmpty();
    }

    public boolean hasOneSuperClass(){
        return superClasses.size() ==1;
    }

}
