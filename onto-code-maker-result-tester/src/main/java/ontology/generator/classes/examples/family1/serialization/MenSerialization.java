
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

public class MenSerialization extends SerializationModel<Men>{

    @Override
    public void addToModel(Model model, Men men) {
        model.add(men.getIri(),RDF.TYPE, men.getClassIRI());
        addPropertiesToModel(model,men);

    }

    protected void addPropertiesToModel(Model model,  Men men) {
        if(men.getHasWife() != null ){
            model.add(men.getIri(),Vocabulary.MEN_HASWIFE_PROPERTY_IRI,men.getHasWife().getIri());
        }
        new HumanSerialization().addPropertiesToModel(model, men);
    }

    protected void setProperties(Model model,Men men,int nestingLevel) throws Exception{

        IRI hasWife = super.getFirstIriObject(model,Vocabulary.MEN_HASWIFE_PROPERTY_IRI,men.getIri());
        if ( hasWife == null){
            // check inverse functional property
            hasWife = super.getSubjectOfCollectionValue(model,Vocabulary.WOMAN_MEN_PROPERTY_IRI,men.getIri());
        }
        if ( hasWife == null){
            // check inverse property hasHusband
            hasWife = super.getSubjectOfCollectionValue(model,Vocabulary.WOMAN_HASHUSBAND_PROPERTY_IRI,men.getIri());
            //hasWife = super.getFirstIRISubject(model,Vocabulary.WOMAN_HASHUSBAND_PROPERTY_IRI,men.getIri());
        }
        if ( hasWife != null ){
            Woman hasWifeInstance = new WomanSerialization().getInstanceFromModel(model, hasWife,nestingLevel);
            men.setHasWife(hasWifeInstance);
        }

        new HumanSerialization().setProperties(model, men,nestingLevel);
    }

    @Override
    public Men getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,Men.CLASS_IRI);
        if(statements.size() != 0){
            Men men = new Men(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, men,nestingLevel);
            }
            return men;
        }

        return null;
    }

    @Override
    public Collection<Men> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,Men.CLASS_IRI);
        Collection<Men> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Men men = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(men);
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
        model.remove(instanceIri,RDF.TYPE,Men.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,Men men){
        Set<Value> rdfCollections = model.filter(men.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(men.getIri(),null,(BNode)node);
        }

        Model statements = model.filter(men.getIri(),null,null);
        Iterator<Statement> iter = statements.iterator();
        while (iter.hasNext()) {
            Statement event = iter.next();
            if(!event.getPredicate().equals(RDF.TYPE)){
                iter.remove();
            }
        }
        addPropertiesToModel(model,men);
    }



}

