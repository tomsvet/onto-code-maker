package ontology.generator.classes.examples.family;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.Set;
import org.eclipse.rdf4j.model.util.Values;
import java.util.Collection;
import ontology.generator.classes.examples.family.serialization.*;
import ontology.generator.classes.examples.family.entities.*;

/**
*
*
*   Generated by OntoCodeMaker
**/
public class SerialializationFactory{

    public SerializationModel getSerializationInstance(OntoEntity entity){
            if (entity.getClass() == Human.class){
                return new HumanSerialization();
            }
            if (entity.getClass() == Child.class){
                return new ChildSerialization();
            }
            if (entity.getClass() == Person.class){
                return new PersonSerialization();
            }
            if (entity.getClass() == Father.class){
                return new FatherSerialization();
            }
            if (entity.getClass() == Parent.class){
                return new ParentSerialization();
            }
            if (entity.getClass() == Mother.class){
                return new MotherSerialization();
            }
            if (entity.getClass() == Woman.class){
                return new WomanSerialization();
            }
            if (entity.getClass() == Doggy.class){
                return new DoggySerialization();
            }
            if (entity.getClass() == Cat.class){
                return new CatSerialization();
            }
            if (entity.getClass() == Men.class){
                return new MenSerialization();
            }
            if (entity.getClass() == ChildlessPerson.class){
                return new ChildlessPersonSerialization();
            }
            if (entity.getClass() == Dog.class){
                return new DogSerialization();
            }
            if (entity.getClass() == Pet.class){
                return new PetSerialization();
            }
        return null;
    }

    public SerializationModel getSerializationInstance(IRI classIri){
            if(classIri.equals(Vocabulary.HUMAN_CLASS_IRI)){
                return new HumanSerialization();
            }
            if(classIri.equals(Vocabulary.CHILD_CLASS_IRI)){
                return new ChildSerialization();
            }
            if(classIri.equals(Vocabulary.PERSON_CLASS_IRI)){
                return new PersonSerialization();
            }
            if(classIri.equals(Vocabulary.FATHER_CLASS_IRI)){
                return new FatherSerialization();
            }
            if(classIri.equals(Vocabulary.PARENT_CLASS_IRI)){
                return new ParentSerialization();
            }
            if(classIri.equals(Vocabulary.MOTHER_CLASS_IRI)){
                return new MotherSerialization();
            }
            if(classIri.equals(Vocabulary.WOMAN_CLASS_IRI)){
                return new WomanSerialization();
            }
            if(classIri.equals(Vocabulary.DOGGY_CLASS_IRI)){
                return new DoggySerialization();
            }
            if(classIri.equals(Vocabulary.CAT_CLASS_IRI)){
                return new CatSerialization();
            }
            if(classIri.equals(Vocabulary.MEN_CLASS_IRI)){
                return new MenSerialization();
            }
            if(classIri.equals(Vocabulary.CHILDLESSPERSON_CLASS_IRI)){
                return new ChildlessPersonSerialization();
            }
            if(classIri.equals(Vocabulary.DOG_CLASS_IRI)){
                return new DogSerialization();
            }
            if(classIri.equals(Vocabulary.PET_CLASS_IRI)){
                return new PetSerialization();
            }
        return null;
    }

    public SerializationModel getSerializationInstance(Model model,IRI instanceIri){
       IRI classIri = getFirstIriObject(model, RDF.TYPE, instanceIri);
            if(classIri.equals(Vocabulary.HUMAN_CLASS_IRI)){
                return new HumanSerialization();
            }
            if(classIri.equals(Vocabulary.CHILD_CLASS_IRI)){
                return new ChildSerialization();
            }
            if(classIri.equals(Vocabulary.PERSON_CLASS_IRI)){
                return new PersonSerialization();
            }
            if(classIri.equals(Vocabulary.FATHER_CLASS_IRI)){
                return new FatherSerialization();
            }
            if(classIri.equals(Vocabulary.PARENT_CLASS_IRI)){
                return new ParentSerialization();
            }
            if(classIri.equals(Vocabulary.MOTHER_CLASS_IRI)){
                return new MotherSerialization();
            }
            if(classIri.equals(Vocabulary.WOMAN_CLASS_IRI)){
                return new WomanSerialization();
            }
            if(classIri.equals(Vocabulary.DOGGY_CLASS_IRI)){
                return new DoggySerialization();
            }
            if(classIri.equals(Vocabulary.CAT_CLASS_IRI)){
                return new CatSerialization();
            }
            if(classIri.equals(Vocabulary.MEN_CLASS_IRI)){
                return new MenSerialization();
            }
            if(classIri.equals(Vocabulary.CHILDLESSPERSON_CLASS_IRI)){
                return new ChildlessPersonSerialization();
            }
            if(classIri.equals(Vocabulary.DOG_CLASS_IRI)){
                return new DogSerialization();
            }
            if(classIri.equals(Vocabulary.PET_CLASS_IRI)){
                return new PetSerialization();
            }
       return null;
    }

    public IRI getFirstIriObject(Model model,IRI predicate, IRI instanceIri){
        Set<Value> objects = model.filter(instanceIri,predicate,null).objects();
        for(Value object : objects){
            if(object.isIRI()){
                return (IRI) object;
            }
        }
        return null;
    }
}