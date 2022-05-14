
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
import ontology.generator.classes.examples.allVariantsOfDatatypeProperties.entities.Class1;

public class Class1Serialization extends SerializationModel<Class1>{

    @Override
    public void addToModel(Model model, Class1 class1) {
        model.add(class1.getIri(),RDF.TYPE, class1.getClassIRI());
        addPropertiesToModel(model,class1);

    }

    protected void addPropertiesToModel(Model model,  Class1 class1) {
        if(class1.getHasIntFunctionalProp() != null){
            model.add(class1.getIri(),Vocabulary.HASINTFUNCTIONALPROP_PROPERTY_IRI,Values.literal(class1.getHasIntFunctionalProp()));
        }

        List<Object> hasIntInverseFunctionalPropPom = new ArrayList<>();
        hasIntInverseFunctionalPropPom.addAll(class1.getHasIntInverseFunctionalProp());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASINTINVERSEFUNCTIONALPROP_PROPERTY_IRI,hasIntInverseFunctionalPropPom);

        List<Object> hasIntPom = new ArrayList<>();
        hasIntPom.addAll(class1.getHasInt());
        hasIntPom.removeAll(class1.getHasIntSubproperty());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASINT_PROPERTY_IRI,hasIntPom);

        if(class1.getHasIntFunctionalProp2() != null){
            model.add(class1.getIri(),Vocabulary.HASINTFUNCTIONALPROP2_PROPERTY_IRI,Values.literal(class1.getHasIntFunctionalProp2()));
        }


        List<Object> hasIntSubPropertyOfFunctionalPom = new ArrayList<>();
        hasIntSubPropertyOfFunctionalPom.addAll(class1.getHasIntSubPropertyOfFunctional());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASINTSUBPROPERTYOFFUNCTIONAL_PROPERTY_IRI,hasIntSubPropertyOfFunctionalPom);


        List<Object> hasIntSubpropertyPom = new ArrayList<>();
        hasIntSubpropertyPom.addAll(class1.getHasIntSubproperty());
        setLiteralsRDFCollection(model,class1.getIri(),Vocabulary.HASINTSUBPROPERTY_PROPERTY_IRI,hasIntSubpropertyPom);


        if(class1.getDomainIsAbstractUnionFunctional() != null){
            model.add(class1.getIri(),Vocabulary.DOMAINISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,Values.literal(class1.getDomainIsAbstractUnionFunctional()));
        }
    }

    protected void setProperties(Model model,Class1 class1,int nestingLevel) throws Exception{
        Literal hasIntFunctionalProp = super.getFirstLiteralObject(model,Vocabulary.HASINTFUNCTIONALPROP_PROPERTY_IRI,class1.getIri());
        if ( hasIntFunctionalProp != null ){
            class1.setHasIntFunctionalProp(Integer.valueOf(hasIntFunctionalProp.intValue()) );
        }
        Set<Value> hasIntInverseFunctionalProp = super.getAllObjects(model,Vocabulary.HASINTINVERSEFUNCTIONALPROP_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasIntInverseFunctionalProp){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasIntInverseFunctionalProp(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasIntInverseFunctionalProp(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Set<Value> hasInt = super.getAllObjects(model,Vocabulary.HASINT_PROPERTY_IRI,class1.getIri());
        // check equivalent hasIntEquivalent
        hasInt.addAll(super.getAllObjects(model,Vocabulary.HASINTEQUIVALENT_PROPERTY_IRI,class1.getIri()));
        for(Value propValue:hasInt){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasInt(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasInt(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Literal hasIntFunctionalProp2 = super.getFirstLiteralObject(model,Vocabulary.HASINTFUNCTIONALPROP2_PROPERTY_IRI,class1.getIri());
        if ( hasIntFunctionalProp2 != null ){
            class1.setHasIntFunctionalProp2(Integer.valueOf(hasIntFunctionalProp2.intValue()) );
        }
        Set<Value> hasIntSubPropertyOfFunctional = super.getAllObjects(model,Vocabulary.HASINTSUBPROPERTYOFFUNCTIONAL_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasIntSubPropertyOfFunctional){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasIntSubPropertyOfFunctional(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasIntSubPropertyOfFunctional(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Set<Value> hasIntSubproperty = super.getAllObjects(model,Vocabulary.HASINTSUBPROPERTY_PROPERTY_IRI,class1.getIri());
        for(Value propValue:hasIntSubproperty){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class1.addHasIntSubproperty(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class1.addHasIntSubproperty(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }


        Literal domainIsAbstractUnionFunctional = super.getFirstLiteralObject(model,Vocabulary.DOMAINISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,class1.getIri());
        if ( domainIsAbstractUnionFunctional != null ){
            class1.setDomainIsAbstractUnionFunctional(domainIsAbstractUnionFunctional.stringValue() );
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

