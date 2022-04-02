
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

public class ParentSerialization extends SerializationModel<Parent>{

    @Override
    public void addToModel(Model model, Parent parent) {
        model.add(parent.getIri(),RDF.TYPE, parent.getClassIRI());
        addPropertiesToModel(model,parent);

    }

    protected void addPropertiesToModel(Model model,  Parent parent) {
        List<OntoEntity> hasChildPom = new ArrayList<>();
                hasChildPom.addAll(parent.getHasChild());
        setRDFCollection(model,parent.getIri(),Vocabulary.PARENT_HASCHILD_PROPERTY_IRI,hasChildPom);
    }

    protected void setProperties(Model model,Parent parent,int nestingLevel) throws Exception{

        Set<Resource> hasChild = super.getAllResourceObjects(model,Vocabulary.PARENT_HASCHILD_PROPERTY_IRI,parent.getIri());
        for(Resource propValue:hasChild){
            if(propValue.isIRI()) {
                Child hasChildInstance = new ChildSerialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(hasChildInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                parent.addHasChild(hasChildInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Child hasChildInstance = new ChildSerialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(hasChildInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        parent.addHasChild(hasChildInstance);
                    }
                 }
            }
        }

    }

    @Override
    public Parent getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Parent.CLASS_IRI);
        if(statements.size() != 0){
            Parent parent = new Parent(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, parent,nestingLevel);
            }
            return parent;
        }

        return null;
    }

    @Override
    public Collection<Parent> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Parent.CLASS_IRI);
        Collection<Parent> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Parent parent = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(parent);
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
        model.remove(instanceIri,RDF.TYPE,Parent.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Parent parent){
        Set<Value> rdfCollections = model.filter(parent.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(parent.getIri(),null,(BNode)node);
        }

        Model statements = model.filter(parent.getIri(),null,null);
        Iterator<Statement> iter = statements.iterator();
        while (iter.hasNext()) {
            Statement event = iter.next();
            if(!event.getPredicate().equals(RDF.TYPE)){
                iter.remove();
            }
        }
        addPropertiesToModel(model,parent);
    }



}

