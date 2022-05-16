
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
import ontology.generator.classes.examples.family.entities.HumanInt;
import ontology.generator.classes.examples.family.entities.Cat;
import ontology.generator.classes.examples.family.entities.Dog;
import ontology.generator.classes.examples.family.entities.Dog;

public class HumanSerialization extends SerializationModel<HumanInt>{

    @Override
    public void addToModel(Model model, HumanInt human) {
        model.add(human.getIri(),RDF.TYPE, human.getClassIRI());
        addPropertiesToModel(model,human);

    }

    protected void addPropertiesToModel(Model model,  HumanInt human) {
        List<Object> hasLuckyNumbersPom = new ArrayList<>();
        hasLuckyNumbersPom.addAll(human.getHasLuckyNumbers());
        setLiteralsRDFCollection(model,human.getIri(),Vocabulary.HASLUCKYNUMBERS_PROPERTY_IRI,hasLuckyNumbersPom);

        if(human.getHasAge() != null){
            model.add(human.getIri(),Vocabulary.HASAGE_PROPERTY_IRI,Values.literal(human.getHasAge()));
        }

        List<OntoEntity> hasCatPom = new ArrayList<>();
        hasCatPom.addAll(human.getHasCat());
        setRDFCollection(model,human.getIri(),Vocabulary.HASCAT_PROPERTY_IRI,hasCatPom);

        if(human.getHasDog() != null ){
            model.add(human.getIri(),Vocabulary.HASDOG_PROPERTY_IRI,human.getHasDog().getIri());
        }

        List<Object> agePom = new ArrayList<>();
        agePom.addAll(human.getAge());
        setLiteralsRDFCollection(model,human.getIri(),Vocabulary.AGE_PROPERTY_IRI,agePom);



    }

    protected void setProperties(Model model,HumanInt human,int nestingLevel) throws Exception{
        Set<Value> hasLuckyNumbers = super.getAllObjects(model,Vocabulary.HASLUCKYNUMBERS_PROPERTY_IRI,human.getIri());
        for(Value propValue:hasLuckyNumbers){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                human.addHasLuckyNumbers(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        human.addHasLuckyNumbers(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }

        Literal hasAge = super.getFirstLiteralObject(model,Vocabulary.HASAGE_PROPERTY_IRI,human.getIri());
        if ( hasAge != null ){
            human.setHasAge(Integer.valueOf(hasAge.intValue()) );
        }
        Set<Resource> hasCat = super.getAllResourceObjects(model,Vocabulary.HASCAT_PROPERTY_IRI,human.getIri());
        for(Resource propValue:hasCat){
            if(propValue.isIRI()) {
                Cat hasCatInstance = new CatSerialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(hasCatInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                human.addHasCat(hasCatInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Cat hasCatInstance = new CatSerialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(hasCatInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        human.addHasCat(hasCatInstance);
                    }
                 }
            }
        }
        IRI hasDog = super.getFirstIriObject(model,Vocabulary.HASDOG_PROPERTY_IRI,human.getIri());
         if ( hasDog == null){
              // check equivalent property hasDogEq
               hasDog = super.getFirstIriObject(model,Vocabulary.HASDOGEQ_PROPERTY_IRI,human.getIri());
         }
        if ( hasDog != null ){
            Dog hasDogInstance = new DogSerialization().getInstanceFromModel(model, hasDog,nestingLevel);
            human.setHasDog(hasDogInstance);
        }
        Set<Value> age = super.getAllObjects(model,Vocabulary.AGE_PROPERTY_IRI,human.getIri());
        for(Value propValue:age){
            if(propValue.isLiteral()) {
                Literal literalValue = (Literal) propValue;
                human.addAge(Integer.valueOf(literalValue.intValue()) );
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    Literal literalValue = (Literal) value;
                    if(value.isLiteral()){
                        human.addAge(Integer.valueOf(literalValue.intValue()) );

                    }
                }
            }
        }


    }

    @Override
    public HumanInt getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Human.CLASS_IRI);
        if(statements.size() != 0){
            Human human = new Human(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, human,nestingLevel);
            }
            return human;
        }

        return null;
    }

    @Override
    public Collection<HumanInt> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Human.CLASS_IRI);
        Collection<HumanInt> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                HumanInt human = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(human);
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
        model.remove(instanceIri,RDF.TYPE,Human.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,HumanInt human){
        Set<Value> rdfCollections = model.filter(human.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(human.getIri(),null,node);
        }

        Model statements = model.filter(human.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,human);
    }



}

