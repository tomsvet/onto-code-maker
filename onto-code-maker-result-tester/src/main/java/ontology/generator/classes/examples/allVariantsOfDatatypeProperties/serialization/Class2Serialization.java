
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

public class Class2Serialization extends SerializationModel<Class2Int>{

    @Override
    public void addToModel(Model model, Class2Int class2) {
        model.add(class2.getIri(),RDF.TYPE, class2.getClassIRI());
        addPropertiesToModel(model,class2);

    }

    protected void addPropertiesToModel(Model model,  Class2Int class2) {
        List<Object> hasInt2Pom = new ArrayList<>();
        hasInt2Pom.addAll(class2.getHasInt2());
        setLiteralsRDFCollection(model,class2.getIri(),Vocabulary.CLASS2_HASINT2_PROPERTY_IRI,hasInt2Pom);

        List<Object> hasIntSubpropertyOfEquivalentPom = new ArrayList<>();
        hasIntSubpropertyOfEquivalentPom.addAll(class2.getHasIntSubpropertyOfEquivalent());
        setLiteralsRDFCollection(model,class2.getIri(),Vocabulary.CLASS2_HASINTSUBPROPERTYOFEQUIVALENT_PROPERTY_IRI,hasIntSubpropertyOfEquivalentPom);


        List<Object> hasIntSubpropertyOfDOubleEquivalentsPom = new ArrayList<>();
        hasIntSubpropertyOfDOubleEquivalentsPom.addAll(class2.getHasIntSubpropertyOfDOubleEquivalents());
        setLiteralsRDFCollection(model,class2.getIri(),Vocabulary.CLASS2_HASINTSUBPROPERTYOFDOUBLEEQUIVALENTS_PROPERTY_IRI,hasIntSubpropertyOfDOubleEquivalentsPom);


        List<Object> hasInt3Pom = new ArrayList<>();
        hasInt3Pom.addAll(class2.getHasInt3());
        hasInt3Pom.removeAll(class2.getHasIntSubproperty2());
        setLiteralsRDFCollection(model,class2.getIri(),Vocabulary.CLASS2_HASINT3_PROPERTY_IRI,hasInt3Pom);

        List<Object> hasIntSubproperty2Pom = new ArrayList<>();
        hasIntSubproperty2Pom.addAll(class2.getHasIntSubproperty2());
        setLiteralsRDFCollection(model,class2.getIri(),Vocabulary.CLASS2_HASINTSUBPROPERTY2_PROPERTY_IRI,hasIntSubproperty2Pom);



        if(class2.getDomainIsAbstractUnionFunctional() != null){
            model.add(class2.getIri(),Vocabulary.UNIONOFCLASS2CLASS1ABSTRACT_DOMAINISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,Values.literal(class2.getDomainIsAbstractUnionFunctional()));
        }
    }

    protected void setProperties(Model model,Class2Int class2,int nestingLevel) throws Exception{
        Set<Value> hasInt2 = super.getAllObjects(model,Vocabulary.CLASS2_HASINT2_PROPERTY_IRI,class2.getIri());
        // check equivalent hasIntEquivalentOfEquivalent
        hasInt2.addAll(super.getAllObjects(model,Vocabulary.CLASS2_HASINTEQUIVALENTOFEQUIVALENT_PROPERTY_IRI,class2.getIri()));
        // check equivalent hasIntEquivalent2
        hasInt2.addAll(super.getAllObjects(model,Vocabulary.CLASS2_HASINTEQUIVALENT2_PROPERTY_IRI,class2.getIri()));
        for(Value propValue:hasInt2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class2.addHasInt2(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class2.addHasInt2(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Set<Value> hasIntSubpropertyOfEquivalent = super.getAllObjects(model,Vocabulary.CLASS2_HASINTSUBPROPERTYOFEQUIVALENT_PROPERTY_IRI,class2.getIri());
        for(Value propValue:hasIntSubpropertyOfEquivalent){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class2.addHasIntSubpropertyOfEquivalent(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class2.addHasIntSubpropertyOfEquivalent(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Set<Value> hasIntSubpropertyOfDOubleEquivalents = super.getAllObjects(model,Vocabulary.CLASS2_HASINTSUBPROPERTYOFDOUBLEEQUIVALENTS_PROPERTY_IRI,class2.getIri());
        for(Value propValue:hasIntSubpropertyOfDOubleEquivalents){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class2.addHasIntSubpropertyOfDOubleEquivalents(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class2.addHasIntSubpropertyOfDOubleEquivalents(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Set<Value> hasInt3 = super.getAllObjects(model,Vocabulary.CLASS2_HASINT3_PROPERTY_IRI,class2.getIri());
        for(Value propValue:hasInt3){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class2.addHasInt3(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class2.addHasInt3(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Set<Value> hasIntSubproperty2 = super.getAllObjects(model,Vocabulary.CLASS2_HASINTSUBPROPERTY2_PROPERTY_IRI,class2.getIri());
        // check equivalent hasIntEquivalentOfSubproperty
        hasIntSubproperty2.addAll(super.getAllObjects(model,Vocabulary.CLASS2_HASINTEQUIVALENTOFSUBPROPERTY_PROPERTY_IRI,class2.getIri()));
        for(Value propValue:hasIntSubproperty2){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                class2.addHasIntSubproperty2(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        class2.addHasIntSubproperty2(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }


        Literal domainIsAbstractUnionFunctional = super.getFirstLiteralObject(model,Vocabulary.UNIONOFCLASS2CLASS1ABSTRACT_DOMAINISABSTRACTUNIONFUNCTIONAL_PROPERTY_IRI,class2.getIri());
        if ( domainIsAbstractUnionFunctional != null ){
            class2.setDomainIsAbstractUnionFunctional(domainIsAbstractUnionFunctional.stringValue() );
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

