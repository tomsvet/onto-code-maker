
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

public class HumanSerialization extends SerializationModel<Human>{

    @Override
    public void addToModel(Model model, Human human) {
        model.add(human.getIri(),RDF.TYPE, human.getClassIRI());
        addPropertiesToModel(model,human);

    }

    protected void addPropertiesToModel(Model model,  Human human) {
        model.add(human.getIri(),Vocabulary.HUMAN_HASAGE_PROPERTY_IRI,Values.literal(human.getHasAge()));
        if(human.getHasDog() != null ){
            model.add(human.getIri(),Vocabulary.HUMAN_HASDOG_PROPERTY_IRI,human.getHasDog().getIri());
        }
        List<OntoEntity> hasCatPom = new ArrayList<>();
                hasCatPom.addAll(human.getHasCat());
        setRDFCollection(model,human.getIri(),Vocabulary.HUMAN_HASCAT_PROPERTY_IRI,hasCatPom);
    }

    protected void setProperties(Model model,Human human,int nestingLevel) throws Exception{

        Literal hasAge = super.getFirstLiteralObject(model,Vocabulary.HUMAN_HASAGE_PROPERTY_IRI,human.getIri());
        if ( hasAge != null ){
            human.setHasAge(hasAge.intValue() );
        }

        IRI hasDog = super.getFirstIriObject(model,Vocabulary.HUMAN_HASDOG_PROPERTY_IRI,human.getIri());
         if ( hasDog == null){
              // check equivalent property hasDogEq
               hasDog = super.getFirstIriObject(model,Vocabulary.HUMAN_HASDOGEQ_PROPERTY_IRI,human.getIri());
         }
        if ( hasDog != null ){
            Dog hasDogInstance = new DogSerialization().getInstanceFromModel(model, hasDog,nestingLevel);
            human.setHasDog(hasDogInstance);
        }


        Set<Resource> hasCat = super.getAllResourceObjects(model,Vocabulary.HUMAN_HASCAT_PROPERTY_IRI,human.getIri());
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

    }

    @Override
    public Human getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
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
    public Collection<Human> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Human.CLASS_IRI);
        Collection<Human> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Human human = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(human);
            }
        }

        allInstances.addAll( new MenSerialization().getAllInstancesFromModel(model,nestingLevel));
        allInstances.addAll( new WomanSerialization().getAllInstancesFromModel(model,nestingLevel));

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
    public void updateInstanceInModel(Model model,Human human){
        Set<Value> rdfCollections = model.filter(human.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(human.getIri(),null,(BNode)node);
        }

        Model statements = model.filter(human.getIri(),null,null);
        Iterator<Statement> iter = statements.iterator();
        while (iter.hasNext()) {
            Statement event = iter.next();
            if(!event.getPredicate().equals(RDF.TYPE)){
                iter.remove();
            }
        }
        addPropertiesToModel(model,human);
    }



}

