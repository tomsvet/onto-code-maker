package ontology.generator.classes.examples.familySimple;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.Set;
import org.eclipse.rdf4j.model.util.Values;
import java.util.Collection;
import ontology.generator.classes.examples.familySimple.serialization.*;
import ontology.generator.classes.examples.familySimple.entities.*;

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
            if (entity.getClass() == Dog.class){
                return new DogSerialization();
            }
            if (entity.getClass() == Pet.class){
                return new PetSerialization();
            }
            if (entity.getClass() == Person.class){
                return new PersonSerialization();
            }
            if (entity.getClass() == Cat.class){
                return new CatSerialization();
            }
            if (entity.getClass() == Men.class){
                return new MenSerialization();
            }
        return null;
    }

    public SerializationModel getSerializationInstance(IRI classIri){
            if(classIri.equals(Vocabulary.HUMAN_CLASS_IRI)){
                return new HumanSerialization();
            }
            if(classIri.equals(Vocabulary.DOG_CLASS_IRI)){
                return new DogSerialization();
            }
            if(classIri.equals(Vocabulary.PET_CLASS_IRI)){
                return new PetSerialization();
            }
            if(classIri.equals(Vocabulary.PERSON_CLASS_IRI)){
                return new PersonSerialization();
            }
            if(classIri.equals(Vocabulary.CAT_CLASS_IRI)){
                return new CatSerialization();
            }
            if(classIri.equals(Vocabulary.MEN_CLASS_IRI)){
                return new MenSerialization();
            }
        return null;
    }

    public SerializationModel getSerializationInstance(Model model,IRI instanceIri){
       IRI classIri = getFirstIriObject(model, RDF.TYPE, instanceIri);
            if(classIri.equals(Vocabulary.HUMAN_CLASS_IRI)){
                return new HumanSerialization();
            }
            if(classIri.equals(Vocabulary.DOG_CLASS_IRI)){
                return new DogSerialization();
            }
            if(classIri.equals(Vocabulary.PET_CLASS_IRI)){
                return new PetSerialization();
            }
            if(classIri.equals(Vocabulary.PERSON_CLASS_IRI)){
                return new PersonSerialization();
            }
            if(classIri.equals(Vocabulary.CAT_CLASS_IRI)){
                return new CatSerialization();
            }
            if(classIri.equals(Vocabulary.MEN_CLASS_IRI)){
                return new MenSerialization();
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