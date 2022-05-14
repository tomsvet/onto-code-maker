
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
import ontology.generator.classes.examples.family.entities.Child;
import ontology.generator.classes.examples.family.entities.ParentInt;
import ontology.generator.classes.examples.family.entities.ParentInt;
import ontology.generator.classes.examples.family.entities.ParentInt;

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
        setRDFCollection(model,child.getIri(),Vocabulary.HASPARENT_PROPERTY_IRI,hasParentPom);

        if(child.getParent() != null ){
            model.add(child.getIri(),Vocabulary.PARENT_PROPERTY_IRI,child.getParent().getIri());
        }

        List<OntoEntity> hasFatherPom = new ArrayList<>();
        hasFatherPom.addAll(child.getHasFather());
        setRDFCollection(model,child.getIri(),Vocabulary.HASFATHER_PROPERTY_IRI,hasFatherPom);


    }

    protected void setProperties(Model model,Child child,int nestingLevel) throws Exception{
        Set<Resource> hasParent = super.getAllResourceObjects(model,Vocabulary.HASPARENT_PROPERTY_IRI,child.getIri());
        for(Resource propValue:hasParent){
            if(propValue.isIRI()) {
                ParentInt hasParentInstance = new ParentSerialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(hasParentInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                child.addHasParent(hasParentInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        ParentInt hasParentInstance = new ParentSerialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(hasParentInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        child.addHasParent(hasParentInstance);
                    }
                 }
            }
        }
        IRI parent = super.getFirstIriObject(model,Vocabulary.PARENT_PROPERTY_IRI,child.getIri());
        if ( parent == null){
            // check inverse functional property
            parent = super.getSubjectOfCollectionValue(model,Vocabulary.HASCHILD_PROPERTY_IRI,child.getIri());
        }
        if ( parent != null ){
            ParentInt parentInstance = new ParentSerialization().getInstanceFromModel(model, parent,nestingLevel);
            child.setParent(parentInstance);
        }
        Set<Resource> hasFather = super.getAllResourceObjects(model,Vocabulary.HASFATHER_PROPERTY_IRI,child.getIri());
        for(Resource propValue:hasFather){
            if(propValue.isIRI()) {
                ParentInt hasFatherInstance = new ParentSerialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(hasFatherInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                child.addHasFather(hasFatherInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        ParentInt hasFatherInstance = new ParentSerialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(hasFatherInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        child.addHasFather(hasFatherInstance);
                    }
                 }
            }
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
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(child.getIri(),null,node);
        }

        Model statements = model.filter(child.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,child);
    }



}

