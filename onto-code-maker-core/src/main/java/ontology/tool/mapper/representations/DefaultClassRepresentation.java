package ontology.tool.mapper.representations;

import java.util.ArrayList;
import java.util.List;

public abstract class DefaultClassRepresentation extends EntityRepresentation{

    public enum CLASS_TYPE {
        ABSTRACT("Abstract"),
        NORMAL("Normal"),
        RESTRICTION("Restriction"),
        EQUIVALENT("Equivalent");

        private final String name;

        /**
         * @param name
         */
        private CLASS_TYPE(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private ClassRepresentation.CLASS_TYPE classType;

    private List<ClassRepresentation> subClasses = new ArrayList<>();
    private List<ClassRepresentation> superClasses = new ArrayList<>();

    public DefaultClassRepresentation(String namespace, String name,CLASS_TYPE type){
        super(namespace,name);
        this.classType = type;
    }

    public DefaultClassRepresentation(String name,CLASS_TYPE type){
        super(name);
        this.classType = type;
    }

    public DefaultClassRepresentation(CLASS_TYPE type){
        super();
        this.classType = type;
    }

    public CLASS_TYPE getClassType(){
        return classType;
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
