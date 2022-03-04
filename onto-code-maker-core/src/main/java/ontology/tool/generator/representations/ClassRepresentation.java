package ontology.tool.generator.representations;

import java.util.ArrayList;
import java.util.List;

import static ontology.tool.generator.OntologyGenerator.SERIALIZATION_FILE_NAME_SUFFIX;

public class ClassRepresentation extends EntityRepresentation{

    private List<ClassRepresentation> subClasses = new ArrayList<>();
    private List<ClassRepresentation> superClasses = new ArrayList<>();

    private List<ClassRepresentation> equivalentClasses = new ArrayList<>();

    private List<PropertyRepresentation> properties = new ArrayList<>();

    private boolean hasInterface;
    private boolean hasEquivalentClassInterface;

    private String equivalentInterfaceName;

    public ClassRepresentation(String namespace,String name){
        super(namespace,name);
    }

    public String getConstantName(){return this.getName().toUpperCase()  + "_CLASS_IRI";}

    public String getSerializationClassName(){return this.getName() + SERIALIZATION_FILE_NAME_SUFFIX;}

    public void addProperties(PropertyRepresentation property){
        this.properties.add(property);
    }

    public List<PropertyRepresentation> getProperties() {
        return properties;
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

    public List<ClassRepresentation> getSuperClasses(){
        return superClasses;
    }

    public boolean hasSuperClass(){
        return !superClasses.isEmpty();
    }

    public boolean hasOneSuperClass(){
        return superClasses.size() ==1;
    }

    public boolean isHasInterface(){
        return hasInterface;
    }

    public void setHasInterface(boolean value){
        this.hasInterface = value;
    }


    public void addEquivalentClasses(ClassRepresentation equivalentClasses){
        this.equivalentClasses.add(equivalentClasses);
    }

    public List<ClassRepresentation> getEquivalentClasses(){
        return equivalentClasses;
    }

    public boolean isHasEquivalentClassInterface() {
        return hasEquivalentClassInterface;
    }

    public void setHasEquivalentClassInterface(boolean hasEquivalentClassInterface) {
        this.hasEquivalentClassInterface = hasEquivalentClassInterface;
    }

    public void setEquivalentInterfaceName(String equivalentInterfaceName) {
        this.equivalentInterfaceName = equivalentInterfaceName;
    }

    public String getEquivalentInterfaceName() {
        return equivalentInterfaceName;
    }
}
