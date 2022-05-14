
package ontology.generator.classes.examples.familySimple.serialization;

import org.eclipse.rdf4j.model.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

import java.util.stream.Collectors;
import ontology.generator.classes.examples.familySimple.entities.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.time.LocalTime;
import ontology.generator.classes.examples.familySimple.Vocabulary;

public class PersonSerialization extends SerializationModel<Person>{

    @Override
    public void addToModel(Model model, Person person) {
        model.add(person.getIri(),RDF.TYPE, person.getClassIRI());
        addPropertiesToModel(model,person);

    }

    protected void addPropertiesToModel(Model model,  Person person) {
        if(person.getHasAge() != null){
            model.add(person.getIri(),Vocabulary.HASAGE_PROPERTY_IRI,Values.literal(person.getHasAge()));
        }

        List<OntoEntity> hasCatPom = new ArrayList<>();
        hasCatPom.addAll(person.getHasCat());
        setRDFCollection(model,person.getIri(),Vocabulary.HASCAT_PROPERTY_IRI,hasCatPom);

        if(person.getHasDog() != null ){
            model.add(person.getIri(),Vocabulary.HASDOG_PROPERTY_IRI,person.getHasDog().getIri());
        }



    }

    protected void setProperties(Model model,Person person,int nestingLevel) throws Exception{
        Literal hasAge = super.getFirstLiteralObject(model,Vocabulary.HASAGE_PROPERTY_IRI,person.getIri());
        if ( hasAge != null ){
            person.setHasAge(Integer.valueOf(hasAge.intValue()) );
        }
        Set<Resource> hasCat = super.getAllResourceObjects(model,Vocabulary.HASCAT_PROPERTY_IRI,person.getIri());
        for(Resource propValue:hasCat){
            if(propValue.isIRI()) {
                Cat hasCatInstance = new CatSerialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(hasCatInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                person.addHasCat(hasCatInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Cat hasCatInstance = new CatSerialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(hasCatInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        person.addHasCat(hasCatInstance);
                    }
                 }
            }
        }
        IRI hasDog = super.getFirstIriObject(model,Vocabulary.HASDOG_PROPERTY_IRI,person.getIri());
         if ( hasDog == null){
              // check equivalent property hasDogEq
               hasDog = super.getFirstIriObject(model,Vocabulary.HASDOGEQ_PROPERTY_IRI,person.getIri());
         }
        if ( hasDog != null ){
            Dog hasDogInstance = new DogSerialization().getInstanceFromModel(model, hasDog,nestingLevel);
            person.setHasDog(hasDogInstance);
        }

    }

    @Override
    public Person getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Person.CLASS_IRI);
        if(statements.size() != 0){
            Person person = new Person(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, person,nestingLevel);
            }
            return person;
        }

        return null;
    }

    @Override
    public Collection<Person> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Person.CLASS_IRI);
        Collection<Person> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Person person = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(person);
            }
        }

        return allInstances;
    }

    @Override
    public void removeInstanceFromModel(Model model,IRI instanceIri) {
        Set<Value> rdfCollections = model.filter(instanceIri,null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(instanceIri,null,(BNode)node);
        }
        model.remove(instanceIri,RDF.TYPE,Person.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Person person){
        Set<Value> rdfCollections = model.filter(person.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(person.getIri(),null,node);
        }

        Model statements = model.filter(person.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,person);
    }



}

