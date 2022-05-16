
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
import ontology.generator.classes.examples.family.entities.WomanInt;
import ontology.generator.classes.examples.family.entities.Men;
import ontology.generator.classes.examples.family.entities.Men;

public class WomanSerialization extends SerializationModel<WomanInt>{

    @Override
    public void addToModel(Model model, WomanInt woman) {
        model.add(woman.getIri(),RDF.TYPE, woman.getClassIRI());
        addPropertiesToModel(model,woman);

    }

    protected void addPropertiesToModel(Model model,  WomanInt woman) {
        List<OntoEntity> hasHusbandPom = new ArrayList<>();
        hasHusbandPom.addAll(woman.getHasHusband());
        setRDFCollection(model,woman.getIri(),Vocabulary.HASHUSBAND_PROPERTY_IRI,hasHusbandPom);

        if(woman.getMen() != null ){
            model.add(woman.getIri(),Vocabulary.MEN_PROPERTY_IRI,woman.getMen().getIri());
        }


         new HumanSerialization().addPropertiesToModel(model, woman);
    }

    protected void setProperties(Model model,WomanInt woman,int nestingLevel) throws Exception{
        Set<Resource> hasHusband = super.getAllResourceObjects(model,Vocabulary.HASHUSBAND_PROPERTY_IRI,woman.getIri());
        // add also all values from inverse property hasWife
         hasHusband.addAll(super.getAllIRISubjects(model,Vocabulary.HASWIFE_PROPERTY_IRI,woman.getIri()));
        for(Resource propValue:hasHusband){
            if(propValue.isIRI()) {
                Men hasHusbandInstance = new MenSerialization().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(hasHusbandInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                woman.addHasHusband(hasHusbandInstance);
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        Men hasHusbandInstance = new MenSerialization().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(hasHusbandInstance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        woman.addHasHusband(hasHusbandInstance);
                    }
                 }
            }
        }
        IRI men = super.getFirstIriObject(model,Vocabulary.MEN_PROPERTY_IRI,woman.getIri());
        if ( men == null){
            // check inverse functional property
            men = super.getSubjectOfCollectionValue(model,Vocabulary.HASWIFE_PROPERTY_IRI,woman.getIri());
        }
        if ( men != null ){
            Men menInstance = new MenSerialization().getInstanceFromModel(model, men,nestingLevel);
            woman.setMen(menInstance);
        }

        new HumanSerialization().setProperties(model, woman,nestingLevel);
    }

    @Override
    public WomanInt getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Woman.CLASS_IRI);
        if(statements.size() != 0){
            Woman woman = new Woman(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, woman,nestingLevel);
            }
            return woman;
        }

        return null;
    }

    @Override
    public Collection<WomanInt> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Woman.CLASS_IRI);
        Collection<WomanInt> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                WomanInt woman = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(woman);
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
        model.remove(instanceIri,RDF.TYPE,Woman.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,WomanInt woman){
        Set<Value> rdfCollections = model.filter(woman.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(woman.getIri(),null,node);
        }

        Model statements = model.filter(woman.getIri(),null,null);
        statements.removeIf(x -> !x.getPredicate().equals(RDF.TYPE));

        addPropertiesToModel(model,woman);
    }



}

