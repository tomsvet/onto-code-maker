package ontology.generator.classes.examples.allVariantsOfObjectProperties.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.Vocabulary;

/**
*  This is the class representing the Class2(http://example.com/Class2) class from ontology
*  This class is subclass of  UnionOfClass1Class2
*
*   Generated by OntoCodeMaker
**/
public class Class2 implements  Class2Int {

    // IRI instance
    protected IRI iri;
    // IRI Constant of Class
    public static IRI CLASS_IRI = Vocabulary.CLASS2_CLASS_IRI;

    /**
    * Property http://example.com/inverseOfObjectProperty
*  Inverse Object Property.
*  Inverse
    **/
    private List<Class1> inverseOfObjectProperty = new ArrayList<>();

    /**
    * Property http://example.com/class1
    **/
    private Class1 class1;

    /**
    * Property http://example.com/inverseFunctionalObjectProperty2Inverse
    **/
    private Class1 inverseFunctionalObjectProperty2Inverse;

    /**
    * Property http://example.com/domainIsAbstractUnionFunctional. This property is from super class.
*  Functional Datatype property created with union of Class1 and Class2.
*  Union Of
    **/
    private Class3 domainIsAbstractUnionFunctional;


    public Class2(IRI iri){
            this.iri = iri;
    }

    public IRI getIri(){
        return iri;
    }

    public IRI getClassIRI() {
        return CLASS_IRI;
    }

    public void addInverseOfObjectProperty(Class1 inverseOfObjectProperty){
        this.inverseOfObjectProperty.add(inverseOfObjectProperty);
    }
    public List<Class1> getInverseOfObjectProperty(){
        return inverseOfObjectProperty;
    }

    public void setClass1(Class1 class1){
        this.class1 = class1;
    }
    public Class1 getClass1(){
        return class1;
    }

    public void setInverseFunctionalObjectProperty2Inverse(Class1 inverseFunctionalObjectProperty2Inverse){
        this.inverseFunctionalObjectProperty2Inverse = inverseFunctionalObjectProperty2Inverse;
    }
    public Class1 getInverseFunctionalObjectProperty2Inverse(){
        return inverseFunctionalObjectProperty2Inverse;
    }


    public void setDomainIsAbstractUnionFunctional(Class3 domainIsAbstractUnionFunctional){
        this.domainIsAbstractUnionFunctional = domainIsAbstractUnionFunctional;
    }
    public Class3 getDomainIsAbstractUnionFunctional(){
        return domainIsAbstractUnionFunctional;
    }

}

