
package ontology.generator.classes.examples.allVariantsOfObjectProperties.serialization;

import org.eclipse.rdf4j.model.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

import java.util.stream.Collectors;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.entities.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.time.LocalTime;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.Vocabulary;

public class Class4Serialization extends SerializationModel<Class4Int>{

    @Override
    public void addToModel(Model model, Class4Int class4) {
        model.add(class4.getIri(),RDF.TYPE, class4.getClassIRI());

    }


    protected void setProperties(Model model,Class4Int class4,int nestingLevel) throws Exception{

    }

    @Override
    public Class4Int getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Class4.CLASS_IRI);
        if(statements.size() != 0){
            Class4 class4 = new Class4(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, class4,nestingLevel);
            }
            return class4;
        }

        return null;
    }

    @Override
    public Collection<Class4Int> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Class4.CLASS_IRI);
        Collection<Class4Int> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Class4Int class4 = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(class4);
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
        model.remove(instanceIri,RDF.TYPE,Class4.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Class4Int class4){
        Set<Value> rdfCollections = model.filter(class4.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(class4.getIri(),null,node);
        }

        Model statements = model.filter(class4.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

    }



}

