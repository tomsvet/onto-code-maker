
package ontology.generator.classes.examples.allVariantsOfDatatypeProperties.serialization;

import org.eclipse.rdf4j.model.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

import java.util.stream.Collectors;
import ontology.generator.classes.examples.allVariantsOfDatatypeProperties.entities.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.time.LocalTime;
import ontology.generator.classes.examples.allVariantsOfDatatypeProperties.Vocabulary;

public class Class3Serialization extends SerializationModel<Class3Int>{

    @Override
    public void addToModel(Model model, Class3Int class3) {
        model.add(class3.getIri(),RDF.TYPE, class3.getClassIRI());

    }


    protected void setProperties(Model model,Class3Int class3,int nestingLevel) throws Exception{

    }

    @Override
    public Class3Int getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Class3.CLASS_IRI);
        if(statements.size() != 0){
            Class3 class3 = new Class3(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, class3,nestingLevel);
            }
            return class3;
        }

        return null;
    }

    @Override
    public Collection<Class3Int> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Class3.CLASS_IRI);
        Collection<Class3Int> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Class3Int class3 = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(class3);
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
        model.remove(instanceIri,RDF.TYPE,Class3.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Class3Int class3){
        Set<Value> rdfCollections = model.filter(class3.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(class3.getIri(),null,node);
        }

        Model statements = model.filter(class3.getIri(),null,null);
        statements.removeIf(event -> !event.getPredicate().equals(RDF.TYPE));

    }



}

