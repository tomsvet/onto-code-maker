package ontology.tool.generator.representations;

public class ClassRepresentation extends EntityRepresentation{

    public ClassRepresentation(String namespace,String name){
        super(namespace,name);
    }

    public String getConstantName(){return this.getName().toUpperCase()  + "_CLASS_IRI";}
}
