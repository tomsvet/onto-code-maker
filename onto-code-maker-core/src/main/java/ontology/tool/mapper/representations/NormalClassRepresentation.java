package ontology.tool.mapper.representations;

import static ontology.tool.generator.OntologyGenerator.SERIALIZATION_FILE_NAME_SUFFIX;

/**
 *  NormalClassRepresentation.java
 *
 *  Representation of basic class in inner model.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
public class NormalClassRepresentation extends ClassRepresentation{

    public static String  CLASS_CONSTANT_SUFFIX= "_CLASS_IRI";

    public NormalClassRepresentation(String namespace, String name){
        super(namespace,name,CLASS_TYPE.NORMAL);
    }

    public String getConstantName(){return this.getName().toUpperCase()  + CLASS_CONSTANT_SUFFIX;}

    public String getStringIRI(){
        return super.getNamespace() + super.getName();
    }

    public String getSerializationClassName(){return this.getName() + SERIALIZATION_FILE_NAME_SUFFIX;}

}
