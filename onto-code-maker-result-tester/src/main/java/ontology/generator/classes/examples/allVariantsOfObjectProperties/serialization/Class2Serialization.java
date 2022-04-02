
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

public class Class2Serialization extends SerializationModel<Class2Int>{

    @Override
    public void addToModel(Model model, Class2Int class2) {
        model.add(class2.getIri(),RDF.TYPE, class2.getClassIRI());
        addPropertiesToModel(model,class2);

    }

    protected void addPropertiesToModel(Model model,  Class2Int class2) {
        List<OntoEntity> inverseOfObjectPropertyPom = new ArrayList<>();
        inverseOfObjectPropertyPom.addAll(class2.getInverseOfObjectProperty());
        setRDFCollection(model,class2.getIri(),Vocabulary.CLASS2_INVERSEOFOBJECTPROPERTY_PROPERTY_IRI,inverseOfObjectPropertyPom);

        if(class2.getClass1() != null ){
            model.add(class2.getIri(),Vocabulary.CLASS2_CLASS1_PROPERTY_IRI,class2.getClass1().getIri());
        }

        if(class2.getInverseFunctionalObjectProperty2Inverse() != null ){
            model.add(class2.getIri(),Vocabulary.CLASS2_INVERSEFUNCTIONALOBJECTPROPERTY2INVERSE_PROPERTY_IRI,class2.getInverseFunctionalObjectProperty2Inverse().getIri());
        }


        if(class2.getDomainIsAbstractUnionFunctional() != null ){
            model.add(class2.getIri(),Vocabulary.UNIONOFCLASS2CLASS1ABSTRACT_DOMAINISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,class2.getDomainIsAbstractUnionFunctional().getIri());
        }
    }

    protected void setProperties(Model model,Class2Int class2,int nestingLevel) throws Exception{
        Set<Resource> inverseOfObjectProperty = super.getAllResourceObjects(model,Vocabulary.CLASS2_INVERSEOFOBJECTPROPERTY_PROPERTY_IRI,class2.getIri());
        // add also all values from inverse property normalObjectProperty
         inverseOfObjectProperty.addAll(super.getAllIRISubjects(model,Vocabulary.CLASS1_NORMALOBJECTPROPERTY_PROPERTY_IRI,class2.getIri()));
        for(Resource propValue:inverseOfObjectProperty){
            if(propValue.isIRI()) {
                Class1 inverseOfObjectPropertyInstance = new Class1Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(inverseOfObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class2.addInverseOfObjectProperty(inverseOfObjectPropertyInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class1 inverseOfObjectPropertyInstance = new Class1Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(inverseOfObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class2.addInverseOfObjectProperty(inverseOfObjectPropertyInstance);
                    }
                 }
            }
        }
        IRI class1 = super.getFirstIriObject(model,Vocabulary.CLASS2_CLASS1_PROPERTY_IRI,class2.getIri());
        if ( class1 == null){
            // check inverse functional property
            class1 = super.getSubjectOfCollectionValue(model,Vocabulary.CLASS1_INVERSEFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,class2.getIri());
        }
        if ( class1 != null ){
            Class1 class1Instance = new Class1Serialization().getInstanceFromModel(model, class1,nestingLevel);
            class2.setClass1(class1Instance);
        }
        IRI inverseFunctionalObjectProperty2Inverse = super.getFirstIriObject(model,Vocabulary.CLASS2_INVERSEFUNCTIONALOBJECTPROPERTY2INVERSE_PROPERTY_IRI,class2.getIri());
        if ( inverseFunctionalObjectProperty2Inverse == null){
            // check inverse functional property
            inverseFunctionalObjectProperty2Inverse = super.getSubjectOfCollectionValue(model,Vocabulary.CLASS1_INVERSEFUNCTIONALOBJECTPROPERTY2_PROPERTY_IRI,class2.getIri());
        }
        if ( inverseFunctionalObjectProperty2Inverse != null ){
            Class1 inverseFunctionalObjectProperty2InverseInstance = new Class1Serialization().getInstanceFromModel(model, inverseFunctionalObjectProperty2Inverse,nestingLevel);
            class2.setInverseFunctionalObjectProperty2Inverse(inverseFunctionalObjectProperty2InverseInstance);
        }

        IRI domainIsAbstractUnionFunctional = super.getFirstIriObject(model,Vocabulary.UNIONOFCLASS2CLASS1ABSTRACT_DOMAINISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,class2.getIri());
        if ( domainIsAbstractUnionFunctional != null ){
            Class3 domainIsAbstractUnionFunctionalInstance = new Class3Serialization().getInstanceFromModel(model, domainIsAbstractUnionFunctional,nestingLevel);
            class2.setDomainIsAbstractUnionFunctional(domainIsAbstractUnionFunctionalInstance);
        }
    }

    @Override
    public Class2Int getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Class2.CLASS_IRI);
        if(statements.size() != 0){
            Class2 class2 = new Class2(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, class2,nestingLevel);
            }
            return class2;
        }

        return null;
    }

    @Override
    public Collection<Class2Int> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Class2.CLASS_IRI);
        Collection<Class2Int> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Class2Int class2 = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(class2);
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
        model.remove(instanceIri,RDF.TYPE,Class2.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Class2Int class2){
        Set<Value> rdfCollections = model.filter(class2.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(class2.getIri(),null,node);
        }

        Model statements = model.filter(class2.getIri(),null,null);
        statements.removeIf(event -> !event.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,class2);
    }



}

