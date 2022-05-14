
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

public class MenSerialization extends SerializationModel<Men>{

    @Override
    public void addToModel(Model model, Men men) {
        model.add(men.getIri(),RDF.TYPE, men.getClassIRI());
        addPropertiesToModel(model,men);

    }

    protected void addPropertiesToModel(Model model,  Men men) {

         new HumanSerialization().addPropertiesToModel(model, men);
    }

    protected void setProperties(Model model,Men men,int nestingLevel) throws Exception{

        new HumanSerialization().setProperties(model, men,nestingLevel);
    }

    @Override
    public Men getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Men.CLASS_IRI);
        if(statements.size() != 0){
            Men men = new Men(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, men,nestingLevel);
            }
            return men;
        }

        return null;
    }

    @Override
    public Collection<Men> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Men.CLASS_IRI);
        Collection<Men> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Men men = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(men);
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
        model.remove(instanceIri,RDF.TYPE,Men.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Men men){
        Set<Value> rdfCollections = model.filter(men.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(men.getIri(),null,node);
        }

        Model statements = model.filter(men.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,men);
    }



}

