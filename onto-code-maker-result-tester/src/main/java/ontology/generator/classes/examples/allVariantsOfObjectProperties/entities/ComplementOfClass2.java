package ontology.generator.classes.examples.allVariantsOfObjectProperties.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;

/**
*   This is an abstract class that represents complement of Class2.
*
*   Generated by OntoCodeMaker
**/
public abstract class ComplementOfClass2 implements  OntoEntity {

    // IRI instance
    protected IRI iri;

    /**
    * Property http://example.com/domainIsAbstractComplementOfFunctional
*  Functional Datatype property created with abstract class which is complement of Class2.
*  Abstract complement Of
    **/
    private Class3 domainIsAbstractComplementOfFunctional;


    public ComplementOfClass2(IRI iri){
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
