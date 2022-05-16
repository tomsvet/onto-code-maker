package unitTests;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.jupiter.api.BeforeAll;

/***
 *  ModelSetUp.java
 *
 *  Class defining some constant values for testing and doing base model setup
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
public class ModelSetUp {
    static String ex = "http://www.ontocodemaker.org/Family#";
    static Model model = new TreeModel();
    static IRI classPerson = Values.iri(ex, "Person");
    static IRI classHuman = Values.iri(ex, "Human");
    static IRI classMen = Values.iri(ex, "Men");
    static IRI classWoman = Values.iri(ex, "Woman");
    static IRI classMother = Values.iri(ex, "Mother");
    static IRI classParent = Values.iri(ex, "Parent");
    static IRI classFather = Values.iri(ex, "Father");
    static IRI classChild = Values.iri(ex, "Child");
    static IRI classChildlessPerson = Values.iri(ex, "ChildlessPerson");
    static IRI classPet = Values.iri(ex, "Pet");
    static IRI classDog = Values.iri(ex, "Dog");
    static IRI classCat = Values.iri(ex, "Cat");
    static IRI classDoggy = Values.iri(ex, "Doggy");

    static IRI testOnt = Values.iri("http://www.ontocodemaker.org/family.owl#Family");
    static IRI testOntPrior = Values.iri("http://www.example.org/2021/priorVersionTest");
    static IRI testOntImport = Values.iri(ex, "imports.owl");

    static IRI hasAge = Values.iri(ex, "hasAge");
    static IRI age = Values.iri(ex,"age");
    static IRI hasAgeDatatype = Values.iri("http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
    static IRI hasDog = Values.iri(ex, "hasDog");
    static IRI hasCat = Values.iri(ex, "hasCat");
    static IRI hasChild = Values.iri(ex, "hasChild");
    static IRI hasDogEq = Values.iri(ex, "hasDogEq");
    static IRI hasFather = Values.iri(ex, "hasFather");
    static IRI hasHusband = Values.iri(ex, "hasHusband");
    static IRI hasParent = Values.iri(ex, "hasParent");
    static IRI hasWife = Values.iri(ex, "hasWife");
    static IRI hasLuckyNumbers = Values.iri(ex, "hasLuckyNumbers");
    static IRI hasSpouse = Values.iri(ex, "hasSpouse");

    @BeforeAll
    public static void setUp() {
        model.add(classHuman, RDF.TYPE, RDFS.CLASS);
        model.add(classPerson, RDF.TYPE, RDFS.CLASS);
        model.add(classMen, RDF.TYPE, OWL.CLASS);
        model.add(classWoman, RDF.TYPE, OWL.CLASS);

        model.add(classMen, DC.CREATOR,Values.literal("Tomas"));
        model.add(classMen, RDFS.LABEL,Values.literal("Men"));
        model.add(classMen, RDFS.COMMENT,Values.literal("This is men"));
        model.add(classMen,RDFS.SUBCLASSOF,classPerson);
        model.add(classWoman,RDFS.SUBCLASSOF,classPerson);
        model.add(classPerson,OWL.EQUIVALENTCLASS,classHuman);

    }
}

