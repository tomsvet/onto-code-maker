package ontology.tool.generator.representations;

public class EntityRepresentation {
    private String name;
    private String namespace;

    public EntityRepresentation(String namespace,String name){
        this.namespace = namespace;
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public String getIRI(){
        return namespace + name;
    }
}
