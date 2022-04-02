
package ontology.generator.classes.examples.family1.serialization;

import org.eclipse.rdf4j.model.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

import java.util.stream.Collectors;
import ontology.generator.classes.examples.family1.entities.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.time.LocalTime;
import ontology.generator.classes.examples.family1.Vocabulary;

public class DogSerialization extends SerializationModel<Dog>{

    @Override
    public void addToModel(Model model, Dog dog) {
        model.add(dog.getIri(),RDF.TYPE, dog.getClassIRI());

    }


    protected void setProperties(Model model,Dog dog,int nestingLevel) throws Exception{

    }

    @Override
    public Dog getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Dog.CLASS_IRI);
        if(statements.size() != 0){
            Dog dog = new Dog(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, dog,nestingLevel);
            }
            return dog;
        }

        return null;
    }

    @Override
    public Collection<Dog> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Dog.CLASS_IRI);
        Collection<Dog> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Dog dog = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(dog);
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
        model.remove(instanceIri,RDF.TYPE,Dog.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Dog dog){
        Set<Value> rdfCollections = model.filter(dog.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(dog.getIri(),null,(BNode)node);
        }

        Model statements = model.filter(dog.getIri(),null,null);
        statements.removeIf(event -> !event.getPredicate().equals(RDF.TYPE));
    }



}

