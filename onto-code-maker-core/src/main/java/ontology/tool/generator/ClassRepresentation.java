package ontology.tool.generator;

public class ClassRepresentation {
    private String className;
    private String classNamespace;

    public ClassRepresentation(String namespace,String name){
        classNamespace = namespace;
        className = name;
    }

    public String classNamespace() {
        return classNamespace;
    }

    public String getClassName() {
        return className;
    }

    public String getClassIRI(){
        return classNamespace + className;
    }
}
