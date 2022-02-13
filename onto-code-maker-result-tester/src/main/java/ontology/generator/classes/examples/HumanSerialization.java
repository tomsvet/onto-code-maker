package ontology.generator.classes.examples;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.util.ArrayList;
import java.util.Collection;

public class HumanSerialization implements SerializationModel<Human> {


    @Override
    public void addToModel(Model model, Human human) {
        model.add(human.getIri(),RDF.TYPE, human.getClassIRI());
        //model.add
    }

    @Override
    public Human getInstanceFromModel(Model model,IRI instanceIri) {
        Model statements = model.filter(instanceIri,RDF.TYPE,Human.CLASS_IRI);
        if(statements.size() != 0){
            Human human = new Human(instanceIri);
            //model.filter(instanceIri,PROPERTY,null);
            //add properties if exist
            return human;
        }


        return null;
    }

    @Override
    public Collection<Human> getAllInstancesFromModel(Model model) {
        Model statements = model.filter(null,RDF.TYPE,Human.CLASS_IRI);
        Collection<Human> humans = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                Human human = new Human(iri);
                //add properties if exist
                humans.add(human);
            }

        }
        return humans;
    }

    @Override
    public void removeInstanceFromModel(Model model,IRI instanceIri) {
        model.remove(instanceIri,RDF.TYPE,Human.CLASS_IRI);

        Model statements = model.filter(instanceIri,null,null);
        for(Statement statement:statements){
            model.remove(statement);
        }
    }

}
