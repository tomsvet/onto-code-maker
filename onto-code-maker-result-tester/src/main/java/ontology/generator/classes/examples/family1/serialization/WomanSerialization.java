
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

public class WomanSerialization extends SerializationModel<Woman>{

    @Override
    public void addToModel(Model model, Woman woman) {
        model.add(woman.getIri(),RDF.TYPE, woman.getClassIRI());
        addPropertiesToModel(model,woman);

    }

    protected void addPropertiesToModel(Model model,  Woman woman) {
        List<OntoEntity> hasHusbandPom = new ArrayList<>();
                hasHusbandPom.addAll(woman.getHasHusband());
        setRDFCollection(model,woman.getIri(),Vocabulary.WOMAN_HASHUSBAND_PROPERTY_IRI,hasHusbandPom);
        if(woman.getMen() != null ){
            model.add(woman.getIri(),Vocabulary.WOMAN_MEN_PROPERTY_IRI,woman.getMen().getIri());
        }
        new HumanSerialization().addPropertiesToModel(model, woman);
    }

    protected void setProperties(Model model,Woman woman,int nestingLevel) throws Exception{

        Set<Resource> hasHusband = super.getAllResourceObjects(model,Vocabulary.WOMAN_HASHUSBAND_PROPERTY_IRI,woman.getIri());
        // add also all values from inverse property hasWife
         hasHusband.addAll(super.getAllIRISubjects(model,Vocabulary.MEN_HASWIFE_PROPERTY_IRI,woman.getIri()));
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

        IRI men = super.getFirstIriObject(model,Vocabulary.WOMAN_MEN_PROPERTY_IRI,woman.getIri());
        if ( men == null){
            // check inverse functional property
            men = super.getSubjectOfCollectionValue(model,Vocabulary.MEN_HASWIFE_PROPERTY_IRI,woman.getIri());
        }
        if ( men != null ){
            Men menInstance = new MenSerialization().getInstanceFromModel(model, men,nestingLevel);
            woman.setMen(menInstance);
        }

        new HumanSerialization().setProperties(model, woman,nestingLevel);
    }

    @Override
    public Woman getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
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
    public Collection<Woman> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Woman.CLASS_IRI);
        Collection<Woman> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Woman woman = getInstanceFromModel(model,iri,nestingLevel);
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
    public void updateInstanceInModel(Model model,Woman woman){
        Set<Value> rdfCollections = model.filter(woman.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(woman.getIri(),null,(BNode)node);
        }

        Model statements = model.filter(woman.getIri(),null,null);
        Iterator<Statement> iter = statements.iterator();
        while (iter.hasNext()) {
            Statement event = iter.next();
            if(!event.getPredicate().equals(RDF.TYPE)){
                iter.remove();
            }
        }
        addPropertiesToModel(model,woman);
    }



}

