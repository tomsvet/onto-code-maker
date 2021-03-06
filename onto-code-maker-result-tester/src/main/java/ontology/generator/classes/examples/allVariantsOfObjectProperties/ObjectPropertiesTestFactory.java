package ontology.generator.classes.examples.allVariantsOfObjectProperties;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import java.util.Collection;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.serialization.*;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.entities.*;

/**
* This Code is generated from these ontologies
* Ontology: ObjectPropertiesTest

*
*
*
*   Generated by OntoCodeMaker
**/
public class ObjectPropertiesTestFactory{

    private Model ontology;
    private SerialializationFactory serializationFactory;

    public ObjectPropertiesTestFactory(Model ontology){
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

    public Class2 createClass2(String name){
        return new Class2(Values.iri(name));
    }

    public Class2 getClass2FromModel(String name) throws Exception{
        return (Class2) serializationFactory.getSerializationInstance(Vocabulary.CLASS2_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Class2> getAllClass2InstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.CLASS2_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeClass2FromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.CLASS2_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Class3 createClass3(String name){
        return new Class3(Values.iri(name));
    }

    public Class3 getClass3FromModel(String name) throws Exception{
        return (Class3) serializationFactory.getSerializationInstance(Vocabulary.CLASS3_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Class3> getAllClass3InstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.CLASS3_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeClass3FromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.CLASS3_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Class1 createClass1(String name){
        return new Class1(Values.iri(name));
    }

    public Class1 getClass1FromModel(String name) throws Exception{
        return (Class1) serializationFactory.getSerializationInstance(Vocabulary.CLASS1_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Class1> getAllClass1InstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.CLASS1_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeClass1FromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.CLASS1_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }
    public Class4 createClass4(String name){
        return new Class4(Values.iri(name));
    }

    public Class4 getClass4FromModel(String name) throws Exception{
        return (Class4) serializationFactory.getSerializationInstance(Vocabulary.CLASS4_CLASS_IRI).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<Class4> getAllClass4InstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(Vocabulary.CLASS4_CLASS_IRI).getAllInstancesFromModel(ontology,2);
    }

    public void removeClass4FromModel(String name){
        serializationFactory.getSerializationInstance(Vocabulary.CLASS4_CLASS_IRI).removeInstanceFromModel(ontology,Values.iri(name));
    }

}