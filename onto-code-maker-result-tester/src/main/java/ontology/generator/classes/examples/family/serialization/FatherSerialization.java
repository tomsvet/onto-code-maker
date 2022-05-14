
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
import ontology.generator.classes.examples.family.entities.Father;

public class FatherSerialization extends SerializationModel<Father>{

    @Override
    public void addToModel(Model model, Father father) {
        model.add(father.getIri(),RDF.TYPE, father.getClassIRI());

    }


    protected void setProperties(Model model,Father father,int nestingLevel) throws Exception{

    }

    @Override
    public Father getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Father.CLASS_IRI);
        if(statements.size() != 0){
            Father father = new Father(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, father,nestingLevel);
            }
            return father;
        }

        return null;
    }

    @Override
    public Collection<Father> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Father.CLASS_IRI);
        Collection<Father> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Father father = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(father);
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
        model.remove(instanceIri,RDF.TYPE,Father.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Father father){
        Set<Value> rdfCollections = model.filter(father.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(father.getIri(),null,node);
        }

        Model statements = model.filter(father.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

    }



}

