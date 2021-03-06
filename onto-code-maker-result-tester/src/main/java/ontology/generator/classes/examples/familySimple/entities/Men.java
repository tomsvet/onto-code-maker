package ontology.generator.classes.examples.familySimple.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;
import ontology.generator.classes.examples.familySimple.Vocabulary;

/**
*  This is the class representing the Men(http://www.ontocodemaker.org/Family#Men) class from ontology
*  This class is subclass of  Human
*
*  Men
*  This is men
*   Generated by OntoCodeMaker
**/
public class Men extends  Human {

    // IRI Constant of Class
    public static IRI CLASS_IRI = Vocabulary.MEN_CLASS_IRI;


    public Men(IRI iri){
            super(iri);
    }


    public IRI getClassIRI() {
        return CLASS_IRI;
    }



}

