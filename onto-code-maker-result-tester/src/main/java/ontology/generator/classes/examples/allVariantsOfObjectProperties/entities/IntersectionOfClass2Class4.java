package ontology.generator.classes.examples.allVariantsOfObjectProperties.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;

/**
*   This is an abstract class that represents intersection of  Class2,  Class4.
*  This class is subclass of  Class2,  Class4
*
*   Generated by OntoCodeMaker
**/
public abstract class IntersectionOfClass2Class4 implements  Class2Int, Class4Int {

    // IRI instance
    protected IRI iri;

    /**
    * Property http://example.com/domainIsAbstractIntersectionOfFunctional
*  Functional Datatype property created with abstract class which is Intersection of Class1 and Class2.
*  Abstract Class Intersection Of
    **/
    private Class3 domainIsAbstractIntersectionOfFunctional;

    /**
    * Property http://example.com/inverseOfObjectProperty. This property is from super class.
*  Inverse Object Property.
*  Inverse
    **/
    private List<Class1> inverseOfObjectProperty = new ArrayList<>();

    /**
    * Property http://example.com/class1. This property is from super class.
    **/
    private Class1 class1;

    /**
    * Property http://example.com/inverseFunctionalObjectProperty2Inverse. This property is from super class.
    **/
    private Class1 inverseFunctionalObjectProperty2Inverse;

    /**
    * Property http://example.com/domainIsAbstractUnionFunctional. This property is from super class.
*  Functional Datatype property created with union of Class1 and Class2.
*  Union Of
    **/
    private Class3 domainIsAbstractUnionFunctional;


    public IntersectionOfClass2Class4(IRI iri){
            this.iri = iri;
    }

    public IRI getIri(){
        return iri;
    }


    public void setDomainIsAbstractIntersectionOfFunctional(Class3 domainIsAbstractIntersectionOfFunctional){
        this.domainIsAbstractIntersectionOfFunctional = domainIsAbstractIntersectionOfFunctional;
    }
    public Class3 getDomainIsAbstractIntersectionOfFunctional(){
        return domainIsAbstractIntersectionOfFunctional;
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

