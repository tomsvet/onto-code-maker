package ontology.generator.classes.examples.allVariantsOfDatatypeProperties.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;
import ontology.generator.classes.examples.allVariantsOfDatatypeProperties.Vocabulary;

/**
*  This is the class representing the Class3(http://example.com/Class3) class from ontology
*
*   Generated by OntoCodeMaker
**/
public class Class3 implements  Class3Int {

    // IRI instance
    protected IRI iri;
    // IRI Constant of Class
    public static IRI CLASS_IRI = Vocabulary.CLASS3_CLASS_IRI;


    public Class3(IRI iri){
            this.iri = iri;
    }

    public IRI getIri(){
        return iri;
    }

    public IRI getClassIRI() {
        return CLASS_IRI;
    }



}

