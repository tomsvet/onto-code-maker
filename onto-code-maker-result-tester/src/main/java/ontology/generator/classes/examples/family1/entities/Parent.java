package ontology.generator.classes.examples.family1.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;
import ontology.generator.classes.examples.family1.Vocabulary;

/**
*  This is the class representing the Parent class from ontology
*
*   Generated by OntoCodeMaker
**/
public class Parent implements  OntoEntity {

    // IRI instance
    protected IRI iri;
    // IRI Constant of Class
    public static IRI CLASS_IRI = Vocabulary.PARENT_CLASS_IRI;

    /**
    * Property http://example.com/hasChild
    **/
    private List<Child> hasChild = new ArrayList<>();


    public Parent(IRI iri){
            this.iri = iri;
    }

    public IRI getIri(){
        return iri;
    }

    public IRI getClassIRI() {
        return CLASS_IRI;
    }

    public void addHasChild(Child hasChild){
        this.hasChild.add(hasChild);
    }
    public List<Child> getHasChild(){
        return hasChild;
    }



}
