package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Values;

import static ontology.tool.generator.OntologyGenerator.SERIALIZATION_FILE_NAME_SUFFIX;

public class NormalClassRepresentation extends ClassRepresentation{

    public static String  CLASS_CONSTANT_SUFFIX= "_CLASS_IRI";

    public NormalClassRepresentation(String namespace, String name){
        super(namespace,name,CLASS_TYPE.NORMAL);
    }

    public String getConstantName(){return this.getName().toUpperCase()  + CLASS_CONSTANT_SUFFIX;}

    public IRI getValueIRI(){
        return Values.iri(super.getNamespace() + super.getName());
    }

    public String getStringIRI(){
        return super.getNamespace() + super.getName();
    }

    public String getSerializationClassName(){return this.getName() + SERIALIZATION_FILE_NAME_SUFFIX;}

    public Resource getResourceValue(){
        return Values.iri(super.getNamespace() + super.getName());
    }

}
