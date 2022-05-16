package ontology.generator.classes.examples.familySimple;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;

public class Vocabulary {
    /**
    *   A constant representing the ontology Family
    **/
    public static final IRI FAMILY_ONTOLOGY_IRI  = Values.iri("http://www.ontocodemaker.org/familySimple.owl#Family");

    /**
    *   A constant representing the class Human
    **/
    public static final IRI HUMAN_CLASS_IRI  = Values.iri("http://www.ontocodemaker.org/Family#Human");

    /**
    *   A constant representing the class Dog
    **/
    public static final IRI DOG_CLASS_IRI  = Values.iri("http://www.ontocodemaker.org/Family#Dog");

    /**
    *   A constant representing the class Pet
    **/
    public static final IRI PET_CLASS_IRI  = Values.iri("http://www.ontocodemaker.org/Family#Pet");

    /**
    *   A constant representing the class Person
    **/
    public static final IRI PERSON_CLASS_IRI  = Values.iri("http://www.ontocodemaker.org/Family#Person");

    /**
    *   A constant representing the class Cat
    **/
    public static final IRI CAT_CLASS_IRI  = Values.iri("http://www.ontocodemaker.org/Family#Cat");

    /**
    *   A constant representing the class Men
    **/
    public static final IRI MEN_CLASS_IRI  = Values.iri("http://www.ontocodemaker.org/Family#Men");

    /**
    *   A constant representing the property hasCat
    **/
    public static final IRI HASCAT_PROPERTY_IRI  = Values.iri("http://www.ontocodemaker.org/Family#hasCat");

    /**
    *   A constant representing the property hasDogEq
    **/
    public static final IRI HASDOGEQ_PROPERTY_IRI  = Values.iri("http://www.ontocodemaker.org/Family#hasDogEq");

    /**
    *   A constant representing the property hasAge
    **/
    public static final IRI HASAGE_PROPERTY_IRI  = Values.iri("http://www.ontocodemaker.org/Family#hasAge");

    /**
    *   A constant representing the property hasDog
    **/
    public static final IRI HASDOG_PROPERTY_IRI  = Values.iri("http://www.ontocodemaker.org/Family#hasDog");

}