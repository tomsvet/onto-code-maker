package ontology.generator.classes.examples.family;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import java.util.Collection;
import ontology.generator.classes.examples.family.serialization.*;
import ontology.generator.classes.examples.family.entities.*;

/**
* This Code is generated from these ontologies
* Ontology: Family
* Prior version: http://www.example.org/2021/priorVersionTest 
*
*   This ontology Was created for testing OntoCodeMaker.
*   Family
*
*
*   Generated by OntoCodeMaker
**/
public class FamilyFactory{

    private Model ontology;
    private SerialializationFactory serializationFactory;

    public FamilyFactory(Model ontology){
        this.ontology = ontology;
        this.serializationFactory = new SerialializationFactory();
    }

    public Model getOntology(){
        return ontology;
    }

    public void addToModel(OntoEntity entity){
            serializationFactory.getSerializationInstance(entity).addToModel(ontology, entity);
    }

    public void updateInstanceInModel(OntoEntity entity){
        serializationFactory.getSerializationInstance(entity).updateInstanceInModel(ontology, entity);
    }

    public Human createHuman(String name){
        return new Human(Values.iri(name));
    }

    public Human getHumanFromModel(String name) throws Exception{
        return (Human) serializationFactory.getSerializationInstance(Vocabulary.HUMAN_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Human> getAllHumanInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.HUMAN_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeHumanFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.HUMAN_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Child createChild(String name){
        return new Child(Values.iri(name));
    }

    public Child getChildFromModel(String name) throws Exception{
        return (Child) serializationFactory.getSerializationInstance(Vocabulary.CHILD_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Child> getAllChildInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.CHILD_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeChildFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.CHILD_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Person createPerson(String name){
        return new Person(Values.iri(name));
    }

    public Person getPersonFromModel(String name) throws Exception{
        return (Person) serializationFactory.getSerializationInstance(Vocabulary.PERSON_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Person> getAllPersonInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.PERSON_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removePersonFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.PERSON_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Father createFather(String name){
        return new Father(Values.iri(name));
    }

    public Father getFatherFromModel(String name) throws Exception{
        return (Father) serializationFactory.getSerializationInstance(Vocabulary.FATHER_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Father> getAllFatherInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.FATHER_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeFatherFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.FATHER_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Parent createParent(String name){
        return new Parent(Values.iri(name));
    }

    public Parent getParentFromModel(String name) throws Exception{
        return (Parent) serializationFactory.getSerializationInstance(Vocabulary.PARENT_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Parent> getAllParentInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.PARENT_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeParentFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.PARENT_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Mother createMother(String name){
        return new Mother(Values.iri(name));
    }

    public Mother getMotherFromModel(String name) throws Exception{
        return (Mother) serializationFactory.getSerializationInstance(Vocabulary.MOTHER_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Mother> getAllMotherInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.MOTHER_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeMotherFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.MOTHER_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Woman createWoman(String name){
        return new Woman(Values.iri(name));
    }

    public Woman getWomanFromModel(String name) throws Exception{
        return (Woman) serializationFactory.getSerializationInstance(Vocabulary.WOMAN_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Woman> getAllWomanInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.WOMAN_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeWomanFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.WOMAN_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Doggy createDoggy(String name){
        return new Doggy(Values.iri(name));
    }

    public Doggy getDoggyFromModel(String name) throws Exception{
        return (Doggy) serializationFactory.getSerializationInstance(Vocabulary.DOGGY_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Doggy> getAllDoggyInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.DOGGY_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeDoggyFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.DOGGY_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Cat createCat(String name){
        return new Cat(Values.iri(name));
    }

    public Cat getCatFromModel(String name) throws Exception{
        return (Cat) serializationFactory.getSerializationInstance(Vocabulary.CAT_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Cat> getAllCatInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.CAT_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeCatFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.CAT_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Men createMen(String name){
        return new Men(Values.iri(name));
    }

    public Men getMenFromModel(String name) throws Exception{
        return (Men) serializationFactory.getSerializationInstance(Vocabulary.MEN_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Men> getAllMenInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.MEN_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeMenFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.MEN_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public ChildlessPerson createChildlessPerson(String name){
        return new ChildlessPerson(Values.iri(name));
    }

    public ChildlessPerson getChildlessPersonFromModel(String name) throws Exception{
        return (ChildlessPerson) serializationFactory.getSerializationInstance(Vocabulary.CHILDLESSPERSON_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<ChildlessPerson> getAllChildlessPersonInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.CHILDLESSPERSON_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeChildlessPersonFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.CHILDLESSPERSON_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Dog createDog(String name){
        return new Dog(Values.iri(name));
    }

    public Dog getDogFromModel(String name) throws Exception{
        return (Dog) serializationFactory.getSerializationInstance(Vocabulary.DOG_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Dog> getAllDogInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.DOG_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeDogFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.DOG_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Pet createPet(String name){
        return new Pet(Values.iri(name));
    }

    public Pet getPetFromModel(String name) throws Exception{
        return (Pet) serializationFactory.getSerializationInstance(Vocabulary.PET_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Pet> getAllPetInstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.PET_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removePetFromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.PET_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }

}