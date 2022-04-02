package ontology.generator.classes.examples.allVariantsOfObjectProperties.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;

/**
*  This is the class representing the ComplementOfClass2Abstract class from ontology
*
*   Generated by OntoCodeMaker
**/
public abstract class ComplementOfClass2Abstract implements  OntoEntity {

    // IRI instance
    protected IRI iri;

    /**
    * Property http://example.com/domainIsAbstractComplementOfFunctional
    **/
    private Class3 domainIsAbstractComplementOfFunctional;


    public ComplementOfClass2Abstract(IRI iri){
            this.iri = iri;
    }

    public IRI getIri(){
        return iri;
    }


    public void setDomainIsAbstractComplementOfFunctional(Class3 domainIsAbstractComplementOfFunctional){
        this.domainIsAbstractComplementOfFunctional = domainIsAbstractComplementOfFunctional;
    }
    public Class3 getDomainIsAbstractComplementOfFunctional(){
        return domainIsAbstractComplementOfFunctional;
    }



}

