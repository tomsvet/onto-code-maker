
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

public class Class3Serialization extends SerializationModel<Class3>{

    @Override
    public void addToModel(Model model, Class3 class3) {
        model.add(class3.getIri(),RDF.TYPE, class3.getClassIRI());
        addPropertiesToModel(model,class3);

    }

    protected void addPropertiesToModel(Model model,  Class3 class3) {
        List<OntoEntity> normalProperty2Pom = new ArrayList<>();
        normalProperty2Pom.addAll(class3.getNormalProperty2());
        setRDFCollection(model,class3.getIri(),Vocabulary.CLASS3_NORMALPROPERTY2_PROPERTY_IRI,normalProperty2Pom);

        List<OntoEntity> normalProperty1Pom = new ArrayList<>();
        normalProperty1Pom.addAll(class3.getNormalProperty1());
        setRDFCollection(model,class3.getIri(),Vocabulary.CLASS3_NORMALPROPERTY1_PROPERTY_IRI,normalProperty1Pom);

        if(class3.getRangeIsAbstractUnionFunctional() != null ){
            model.add(class3.getIri(),Vocabulary.CLASS3_RANGEISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,class3.getRangeIsAbstractUnionFunctional().getIri());
        }


    }

    protected void setProperties(Model model,Class3 class3,int nestingLevel) throws Exception{
        Set<Resource> normalProperty2 = super.getAllResourceObjects(model,Vocabulary.CLASS3_NORMALPROPERTY2_PROPERTY_IRI,class3.getIri());
        for(Resource propValue:normalProperty2){
            if(propValue.isIRI()) {
                Class4Int normalProperty2Instance = new Class4Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(normalProperty2Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class3.addNormalProperty2(normalProperty2Instance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class4Int normalProperty2Instance = new Class4Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(normalProperty2Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class3.addNormalProperty2(normalProperty2Instance);
                    }
                 }
            }
        }
        Set<Resource> normalProperty1 = super.getAllResourceObjects(model,Vocabulary.CLASS3_NORMALPROPERTY1_PROPERTY_IRI,class3.getIri());
        for(Resource propValue:normalProperty1){
            if(propValue.isIRI()) {
                Class4Int normalProperty1Instance = new Class4Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(normalProperty1Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class3.addNormalProperty1(normalProperty1Instance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class4Int normalProperty1Instance = new Class4Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(normalProperty1Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class3.addNormalProperty1(normalProperty1Instance);
                    }
                 }
            }
        }
        IRI rangeIsAbstractUnionFunctional = super.getFirstIriObject(model,Vocabulary.CLASS3_RANGEISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,class3.getIri());
        if ( rangeIsAbstractUnionFunctional != null ){
        Class2Int rangeIsAbstractUnionFunctionalInstanceClass2 = new Class2Serialization().getInstanceFromModel(model, rangeIsAbstractUnionFunctional,nestingLevel);
        if(rangeIsAbstractUnionFunctionalInstanceClass2 != null){
            class3.setRangeIsAbstractUnionFunctional(rangeIsAbstractUnionFunctionalInstanceClass2);
             
        }
        Class1 rangeIsAbstractUnionFunctionalInstanceClass1 = new Class1Serialization().getInstanceFromModel(model, rangeIsAbstractUnionFunctional,nestingLevel);
        if(rangeIsAbstractUnionFunctionalInstanceClass1 != null){
            class3.setRangeIsAbstractUnionFunctional(rangeIsAbstractUnionFunctionalInstanceClass1);
             
        }
        }

    }

    @Override
    public Class3 getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
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
    public Collection<Class3> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Class3.CLASS_IRI);
        Collection<Class3> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Class3 class3 = getInstanceFromModel(model,iri,nestingLevel);
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
    public void updateInstanceInModel(Model model,Class3 class3){
        Set<Value> rdfCollections = model.filter(class3.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(class3.getIri(),null,node);
        }

        Model statements = model.filter(class3.getIri(),null,null);
        statements.removeIf(event -> !event.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,class3);
    }



}

