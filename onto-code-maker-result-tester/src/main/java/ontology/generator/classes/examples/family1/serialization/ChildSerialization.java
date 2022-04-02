
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

public class ChildSerialization extends SerializationModel<Child>{

    @Override
    public void addToModel(Model model, Child child) {
        model.add(child.getIri(),RDF.TYPE, child.getClassIRI());
        addPropertiesToModel(model,child);

    }

    protected void addPropertiesToModel(Model model,  Child child) {
        List<OntoEntity> hasParentPom = new ArrayList<>();
                hasParentPom.addAll(child.getHasParent());
        hasParentPom.removeAll(child.getHasFather());
        setRDFCollection(model,child.getIri(),Vocabulary.CHILD_HASPARENT_PROPERTY_IRI,hasParentPom);
        List<OntoEntity> hasFatherPom = new ArrayList<>();
                hasFatherPom.addAll(child.getHasFather());
        setRDFCollection(model,child.getIri(),Vocabulary.CHILD_HASFATHER_PROPERTY_IRI,hasFatherPom);
        if(child.getParent() != null ){
            model.add(child.getIri(),Vocabulary.CHILD_PARENT_PROPERTY_IRI,child.getParent().getIri());
        }
    }

    protected void setProperties(Model model,Child child,int nestingLevel) throws Exception{

        Set<Resource> hasParent = super.getAllResourceObjects(model,Vocabulary.CHILD_HASPARENT_PROPERTY_IRI,child.getIri());
        for(Resource propValue:hasParent){
            if(propValue.isIRI()) {
                Parent hasParentInstance = new ParentSerialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(hasParentInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                child.addHasParent(hasParentInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Parent hasParentInstance = new ParentSerialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(hasParentInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        child.addHasParent(hasParentInstance);
                    }
                 }
            }
        }

        Set<Resource> hasFather = super.getAllResourceObjects(model,Vocabulary.CHILD_HASFATHER_PROPERTY_IRI,child.getIri());
        for(Resource propValue:hasFather){
            if(propValue.isIRI()) {
                Parent hasFatherInstance = new ParentSerialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(hasFatherInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                child.addHasFather(hasFatherInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Parent hasFatherInstance = new ParentSerialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(hasFatherInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        child.addHasFather(hasFatherInstance);
                    }
                 }
            }
        }

        IRI parent = super.getFirstIriObject(model,Vocabulary.CHILD_PARENT_PROPERTY_IRI,child.getIri());
        if ( parent == null){
            // check inverse functional property
            parent = super.getSubjectOfCollectionValue(model,Vocabulary.PARENT_HASCHILD_PROPERTY_IRI,child.getIri());
        }
        if ( parent != null ){
            Parent parentInstance = new ParentSerialization().getInstanceFromModel(model, parent,nestingLevel);
            child.setParent(parentInstance);
        }

    }

    @Override
    public Child getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Child.CLASS_IRI);
        if(statements.size() != 0){
            Child child = new Child(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, child,nestingLevel);
            }
            return child;
        }

        return null;
    }

    @Override
    public Collection<Child> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Child.CLASS_IRI);
        Collection<Child> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Child child = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(child);
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
        model.remove(instanceIri,RDF.TYPE,Child.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Child child){
        Set<Value> rdfCollections = model.filter(child.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(child.getIri(),null,(BNode)node);
        }

        Model statements = model.filter(child.getIri(),null,null);
        Iterator<Statement> iter = statements.iterator();
        while (iter.hasNext()) {
            Statement event = iter.next();
            if(!event.getPredicate().equals(RDF.TYPE)){
                iter.remove();
            }
        }
        addPropertiesToModel(model,child);
    }



}

