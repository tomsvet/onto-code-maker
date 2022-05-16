
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
import ontology.generator.classes.examples.family.entities.Doggy;

public class DoggySerialization extends SerializationModel<Doggy>{

    @Override
    public void addToModel(Model model, Doggy doggy) {
        model.add(doggy.getIri(),RDF.TYPE, doggy.getClassIRI());

    }


    protected void setProperties(Model model,Doggy doggy,int nestingLevel) throws Exception{

    }

    @Override
    public Doggy getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Doggy.CLASS_IRI);
        if(statements.size() != 0){
            Doggy doggy = new Doggy(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, doggy,nestingLevel);
            }
            return doggy;
        }

        return null;
    }

    @Override
    public Collection<Doggy> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Doggy.CLASS_IRI);
        Collection<Doggy> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Doggy doggy = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(doggy);
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
        model.remove(instanceIri,RDF.TYPE,Doggy.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Doggy doggy){
        Set<Value> rdfCollections = model.filter(doggy.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(doggy.getIri(),null,node);
        }

        Model statements = model.filter(doggy.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

    }



}

