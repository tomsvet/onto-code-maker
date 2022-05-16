
package ontology.generator.classes.examples.family.serialization;

import org.eclipse.rdf4j.model.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

import java.util.stream.Collectors;
import ontology.generator.classes.examples.family.entities.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.time.LocalTime;
import ontology.generator.classes.examples.family.Vocabulary;
import ontology.generator.classes.examples.family.entities.Pet;

public class PetSerialization extends SerializationModel<Pet>{

    @Override
    public void addToModel(Model model, Pet pet) {
        model.add(pet.getIri(),RDF.TYPE, pet.getClassIRI());

    }


    protected void setProperties(Model model,Pet pet,int nestingLevel) throws Exception{

    }

    @Override
    public Pet getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Pet.CLASS_IRI);
        if(statements.size() != 0){
            Pet pet = new Pet(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, pet,nestingLevel);
            }
            return pet;
        }

        return null;
    }

    @Override
    public Collection<Pet> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Pet.CLASS_IRI);
        Collection<Pet> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Pet pet = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(pet);
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
        model.remove(instanceIri,RDF.TYPE,Pet.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Pet pet){
        Set<Value> rdfCollections = model.filter(pet.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(pet.getIri(),null,node);
        }

        Model statements = model.filter(pet.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

    }



}

