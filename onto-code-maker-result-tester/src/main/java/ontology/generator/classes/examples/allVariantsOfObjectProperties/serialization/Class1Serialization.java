
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

public class Class1Serialization extends SerializationModel<Class1>{

    @Override
    public void addToModel(Model model, Class1 class1) {
        model.add(class1.getIri(),RDF.TYPE, class1.getClassIRI());
        addPropertiesToModel(model,class1);

    }

    protected void addPropertiesToModel(Model model,  Class1 class1) {
        List<OntoEntity> normalObjectPropertyPom = new ArrayList<>();
        normalObjectPropertyPom.addAll(class1.getNormalObjectProperty());
        normalObjectPropertyPom.removeAll(class1.getSubPropertyOfObjectProperty());
        normalObjectPropertyPom.removeAll(class1.getSubPropertyOfEquivalentObjectProperty());
        setRDFCollection(model,class1.getIri(),Vocabulary.NORMALOBJECTPROPERTY_PROPERTY_IRI,normalObjectPropertyPom);
         for(OntoEntity pom:normalObjectPropertyPom){
             model.add(pom.getIri(),Vocabulary.INVERSEOFOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
         }

        List<OntoEntity> inverseFunctionalObjectPropertyPom = new ArrayList<>();
        inverseFunctionalObjectPropertyPom.addAll(class1.getInverseFunctionalObjectProperty());
        inverseFunctionalObjectPropertyPom.removeAll(class1.getSubPropertyOfInverseFunctionalObjectProperty());
        setRDFCollection(model,class1.getIri(),Vocabulary.INVERSEFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,inverseFunctionalObjectPropertyPom);

        if(class1.getFunctionalObjectProperty2() != null ){
            model.add(class1.getIri(),Vocabulary.FUNCTIONALOBJECTPROPERTY2_PROPERTY_IRI,class1.getFunctionalObjectProperty2().getIri());
        }

        List<OntoEntity> inverseFunctionalObjectProperty2Pom = new ArrayList<>();
        inverseFunctionalObjectProperty2Pom.addAll(class1.getInverseFunctionalObjectProperty2());
        setRDFCollection(model,class1.getIri(),Vocabulary.INVERSEFUNCTIONALOBJECTPROPERTY2_PROPERTY_IRI,inverseFunctionalObjectProperty2Pom);

        if(class1.getFunctionalObjectProperty() != null         && class1.getFunctionalObjectProperty() != class1.getSubPropertyOfFunctionalObjectProperty()){
            model.add(class1.getIri(),Vocabulary.FUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,class1.getFunctionalObjectProperty().getIri());
        }


        List<OntoEntity> subPropertyOfObjectPropertyPom = new ArrayList<>();
        subPropertyOfObjectPropertyPom.addAll(class1.getSubPropertyOfObjectProperty());
        subPropertyOfObjectPropertyPom.removeAll(class1.getSubPropertyOfSubpropertyObjectProperty());
        setRDFCollection(model,class1.getIri(),Vocabulary.SUBPROPERTYOFOBJECTPROPERTY_PROPERTY_IRI,subPropertyOfObjectPropertyPom);


        List<OntoEntity> subPropertyOfSubpropertyObjectPropertyPom = new ArrayList<>();
        subPropertyOfSubpropertyObjectPropertyPom.addAll(class1.getSubPropertyOfSubpropertyObjectProperty());
        setRDFCollection(model,class1.getIri(),Vocabulary.SUBPROPERTYOFSUBPROPERTYOBJECTPROPERTY_PROPERTY_IRI,subPropertyOfSubpropertyObjectPropertyPom);


        List<OntoEntity> subPropertyOfInverseFunctionalObjectPropertyPom = new ArrayList<>();
        subPropertyOfInverseFunctionalObjectPropertyPom.addAll(class1.getSubPropertyOfInverseFunctionalObjectProperty());
        setRDFCollection(model,class1.getIri(),Vocabulary.SUBPROPERTYOFINVERSEFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,subPropertyOfInverseFunctionalObjectPropertyPom);


        List<OntoEntity> subPropertyOfFunctionalObjectPropertyPom = new ArrayList<>();
        subPropertyOfFunctionalObjectPropertyPom.addAll(class1.getSubPropertyOfFunctionalObjectProperty());
        setRDFCollection(model,class1.getIri(),Vocabulary.SUBPROPERTYOFFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,subPropertyOfFunctionalObjectPropertyPom);


        List<OntoEntity> subPropertyOfEquivalentObjectPropertyPom = new ArrayList<>();
        subPropertyOfEquivalentObjectPropertyPom.addAll(class1.getSubPropertyOfEquivalentObjectProperty());
        setRDFCollection(model,class1.getIri(),Vocabulary.SUBPROPERTYOFEQUIVALENTOBJECTPROPERTY_PROPERTY_IRI,subPropertyOfEquivalentObjectPropertyPom);


        if(class1.getDomainIsAbstractUnionFunctional() != null ){
            model.add(class1.getIri(),Vocabulary.DOMAINISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,class1.getDomainIsAbstractUnionFunctional().getIri());
        }
    }

    protected void setProperties(Model model,Class1 class1,int nestingLevel) throws Exception{
        Set<Resource> normalObjectProperty = super.getAllResourceObjects(model,Vocabulary.NORMALOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
         // check equivalent equivalentObjectProperty
         normalObjectProperty.addAll(super.getAllResourceObjects(model,Vocabulary.EQUIVALENTOBJECTPROPERTY_PROPERTY_IRI,class1.getIri()));
         // check equivalent equivalentToSubpropertyObjectProperty
         normalObjectProperty.addAll(super.getAllResourceObjects(model,Vocabulary.EQUIVALENTTOSUBPROPERTYOBJECTPROPERTY_PROPERTY_IRI,class1.getIri()));
         // check equivalent equivalentToEquivalentObjectProperty
         normalObjectProperty.addAll(super.getAllResourceObjects(model,Vocabulary.EQUIVALENTTOEQUIVALENTOBJECTPROPERTY_PROPERTY_IRI,class1.getIri()));
        for(Resource propValue:normalObjectProperty){
            if(propValue.isIRI()) {
                Class2Int normalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(normalObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class1.addNormalObjectProperty(normalObjectPropertyInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class2Int normalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(normalObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class1.addNormalObjectProperty(normalObjectPropertyInstance);
                    }
                 }
            }
        }
        Set<Resource> inverseFunctionalObjectProperty = super.getAllResourceObjects(model,Vocabulary.INVERSEFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
         // check equivalent equivalentToInverseFunctionalObjectProperty
         inverseFunctionalObjectProperty.addAll(super.getAllResourceObjects(model,Vocabulary.EQUIVALENTTOINVERSEFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,class1.getIri()));
        for(Resource propValue:inverseFunctionalObjectProperty){
            if(propValue.isIRI()) {
                Class2Int inverseFunctionalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(inverseFunctionalObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class1.addInverseFunctionalObjectProperty(inverseFunctionalObjectPropertyInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class2Int inverseFunctionalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(inverseFunctionalObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class1.addInverseFunctionalObjectProperty(inverseFunctionalObjectPropertyInstance);
                    }
                 }
            }
        }
        IRI functionalObjectProperty2 = super.getFirstIriObject(model,Vocabulary.FUNCTIONALOBJECTPROPERTY2_PROPERTY_IRI,class1.getIri());
        if ( functionalObjectProperty2 != null ){
            Class2Int functionalObjectProperty2Instance = new Class2Serialization().getInstanceFromModel(model, functionalObjectProperty2,nestingLevel);
            class1.setFunctionalObjectProperty2(functionalObjectProperty2Instance);
        }
        Set<Resource> inverseFunctionalObjectProperty2 = super.getAllResourceObjects(model,Vocabulary.INVERSEFUNCTIONALOBJECTPROPERTY2_PROPERTY_IRI,class1.getIri());
        for(Resource propValue:inverseFunctionalObjectProperty2){
            if(propValue.isIRI()) {
                Class2Int inverseFunctionalObjectProperty2Instance = new Class2Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(inverseFunctionalObjectProperty2Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class1.addInverseFunctionalObjectProperty2(inverseFunctionalObjectProperty2Instance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class2Int inverseFunctionalObjectProperty2Instance = new Class2Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(inverseFunctionalObjectProperty2Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class1.addInverseFunctionalObjectProperty2(inverseFunctionalObjectProperty2Instance);
                    }
                 }
            }
        }
        IRI functionalObjectProperty = super.getFirstIriObject(model,Vocabulary.FUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
         if ( functionalObjectProperty == null){
              // check equivalent property equivalentToFunctionalObjectProperty
               functionalObjectProperty = super.getFirstIriObject(model,Vocabulary.EQUIVALENTTOFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
         }
        if ( functionalObjectProperty != null ){
            Class2Int functionalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, functionalObjectProperty,nestingLevel);
            class1.setFunctionalObjectProperty(functionalObjectPropertyInstance);
        }
        Set<Resource> subPropertyOfObjectProperty = super.getAllResourceObjects(model,Vocabulary.SUBPROPERTYOFOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
         // check equivalent equivalentToSubpropertyObjectProperty
         subPropertyOfObjectProperty.addAll(super.getAllResourceObjects(model,Vocabulary.EQUIVALENTTOSUBPROPERTYOBJECTPROPERTY_PROPERTY_IRI,class1.getIri()));
        for(Resource propValue:subPropertyOfObjectProperty){
            if(propValue.isIRI()) {
                Class2Int subPropertyOfObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(subPropertyOfObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class1.addSubPropertyOfObjectProperty(subPropertyOfObjectPropertyInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class2Int subPropertyOfObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(subPropertyOfObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class1.addSubPropertyOfObjectProperty(subPropertyOfObjectPropertyInstance);
                    }
                 }
            }
        }
        Set<Resource> subPropertyOfSubpropertyObjectProperty = super.getAllResourceObjects(model,Vocabulary.SUBPROPERTYOFSUBPROPERTYOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
        for(Resource propValue:subPropertyOfSubpropertyObjectProperty){
            if(propValue.isIRI()) {
                Class2Int subPropertyOfSubpropertyObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(subPropertyOfSubpropertyObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class1.addSubPropertyOfSubpropertyObjectProperty(subPropertyOfSubpropertyObjectPropertyInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class2Int subPropertyOfSubpropertyObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(subPropertyOfSubpropertyObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class1.addSubPropertyOfSubpropertyObjectProperty(subPropertyOfSubpropertyObjectPropertyInstance);
                    }
                 }
            }
        }
        Set<Resource> subPropertyOfInverseFunctionalObjectProperty = super.getAllResourceObjects(model,Vocabulary.SUBPROPERTYOFINVERSEFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
        for(Resource propValue:subPropertyOfInverseFunctionalObjectProperty){
            if(propValue.isIRI()) {
                Class2Int subPropertyOfInverseFunctionalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(subPropertyOfInverseFunctionalObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class1.addSubPropertyOfInverseFunctionalObjectProperty(subPropertyOfInverseFunctionalObjectPropertyInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class2Int subPropertyOfInverseFunctionalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(subPropertyOfInverseFunctionalObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class1.addSubPropertyOfInverseFunctionalObjectProperty(subPropertyOfInverseFunctionalObjectPropertyInstance);
                    }
                 }
            }
        }
        Set<Resource> subPropertyOfFunctionalObjectProperty = super.getAllResourceObjects(model,Vocabulary.SUBPROPERTYOFFUNCTIONALOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
        for(Resource propValue:subPropertyOfFunctionalObjectProperty){
            if(propValue.isIRI()) {
                Class2Int subPropertyOfFunctionalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(subPropertyOfFunctionalObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class1.addSubPropertyOfFunctionalObjectProperty(subPropertyOfFunctionalObjectPropertyInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class2Int subPropertyOfFunctionalObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(subPropertyOfFunctionalObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class1.addSubPropertyOfFunctionalObjectProperty(subPropertyOfFunctionalObjectPropertyInstance);
                    }
                 }
            }
        }
        Set<Resource> subPropertyOfEquivalentObjectProperty = super.getAllResourceObjects(model,Vocabulary.SUBPROPERTYOFEQUIVALENTOBJECTPROPERTY_PROPERTY_IRI,class1.getIri());
        for(Resource propValue:subPropertyOfEquivalentObjectProperty){
            if(propValue.isIRI()) {
                Class2Int subPropertyOfEquivalentObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(subPropertyOfEquivalentObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                class1.addSubPropertyOfEquivalentObjectProperty(subPropertyOfEquivalentObjectPropertyInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Class2Int subPropertyOfEquivalentObjectPropertyInstance = new Class2Serialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(subPropertyOfEquivalentObjectPropertyInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        class1.addSubPropertyOfEquivalentObjectProperty(subPropertyOfEquivalentObjectPropertyInstance);
                    }
                 }
            }
        }

        IRI domainIsAbstractUnionFunctional = super.getFirstIriObject(model,Vocabulary.DOMAINISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,class1.getIri());
        if ( domainIsAbstractUnionFunctional != null ){
            Class3 domainIsAbstractUnionFunctionalInstance = new Class3Serialization().getInstanceFromModel(model, domainIsAbstractUnionFunctional,nestingLevel);
            class1.setDomainIsAbstractUnionFunctional(domainIsAbstractUnionFunctionalInstance);
        }
    }

    @Override
    public Class1 getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Class1.CLASS_IRI);
        if(statements.size() != 0){
            Class1 class1 = new Class1(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, class1,nestingLevel);
            }
            return class1;
        }

        return null;
    }

    @Override
    public Collection<Class1> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Class1.CLASS_IRI);
        Collection<Class1> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Class1 class1 = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(class1);
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
        model.remove(instanceIri,RDF.TYPE,Class1.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Class1 class1){
        Set<Value> rdfCollections = model.filter(class1.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(class1.getIri(),null,node);
        }

        Model statements = model.filter(class1.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,class1);
    }



}

