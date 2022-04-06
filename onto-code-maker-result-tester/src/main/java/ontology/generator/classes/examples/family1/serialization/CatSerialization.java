
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

public class CatSerialization extends SerializationModel<Cat>{

    @Override
    public void addToModel(Model model, Cat cat) {
        model.add(cat.getIri(),RDF.TYPE, cat.getClassIRI());

    }


    protected void setProperties(Model model,Cat cat,int nestingLevel) throws Exception{

    }

    @Override
    public Cat getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Cat.CLASS_IRI);
        if(statements.size() != 0){
            Cat cat = new Cat(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, cat,nestingLevel);
            }
            return cat;
        }

        return null;
    }

    @Override
    public Collection<Cat> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Cat.CLASS_IRI);
        Collection<Cat> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Cat cat = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(cat);
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
        model.remove(instanceIri,RDF.TYPE,Cat.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Cat cat){
        Set<Value> rdfCollections = model.filter(cat.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(cat.getIri(),null,(BNode)node);
        }

        Model statements = model.filter(cat.getIri(),null,null);
        Iterator<Statement> iter = statements.iterator();
        while (iter.hasNext()) {
            Statement event = iter.next();
            if(!event.getPredicate().equals(RDF.TYPE)){
                iter.remove();
            }
        }
    }



}
