
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
import ontology.generator.classes.examples.family.entities.ChildlessPerson;

public class ChildlessPersonSerialization extends SerializationModel<ChildlessPerson>{

    @Override
    public void addToModel(Model model, ChildlessPerson childlessPerson) {
        model.add(childlessPerson.getIri(),RDF.TYPE, childlessPerson.getClassIRI());

    }


    protected void setProperties(Model model,ChildlessPerson childlessPerson,int nestingLevel) throws Exception{

    }

    @Override
    public ChildlessPerson getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,ChildlessPerson.CLASS_IRI);
        if(statements.size() != 0){
            ChildlessPerson childlessPerson = new ChildlessPerson(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, childlessPerson,nestingLevel);
            }
            return childlessPerson;
        }

        return null;
    }

    @Override
    public Collection<ChildlessPerson> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,ChildlessPerson.CLASS_IRI);
        Collection<ChildlessPerson> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                ChildlessPerson childlessPerson = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(childlessPerson);
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
        model.remove(instanceIri,RDF.TYPE,ChildlessPerson.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,ChildlessPerson childlessPerson){
        Set<Value> rdfCollections = model.filter(childlessPerson.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(childlessPerson.getIri(),null,node);
        }

        Model statements = model.filter(childlessPerson.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

    }



}

