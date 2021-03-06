package ontology.generator.classes.examples.family.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;

/**
*   This is an abstract class that represents complement of Parent.
*
*   Generated by OntoCodeMaker
**/
public abstract class ComplementOfParent implements  EquivalenceChildlessPersonComplementOfParent, OntoEntity {

    // IRI instance
    protected IRI iri;


    public ComplementOfParent(IRI iri){
            this.iri = iri;
    }

    public IRI getIri(){
        return iri;
    }




}

