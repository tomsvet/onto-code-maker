
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
import ontology.generator.classes.examples.family.entities.Mother;

public class MotherSerialization extends SerializationModel<Mother>{

    @Override
    public void addToModel(Model model, Mother mother) {
        model.add(mother.getIri(),RDF.TYPE, mother.getClassIRI());

    }


    protected void setProperties(Model model,Mother mother,int nestingLevel) throws Exception{

    }

    @Override
    public Mother getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Mother.CLASS_IRI);
        if(statements.size() != 0){
            Mother mother = new Mother(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, mother,nestingLevel);
            }
            return mother;
        }

        return null;
    }

    @Override
    public Collection<Mother> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Mother.CLASS_IRI);
        Collection<Mother> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Mother mother = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(mother);
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
        model.remove(instanceIri,RDF.TYPE,Mother.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Mother mother){
        Set<Value> rdfCollections = model.filter(mother.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(mother.getIri(),null,node);
        }

        Model statements = model.filter(mother.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

    }



}

