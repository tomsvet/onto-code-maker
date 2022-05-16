package ontology.generator.classes.examples.allVariantsOfObjectProperties;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.Set;
import org.eclipse.rdf4j.model.util.Values;
import java.util.Collection;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.serialization.*;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.entities.*;

/**
*
*
*   Generated by OntoCodeMaker
**/
public class SerialializationFactory{

    public SerializationModel getSerializationInstance(OntoEntity entity){
            if (entity.getClass() == Class2.class){
                return new Class2Serialization();
            }
            if (entity.getClass() == Class3.class){
                return new Class3Serialization();
            }
            if (entity.getClass() == Class1.class){
                return new Class1Serialization();
            }
            if (entity.getClass() == Class4.class){
                return new Class4Serialization();
            }
        return null;
    }

    public SerializationModel getSerializationInstance(IRI classIri){
            if(classIri.equals(Vocabulary.CLASS2_CLASS_IRI)){
                return new Class2Serialization();
            }
            if(classIri.equals(Vocabulary.CLASS3_CLASS_IRI)){
                return new Class3Serialization();
            }
            if(classIri.equals(Vocabulary.CLASS1_CLASS_IRI)){
                return new Class1Serialization();
            }
            if(classIri.equals(Vocabulary.CLASS4_CLASS_IRI)){
                return new Class4Serialization();
            }
        return null;
    }

    public SerializationModel getSerializationInstance(Model model,IRI instanceIri){
       IRI classIri = getFirstIriObject(model, RDF.TYPE, instanceIri);
            if(classIri.equals(Vocabulary.CLASS2_CLASS_IRI)){
                return new Class2Serialization();
            }
            if(classIri.equals(Vocabulary.CLASS3_CLASS_IRI)){
                return new Class3Serialization();
            }
            if(classIri.equals(Vocabulary.CLASS1_CLASS_IRI)){
                return new Class1Serialization();
            }
            if(classIri.equals(Vocabulary.CLASS4_CLASS_IRI)){
                return new Class4Serialization();
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