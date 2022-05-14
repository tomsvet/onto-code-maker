package ontology.generator.classes.examples.family.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;
import ontology.generator.classes.examples.family.Vocabulary;

/**
*  This is the class representing the Person(http://www.ontocodemaker.org/Family#Person) class from ontology
*
*   Generated by OntoCodeMaker
**/
public class Person implements  EquivalenceHumanPerson {

    // IRI instance
    protected IRI iri;
    // IRI Constant of Class
    public static IRI CLASS_IRI = Vocabulary.PERSON_CLASS_IRI;

    /**
    * Property http://www.ontocodemaker.org/Family#hasLuckyNumbers
*  These are lucky numbers
*  hasLuckyNumbers
    **/
    private List<Integer> hasLuckyNumbers = new ArrayList<>();

    /**
    * Property http://www.ontocodemaker.org/Family#hasAge
*  This is hasAge
*  hasAge
    **/
    private Integer hasAge;

    /**
    * Property http://www.ontocodemaker.org/Family#hasCat
    **/
    private List<Cat> hasCat = new ArrayList<>();

    /**
    * Property http://www.ontocodemaker.org/Family#hasDog
*  This is hasDog
*  hasDog
    **/
    private Dog hasDog;

    /**
    * Property http://www.ontocodemaker.org/Family#age
    * The property is SubProperty of  hasAge
    **/
    private List<Integer> age = new ArrayList<>();

    /**
    * Property http://www.ontocodemaker.org/Family#hasDogEq
    * The property is equivalent to hasDog
    **/
    private List<Dog> hasDogEq = new ArrayList<>();


    public Person(IRI iri){
            this.iri = iri;
    }

    public IRI getIri(){
        return iri;
    }

    public IRI getClassIRI() {
        return CLASS_IRI;
    }

    public void addHasLuckyNumbers(Integer hasLuckyNumbers){
        this.hasLuckyNumbers.add(hasLuckyNumbers);
    }
    public List<Integer> getHasLuckyNumbers(){
        return hasLuckyNumbers;
    }

    public void setHasAge(Integer hasAge){
        this.hasAge = hasAge;
    }
    public Integer getHasAge(){
        return hasAge;
    }

    public void addHasCat(Cat hasCat){
        this.hasCat.add(hasCat);
    }
    public List<Cat> getHasCat(){
        return hasCat;
    }

    public void setHasDog(Dog hasDog){
        this.hasDog = hasDog;
        if(!this.hasDogEq.contains(hasDog)){
            this.hasDogEq.add(hasDog);
        }
    }
    public Dog getHasDog(){
        return hasDog;
    }

    public void addAge(Integer age){
        this.age.add(age);
        if(this.hasAge == null || !this.hasAge.equals(age)){
            this.hasAge = age;
        }
    }
    public List<Integer> getAge(){
        return age;
    }

    public List<Dog> getHasDogEq(){
        return hasDogEq;
    }



}

